package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.vpriv.classes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.tepi.filtertable.FilterTable;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.pdf.OnDemandFileDownloader;
import archenoah.lib.tool.pdf.OnDemandFileDownloader.OnDemandStreamResource;
import archenoah.lib.tool.pdf.PdfPopulater;
import archenoah.lib.tool.pdf.PdfPopulater.FormElement;
import archenoah.lib.vaadin.Language.i18n.I18nConverter;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.combined.classes.CombinedPdfPrint.CombinedPrintable;

import com.google.common.base.Joiner;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.server.Page;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Layout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

public class VprivPdfPrint implements CombinedPrintable{

    private I18nConverter lngIntensities;
    
    private FilterTable table;
    
//    private final String LNG_ID = "401";
    private final String tableIdCol = "PVD_ID";
    
    private boolean includeName = false;
    
    public VprivPdfPrint(FilterTable table) {
        this.table = table;
        init();
    }
    
    public VprivPdfPrint(boolean includeName) {
        this.includeName = includeName;
        init();
    }
    
    private void init(){
        lngFill();
    }
    
    /* ******************
     * Public 
     */
    
    public void attach(AbstractComponent component){
        
        new OnDemandFileDownloader(new OnDemandStreamResource() {

            @Override
            public String getFilename() {
                return "VPRIV-"+ DateTimeFormat.forStyle("SS").print(new DateTime())  + ".zip";
            }

            @Override
            public byte[] getOutput() {
                return generateOutput();
            }

            @Override
            public String getContentType() {
                return "application/zip";
            }
            
        }).extend(component);
        
    }

    public void attach(Layout layout, MenuBar.MenuItem menuItem){
        
        final String downloadButtonID = "downloadPdfReplagal";
        
        final Button downloadInvisibleButton = new Button();
        downloadInvisibleButton.setId(downloadButtonID);
        downloadInvisibleButton.addStyleName("hidden");
        layout.addComponent(downloadInvisibleButton);
        attach(downloadInvisibleButton);
        
        menuItem.setCommand(new Command() {
            
            @Override
            public void menuSelected(MenuItem selectedItem) {
                // TASK Auto-generated method stub
                Page.getCurrent().getJavaScript().execute("document.getElementById('"+downloadButtonID+"').click();");
            }
        });
    }
    
    /**
     * zos must be close manually!
     * @param zos
     * @param filenames
     * @param idList
     */
    @Override
    public void writeEntriestoZip(ZipOutputStream zos, ArrayList<String> filenames, ArrayList<Object> idList) {
        
        try {
            
            generateEntries(zos, filenames, getExportData(idList));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    /* ********************
     * Private
     */
    
    private byte[] generateOutput(){
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        

        
        Collection values = (Collection) table.getValue();
        ArrayList<Object> pvdValues = new ArrayList<Object>();
        
        for (Object selectedValue : values) {
            pvdValues.add(table.getContainerProperty(selectedValue, tableIdCol).getValue());
        }
        
        
        Container rows = getExportData(pvdValues);
        
        ArrayList<String> filenames = new ArrayList<String>();
           
        
        
        ZipOutputStream zos = new ZipOutputStream(os);
        
        try {
            
            generateEntries(zos, filenames, rows);
            
            zos.finish();
            zos.close();

            
        } catch (IOException e) {
            // TASK Auto-generated catch block
            e.printStackTrace();
        }
        
        return os.toByteArray();
    }
    
    private void generateEntries(ZipOutputStream zos, ArrayList<String> filenames, Container rows) throws IOException {
        
        PdfPopulater pdfSrc = new PdfPopulater("pdf_vpriv");
        byte[] fileByteArray = pdfSrc.getFileByteArray();
        pdfSrc.cleanup();
        
        for (Object rowId : rows.getItemIds()) {
            
            Item item = rows.getItem(rowId);
            
            if(item == null){
                continue;
            }
            
            PropertysetItem propertyItem = getPropertyItem(item);
            ArrayList<FormElement> fieldList = fieldListFromItem(propertyItem);
            
            
            PdfPopulater pdf = new PdfPopulater(fileByteArray);
            pdf.fillFields(fieldList);
            
            //check if filename exists and append counter until it doesn't
            String filename = generatePdfName(propertyItem);
            String fileZipName = filename;
            Integer counter = 0;
            while(filenames.contains(fileZipName)){
                fileZipName = filename + "-" + ++counter;
            }
            filenames.add(fileZipName);
            zos.putNextEntry(new ZipEntry(fileZipName + ".pdf"));

            zos.write(pdf.finish().toByteArray());
            zos.closeEntry();
            
        }
    }
    
    private PropertysetItem getPropertyItem(Item rowItem){
        
        ArrayList<FormElement> fieldList = new ArrayList<FormElement>();
        
        DateTimeFormatter fmt = DateTimeFormat.forStyle("M-");
        
        
        PropertysetItem item = new PropertysetItem();
        
        
        //item.getItemProperty("PVD_DATE").setValue();
        
        //System.out.println(rowItem.getItemProperty("PVD_ID"));
        
        for (Object pid : rowItem.getItemPropertyIds()) {
            
            item.addItemProperty(pid, rowItem.getItemProperty(pid));
            
        }
        
        // PVD_DATE
        if(rowItem.getItemProperty("PVD_DATE").getValue() != null){
            LocalDate PVD_date = new LocalDate(rowItem.getItemProperty("PVD_DATE").getValue());
            item.removeItemProperty("PVD_DATE");
            item.addItemProperty("PVD_DATE", new ObjectProperty<String>(fmt.print(PVD_date)));
        }
        
        //PVD_FAHRTZEIT_N
        if(rowItem.getItemProperty("PVD_FAHRTZEIT_STD_N").getValue() != null){
            Integer PVD_fahrzeit = (Integer) rowItem.getItemProperty("PVD_FAHRTZEIT_STD_N").getValue() * 60 + 
                                    (Integer) rowItem.getItemProperty("PVD_FAHRTZEIT_MIN_N").getValue();
            item.addItemProperty("PVD_FAHRTZEIT_N", new ObjectProperty<String>(PVD_fahrzeit.toString()));
        }
        
        // PSL_GEB_DATUM
        if(rowItem.getItemProperty("PSL_GEB_DATUM").getValue() != null){
            LocalDate psl_geb_datum = new LocalDate(rowItem.getItemProperty("PSL_GEB_DATUM").getValue());
            item.removeItemProperty("PSL_GEB_DATUM");
            item.addItemProperty("PSL_GEB_DATUM", new ObjectProperty<String>(fmt.print(psl_geb_datum)));
        }
        
        // PVD_INFUSIONS_REAKTION
        
        item.removeItemProperty("PVD_INFUSION_REACTION_TODAY_INTENSITY");
        item.removeItemProperty("PVD_INFUSION_REACTION_RECENT_INTENSITY");
        item.removeItemProperty("PVD_INFUSIONS_REAKTION_VERG");
        item.removeItemProperty("PVD_INFUSIONS_REAKTION_HEUTE");
        
        Integer pvd_infusions_reaktion_heute_int = (Integer) rowItem.getItemProperty("PVD_INFUSIONS_REAKTION_HEUTE").getValue();
        Integer pvd_infusions_reaktion_verg_int = (Integer) rowItem.getItemProperty("PVD_INFUSIONS_REAKTION_VERG").getValue();

        item.addItemProperty("PVD_INFUSIONS_REAKTION_HEUTE", new ObjectProperty<Boolean>(MyUtils.equalsWithNulls(pvd_infusions_reaktion_heute_int, 1)));
        item.addItemProperty("PVD_INFUSIONS_REAKTION_HEUTE_NOT", new ObjectProperty<Boolean>(MyUtils.equalsWithNulls(pvd_infusions_reaktion_heute_int, 0)));


        item.addItemProperty("PVD_INFUSIONS_REAKTION_VERG", new ObjectProperty<Boolean>(MyUtils.equalsWithNulls(pvd_infusions_reaktion_verg_int, 1)));
        item.addItemProperty("PVD_INFUSIONS_REAKTION_VERG_NOT", new ObjectProperty<Boolean>(MyUtils.equalsWithNulls(pvd_infusions_reaktion_verg_int, 0)));
        
        
        //PVD_PRESCRIBED_DOSE_ADM
        Integer prescribed_dose_adm = null;
        if(rowItem.getItemProperty("PVD_PRESCRIBED_DOSE_ADM") != null && rowItem.getItemProperty("PVD_PRESCRIBED_DOSE_ADM").getValue() != null){
            prescribed_dose_adm = (Integer) rowItem.getItemProperty("PVD_PRESCRIBED_DOSE_ADM").getValue();
        }
        item.addItemProperty("PVD_PRESCRIBED_DOSE_ADM_TRUE", new ObjectProperty<Boolean>(MyUtils.equalsWithNulls(prescribed_dose_adm, 1)));
        item.addItemProperty("PVD_PRESCRIBED_DOSE_ADM_FALSE", new ObjectProperty<Boolean>(MyUtils.equalsWithNulls(prescribed_dose_adm, 0)));
        
        //PC_INTENSITY_TODAY
        item.removeItemProperty("PC_INTENSITY_TODAY");
        Property pc_intensity_today = rowItem.getItemProperty("PC_INTENSITY_TODAY");
        if(pc_intensity_today != null && pc_intensity_today.getValue() != null){
            item.addItemProperty("PC_INTENSITY_TODAY", new ObjectProperty<String>(lngIntensities.getLngText((String) pc_intensity_today.getValue())));
        }
        
        //PC_INTENSITY_RECENT
        item.removeItemProperty("PC_INTENSITY_RECENT");
        Property pc_intensity_recent = rowItem.getItemProperty("PC_INTENSITY_RECENT");
        if(pc_intensity_recent != null && pc_intensity_recent.getValue() != null){
            item.addItemProperty("PC_INTENSITY_RECENT", new ObjectProperty<String>(lngIntensities.getLngText((String) pc_intensity_recent.getValue())));
        }
        
        //DATE,
        item.addItemProperty("DATE", new ObjectProperty<String>(fmt.print(new Date().getTime())));
        
        //IS_PORT,
        Integer needleId = (Integer) rowItem.getItemProperty("PVD_PUNKT_NADEL").getValue();
        item.addItemProperty("IS_PORT", new ObjectProperty<Boolean>(MyUtils.equalsWithNulls(needleId, 4)));
        
        //PVD_INFUSIONSREAKTION_ZUSAMMENHANG_FALSE, PVD_INFUSIONSREAKTION_ZUSAMMENHANG_TRUE,
        Integer PVD_infusions_reaktion_zusammenhang = (Integer) rowItem.getItemProperty("PVD_INFUSIONSREAKTION_ZUSAMMENHANG").getValue();

        item.addItemProperty("PVD_INFUSIONSREAKTION_ZUSAMMENHANG_TRUE", new ObjectProperty<Boolean>(MyUtils.equalsWithNulls(PVD_infusions_reaktion_zusammenhang, 1)));
        item.addItemProperty("PVD_INFUSIONSREAKTION_ZUSAMMENHANG_FALSE", new ObjectProperty<Boolean>(MyUtils.equalsWithNulls(PVD_infusions_reaktion_zusammenhang, 0)));
        
        //PVMS_PAT_INITIALS -> PAT_CODE
        if(rowItem.getItemProperty("PVMS_PAT_INITIALS").getValue() != null){
            item.addItemProperty("PAT_CODE", new ObjectProperty<String>((String) rowItem.getItemProperty("PVMS_PAT_INITIALS").getValue()));
        }
        
        // Pat_Name
        if(includeName && rowItem.getItemProperty("NAME").getValue() != null){
            item.addItemProperty("Pat_Name", new ObjectProperty<String>((String) rowItem.getItemProperty("NAME").getValue()));
        }
        
        return item;
        
        
        
        
    }
    
    private ArrayList<FormElement> fieldListFromItem(Item item){
        ArrayList<FormElement> fieldList = new ArrayList<FormElement>();
        
        for (Object pid : item.getItemPropertyIds()) {
            fieldList.add(new FormElement((String) pid, item.getItemProperty(pid).getValue()));
        }
        
        return fieldList;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    private void lngFill() {
       
        lngIntensities = new I18nConverter("cust_protokoll_complications", "PC_INTENSITY");
        
    }
    
    
    

    
    private String generatePdfName(PropertysetItem item){
        
        StringBuffer str = new StringBuffer();
        
        DateTimeFormatter fmt = DateTimeFormat.forPattern("YYYY-MM-dd");
        DateTimeFormatter fmtp = DateTimeFormat.forStyle("M-");
        LocalDate ldt = new LocalDate();
        LocalDate ldtp = new LocalDate();
        if(item.getItemProperty("PVD_DATE").getValue() != null){
            ldtp = LocalDate.parse((String) item.getItemProperty("PVD_DATE").getValue(), fmtp);
        }
        
        
        str.append("VPRIV-");
        str.append(fmt.print(ldt));
        str.append("-");
        str.append(item.getItemProperty("PVD_ID").getValue());
        
        
        String code = (String) item.getItemProperty("PVMS_PAT_CODE").getValue(); 
        if(code != null && !code.equals("") ){
            str.append("-[");
            str.append(code);
            str.append("]");
        }
        
        if(item.getItemProperty("PVMS_PAT_INITIALS").getValue() != null){
        
            str.append("-");
            str.append(item.getItemProperty("PVMS_PAT_INITIALS").getValue());
        
        }
        
        
        if(item.getItemProperty("PVD_DATE").getValue() != null && ldtp.getYear() > 1900){
            str.append("-");
            str.append(fmtp.print(ldtp));
        }
        
        return str.toString();
        
    }
    
    /* **********
     * Database
     */
    
    
    private Container getExportData(Collection values) {
        
        DBClass db = new DBClass();
        
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("*");
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("CONCAT(PSL_TITEL,' ',PSA_NAME,' ',PSL_NAME,', ',PSL_VORNAME)", "NAME");
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("GROUP_CONCAT((CONCAT(PVC_COUNT, 'x ', PVC_NR)) SEPARATOR ', ')", "CHARGEN");
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("CONCAT(AUL_VORNAME,' ',AUL_NAME)", "NURSE");
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("GROUP_CONCAT(CONCAT(PVC_COUNT, 'x ', PVC_NR) ORDER BY PVC_ID ASC SEPARATOR ', ')", "CHARGEN");
        
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("reaction_today.PC_INTENSITY", "PC_INTENSITY_TODAY");
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("reaction_recent.PC_INTENSITY", "PC_INTENSITY_RECENT");
        
        
        
        

        /******************************** Table ****************************************/
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Verknüpfung("cust_protokoll_vpriv_daten", "RIGHT JOIN", "cust_patient_stammdaten_liste", "PVD_P_ID", "PSL_ID");
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Verknüpfung("cust_patient_stammdaten_liste", "RIGHT JOIN", "cust_patient_stammdaten_anrede", "PSL_ANREDE", "PSA_ID");
        
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Verknüpfung("cust_protokoll_vpriv_daten", "RIGHT JOIN", "cust_protokoll_vpriv_meta_dynamic", "PVD_DELEGATION_ID", "PVMD_ID");
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Verknüpfung("cust_protokoll_vpriv_meta_dynamic", "RIGHT JOIN", "cust_verk_haupt", "PVMD_VH_ID", "VH_ID");
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Verknüpfung("cust_protokoll_vpriv_meta_dynamic", "RIGHT JOIN", "cust_protokoll_vpriv_meta_static", "PVMD_VH_ID", "PVMS_VH_ID");
        
        
        // chargen
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Verknüpfung("cust_protokoll_vpriv_daten", "RIGHT JOIN", "cust_protokoll_vpriv_chargen", "PVD_ID", "PVC_PVD_ID");
        
        // projektcode
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Verknüpfung("cust_projekte_stammdaten_liste", "RIGHT JOIN", "cust_verk_haupt", "PSLV_ID", "VH_PROJEKT_ID");
        
        //nurse
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Verknüpfung("cust_protokoll_vpriv_daten", "INNER JOIN", "cust_krankenschwester_stammdaten_ks", "PVD_K_ID", "KSK_ID");
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Verknüpfung("cms_auth_stammdaten_user", "INNER JOIN", "cust_krankenschwester_stammdaten_ks", "AUL_ID", "KSK_U_ID");

        //needle
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Verknüpfung("cust_protokoll_vpriv_daten", "RIGHT JOIN", "cust_protokoll_nadeltypen", "PVD_PUNKT_NADEL", "PNT_ID");
        
        
        //intensities
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Verknüpfung_Graph("LEFT JOIN", "cust_protokoll_complications AS reaction_today", "reaction_today", "cust_protokoll_vpriv_daten", "PC_ID", "PVD_INFUSION_REACTION_TODAY_INTENSITY");
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Verknüpfung_Graph("LEFT JOIN", "cust_protokoll_complications AS reaction_recent", "reaction_recent", "cust_protokoll_vpriv_daten", "PC_ID", "PVD_INFUSION_REACTION_RECENT_INTENSITY");
        
        /******************************************************************************/
        /******************************** Filter ****************************************/
        //db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cust_protokoll_vpriv_daten", "PVD_ID", "=", dataId.toString(), "");
        db.DB_Data_Get.DB_Filter.DB_WHERE_In("cust_protokoll_vpriv_daten", "PVD_ID", Joiner.on(",").join(values), "");
        /******************************************************************************/
        
        db.DB_Data_Get.DB_Gruppieren.DB_Gruppieren("PVD_ID");

        // db.DB_Data_Get.Set_Type("SLF_VON_UHRZEIT", Date.class);
        // db.DB_Data_Get.Set_Type("SLF_BIS_UHRZEIT", Date.class);

        //db.DB_Data_Get.DB_DEBUG_SQL_STRING();
        
        //db.debugNextQuery(true);
        
        return db.DB_Data_Get.DB_SEND_AND_GET_Container();
        

    }
    
}
