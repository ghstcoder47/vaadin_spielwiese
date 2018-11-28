package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.pop.classes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.pdfbox.pdmodel.font.PDType1Font;
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
import archenoah.lib.tool.pdf.PdfPopulater.RowCell;
import archenoah.lib.tool.pdf.PdfPopulater.TableRow;
import archenoah.lib.vaadin.CustomConverterFactorys.BooleanToTextConverter;
import archenoah.lib.vaadin.Language.i18n.I18nConverter;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.combined.classes.CombinedPdfPrint.CombinedPrintable;

import com.google.common.base.Joiner;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.server.Page;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Layout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

public class PoPPdfPrint implements CombinedPrintable{
    
    private org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
    
    private FilterTable table;
    
    private final String LNG_ID = "410";
    private final String tableIdCol = "ID";
    
    private HashMap<Integer, DataBean> dataMap;

    private I18nConverter cust_protokoll_solvents;
    private I18nConverter cust_protokoll_complications;
    private I18nConverter cust_protokoll_pop_multi_meds_applications;
    private I18nConverter cust_protokoll_pop_typ;
    private BooleanToTextConverter booleanConverter;
    
    private DateTimeFormatter fmt;
    private DateTimeFormatter tfu;
    private DateTimeFormatter tfz;
    
    private boolean includeName = false;
    
    public PoPPdfPrint(FilterTable table) {
        this.table = table;
        init();
    }
    
    public PoPPdfPrint(boolean includeName) {
        this.includeName = includeName;
        init();
    }
    
    private void init(){
        lngFill();
        
        fmt = DateTimeFormat.forStyle("M-");
        tfu = DateTimeFormat.forStyle("-S").withZoneUTC();
        tfz = DateTimeFormat.forStyle("-S");
        
    }
    
    /* ******************
     * Public 
     */
    
    public void test() {
        
        fillBeanList();
        
    }
    
    public void attach(AbstractComponent component){
        
        new OnDemandFileDownloader(new OnDemandStreamResource() {

            @Override
            public String getFilename() {
                return "POP-"+ DateTimeFormat.forStyle("SS").print(new DateTime())  + ".zip";
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
        
        final String downloadButtonID = "downloadPdfPoP";
        
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
        
        fillBeanList(idList);
        
        try {
            
            generateEntries(zos, filenames);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    /* ********************
     * Private
     */
    
    private byte[] generateOutput(){
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        
        
        fillBeanList();
        
        
        ArrayList<String> filenames = new ArrayList<String>();
           
        ZipOutputStream zos = new ZipOutputStream(os);
        
        try {
            
            generateEntries(zos, filenames);
            
            zos.finish();
            zos.close();

            
        } catch (IOException e) {
            // TASK Auto-generated catch block
            e.printStackTrace();
        }
        
        return os.toByteArray();
    }
    
    private void generateEntries(ZipOutputStream zos, ArrayList<String> filenames) throws IOException {
        
        PdfPopulater pdfSrc = new PdfPopulater("pdf_pop");
        byte[] fileByteArray = pdfSrc.getFileByteArray();
        pdfSrc.cleanup();
        
        for (Entry<Integer, DataBean> entry : dataMap.entrySet()) {
            
            if(entry == null){
                continue;
            }
            
            Integer beanId = entry.getKey();
            DataBean bean = entry.getValue();
            
            PropertysetItem propertyItem = getPropertyItem(bean);
            HashMap<String, ArrayList<TableRow>> tableList = getTableFields(bean);
            ArrayList<FormElement> fieldList = fieldListFromItem(propertyItem);
            
            PdfPopulater pdf = new PdfPopulater(fileByteArray);
            pdf.fillFields(fieldList);
            
            pdf.fillTable("Firma_Logo_s1", tableList.get("logo"));
            pdf.fillTable("Firma_Logo_s2", tableList.get("logo"));
            
            pdf.fillTable("Prae_Med", tableList.get("premed"));
            pdf.fillTable("Post_Med", tableList.get("postmed"));
            pdf.fillTable("Infusionszeiten", tableList.get("times"));
            pdf.fillTable("vitalzeichen", tableList.get("vitals"));
            pdf.fillTable("Chargen", tableList.get("charges"));
            
            
            //check if filename exists and append counter until it doesn't
            String filename = generatePdfName(propertyItem);
//            String filename = "TEST_"+beanId;
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
    
    private void fillBeanList() {
        fillBeanList(null);
    }
    
    private void fillBeanList(ArrayList<Object> idList) {
        
        
        ArrayList<Object> pvdValues = new ArrayList<Object>();
        
        if(idList != null) {
            pvdValues = idList;
        }else {
            Collection values = (Collection) table.getValue();
            for (Object selectedValue : values) {
                pvdValues.add(table.getContainerProperty(selectedValue, tableIdCol).getValue());
            }
        }
        
        Container rows = getExportData(pvdValues);
        
        log.info("test result: {}", rows);
        
        // setup Bean List
        dataMap = new HashMap<Integer, PoPPdfPrint.DataBean>();
        for (Object iid : rows.getItemIds()) {
            DataBean bean = new DataBean(rows.getItem(iid));
            
            log.debug("adding bean with id {}", bean.getId());
            
            dataMap.put(bean.getId(), bean);
        }
        
        // add Meds
        Container meds = getMultiMeds(pvdValues);
        for (Object iid : meds.getItemIds()) {
            Item item = meds.getItem(iid);
            DataBean bean = dataMap.get(MyUtils.getValueFromItem(item, "ID_DATA", Integer.class));
            
            log.debug("adding meds to bean {}, {}", MyUtils.getValueFromItem(item, "ID_DATA", Integer.class), bean);
            
            if(bean != null) {
                bean.addMed(item);
            }else {
                log.warn("no DataBean found for med item {}", item);
            }
        }
        
        // add Times
        Container times = getMultiTimes(pvdValues);
        for (Object iid : times.getItemIds()) {
            Item item = times.getItem(iid);
            
            DataBean bean = dataMap.get(MyUtils.getValueFromItem(item, "ID_DATA", Integer.class));
            
            log.debug("adding times to bean {}, {}", MyUtils.getValueFromItem(item, "ID_DATA", Integer.class), bean);
            
            if(bean != null) {
                bean.addTimes(item);
            }else {
                log.warn("no DataBean found for times item {}", item);
            }
            
        }
        
        // add Vitals
        Container vitals = getMultiVitals(pvdValues);
        for (Object iid : vitals.getItemIds()) {
            Item item = vitals.getItem(iid);
            
            DataBean bean = dataMap.get(MyUtils.getValueFromItem(item, "ID_DATA", Integer.class));
            
            log.debug("adding vitals to bean {}, {}", MyUtils.getValueFromItem(item, "ID_DATA", Integer.class), bean);
            
            if(bean != null) {
                bean.addVitals(item);
            }else {
                log.warn("no DataBean found for vitals item {}", item);
            }
            
        }
     
        // add Charges
        Container charges = getCharges(pvdValues);
        for (Object iid : charges.getItemIds()) {
            Item item = charges.getItem(iid);
            
            DataBean bean = dataMap.get(MyUtils.getValueFromItem(item, "ID_DATA", Integer.class));
            
            log.debug("adding charge to bean {}, {}", MyUtils.getValueFromItem(item, "ID_DATA", Integer.class), bean);
            
            if(bean != null) {
                bean.addCharge(item);
            }else {
                log.warn("no DataBean found for charge item {}", item);
            }
            
        }
    }
    
    @SuppressWarnings("rawtypes")
    private PropertysetItem getPropertyItem(DataBean bean){
        
        ArrayList<FormElement> fieldList = new ArrayList<FormElement>();
        
        Item rowItem = bean.getDataItem();
        PropertysetItem item = new PropertysetItem();
        
        
        //item.getItemProperty("DATE").setValue();
        
        //System.out.println(rowItem.getItemProperty("ID"));
        
        for (Object pid : rowItem.getItemPropertyIds()) {
            
            item.addItemProperty(pid, rowItem.getItemProperty(pid));
            
        }
        
        // DATE
        if(rowItem.getItemProperty("DATE").getValue() != null){
            LocalDate date = new LocalDate(rowItem.getItemProperty("DATE").getValue());
            item.addItemProperty("INF_Date", new ObjectProperty<String>(fmt.print(date)));
        }
        
        // PSL_GEB_DATUM
        if(rowItem.getItemProperty("PSL_GEB_DATUM").getValue() != null){
            LocalDate psl_geb_datum = new LocalDate(rowItem.getItemProperty("PSL_GEB_DATUM").getValue());
            item.addItemProperty("Geb_Dat", new ObjectProperty<String>(fmt.print(psl_geb_datum)));
        }
        
        // NURSE
        if(rowItem.getItemProperty("NURSE").getValue() != null){
            item.addItemProperty("Nurse_Name_S1", new ObjectProperty<String>(MyUtils.getValueFromItem(rowItem, "NURSE", String.class)));
            item.addItemProperty("Nurse_Name_S2", new ObjectProperty<String>(MyUtils.getValueFromItem(rowItem, "NURSE", String.class)));
        }
        
        // INFUSIONSTYP
        if(rowItem.getItemProperty("INFUSIONSTYP").getValue() != null){
            item.addItemProperty("INF_Type", new ObjectProperty<String>(cust_protokoll_pop_typ.convertToPresentation(
                MyUtils.getValueFromItem(rowItem, "INFUSIONSTYP", Integer.class), String.class)));
        }
        
        // INFUSIONS_REAKTION
        
        item.removeItemProperty("INFUSION_REACTION_TODAY_INTENSITY");
        item.removeItemProperty("INFUSION_REACTION_RECENT_INTENSITY");
        item.removeItemProperty("INFUSIONS_REAKTION_VERG");
        item.removeItemProperty("INFUSIONS_REAKTION_HEUTE");
        
        Boolean infusions_reaktion_heute = MyUtils.getValueFromItem(rowItem, "INFUSIONS_REAKTION_HEUTE", Boolean.class);
        Boolean infusions_reaktion_verg = MyUtils.getValueFromItem(rowItem, "INFUSIONS_REAKTION_VERG", Boolean.class);

        item.addItemProperty("INFUSIONS_REAKTION_HEUTE_Ja", new ObjectProperty<Boolean>(infusions_reaktion_heute != null && infusions_reaktion_heute));
        item.addItemProperty("INFUSIONS_REAKTION_HEUTE_Nein", new ObjectProperty<Boolean>(infusions_reaktion_heute != null && !infusions_reaktion_heute));


        item.addItemProperty("INFUSION_REACTION_RECENT_INTENSITY_Ja", new ObjectProperty<Boolean>(infusions_reaktion_verg != null && infusions_reaktion_verg));
        item.addItemProperty("INFUSION_REACTION_RECENT_INTENSITY_Nein", new ObjectProperty<Boolean>(infusions_reaktion_verg != null && !infusions_reaktion_verg));
        
        
        
        //PC_INTENSITY_TODAY
        if(MyUtils.getValueFromItem(rowItem, "PC_INTENSITY_TODAY", String.class) != null){
            item.addItemProperty("INFUSION_REACTION_TODAY_INTENSITY", new ObjectProperty<String>(
                    cust_protokoll_complications.getLngText(MyUtils.getValueFromItem(rowItem, "PC_INTENSITY_TODAY", String.class))));
        }
        
        //PC_INTENSITY_RECENT
        if(MyUtils.getValueFromItem(rowItem, "PC_INTENSITY_RECENT", String.class) != null){
            item.addItemProperty("INFUSION_REACTION_RECENT_INTENSITY", new ObjectProperty<String>(
                    cust_protokoll_complications.getLngText(MyUtils.getValueFromItem(rowItem, "PC_INTENSITY_RECENT", String.class))));
        }

        //PVMS_PAT_INITIALS -> PAT_CODE
        if(rowItem.getItemProperty("PPOPMS_PAT_CODE").getValue() != null){
            item.addItemProperty("ID_Patient", new ObjectProperty<String>(MyUtils.getValueFromItem(rowItem, "PPOPMS_PAT_CODE", String.class)));
        }
        
        // Pat_Name
        if(includeName && rowItem.getItemProperty("PATNAME").getValue() != null){
            item.addItemProperty("Pat_Name", new ObjectProperty<String>(MyUtils.getValueFromItem(rowItem, "PATNAME", String.class)));
        }
        
        // NEEDLE
        item.removeItemProperty("PUNKT_NADEL");
        if(rowItem.getItemProperty("PNT_NAME").getValue() != null){
            item.addItemProperty("PUNKT_NADEL", new ObjectProperty<String>(MyUtils.getValueFromItem(rowItem, "PNT_NAME", String.class)));
        }
        
        // PUMP
        item.removeItemProperty("PUMP");
        if(rowItem.getItemProperty("PUMP").getValue() != null){
            item.addItemProperty("PUMP", new ObjectProperty<String>(MyUtils.getValueFromItem(rowItem, "rPUMP", String.class)));
        }
        
        item.removeItemProperty("PUMP_TUBE");
        if(rowItem.getItemProperty("PUMP_TUBE").getValue() != null){
            item.addItemProperty("PUMP_TUBE", new ObjectProperty<String>(MyUtils.getValueFromItem(rowItem, "rPUMP_TUBE", String.class)));
        }
        
        // SOLVENT
        item.removeItemProperty("MED_SOLVED_IN");
        if(rowItem.getItemProperty("MED_SOLVED_IN").getValue() != null){
            item.addItemProperty("MED_SOLVED_IN", new ObjectProperty<String>(
                    cust_protokoll_solvents.getLngText(MyUtils.getValueFromItem(rowItem, "rMED_SOLVED_IN", String.class))));
        }
        
        /*
         * special checks
         */
        
        // chk_Prae/Post_Med
        item.addItemProperty("chk_Prae_Med", new ObjectProperty<Boolean>((bean.getPreMed().size() == 0)));
        item.addItemProperty("chk_Post_Med", new ObjectProperty<Boolean>((bean.getPostMed().size() == 0)));

        // sum main med duration
        Long totalSeconds = 0L;
        for (Item timeItem : bean.getTimes()) {
            totalSeconds += MyUtils.getValueFromItem(timeItem, "duration", Long.class);
        }
        item.addItemProperty("Infusionsdauer_Haupt", new ObjectProperty<String>(tfu.print(totalSeconds*1000)));
        
//        for (Object pid : item.getItemPropertyIds()) {
//            log.info("{}, {}", pid, item.getItemProperty(pid).getValue());
//        }
        
        return item;
        
        
        
        
    }
    // cust_protokoll_solvents.getLngText(MyUtils.getValueFromItem(rowItem, "rMED_SOLVED_IN", String.class))));
    private HashMap<String, ArrayList<TableRow>> getTableFields(DataBean bean){
        
        HashMap<String, ArrayList<TableRow>> tables = new HashMap<String, ArrayList<TableRow>>();
        tables.put("logo", new ArrayList<TableRow>());
        tables.put("premed", new ArrayList<TableRow>());
        tables.put("postmed", new ArrayList<TableRow>());
        tables.put("times", new ArrayList<TableRow>());
        tables.put("vitals", new ArrayList<TableRow>());
        tables.put("charges", new ArrayList<TableRow>());
        
        //logo
        String ccode = MyUtils.getValueFromItem(bean.getDataItem(), "PPOPMS_KEY_FIRM", String.class);
        String imageName = "pop_logo_";
        if(ccode != null) {
            imageName += ccode;
        }
        
        TableRow logoRow = new TableRow(47f);
        RowCell logoCell = new RowCell(null, 1f);
        logoCell.setImage(imageName);
        logoCell.setBorder(false);
        logoCell.setCompact(true);
        logoRow.addEntry(logoCell);
        tables.get("logo").add(logoRow);
        
        // premed
        float premedNameWidth = 4f;
        float premedTimeWidth = 1f;
        float premedTypeWidth = 2f;
        
        TableRow medHeader = new TableRow(12f);
        medHeader.addEntry(formatHeader(new RowCell("Medikament", premedNameWidth)));
        medHeader.addEntry(formatHeader(new RowCell("Verabreichungsform", premedTypeWidth)));
        medHeader.addEntry(formatHeader(new RowCell("Uhrzeit", premedTimeWidth)));
        tables.get("premed").add(medHeader);

        for (Item preMedItem : bean.getPreMed()) {
            
            TableRow row = new TableRow(10f);
            row.addEntry(formatCell(new RowCell(MyUtils.getValueFromItem(preMedItem, "NAME", String.class), premedNameWidth)));
            row.addEntry(formatCell(new RowCell(cust_protokoll_pop_multi_meds_applications.getLngText(
                    MyUtils.getValueFromItem(preMedItem, "TYPE", String.class)), premedTypeWidth)));
            row.addEntry(formatCell(new RowCell(tfz.print(
                    MyUtils.getValueFromItem(preMedItem, "TIME", java.sql.Time.class).getTime()
                    ), premedTimeWidth)));
            tables.get("premed").add(row);
        }
        
        
        // postmed
        tables.get("postmed").add(medHeader);
        for (Item postMedItem : bean.getPostMed()) {
            
            TableRow row = new TableRow(10f);
            row.addEntry(formatCell(new RowCell(MyUtils.getValueFromItem(postMedItem, "NAME", String.class), premedNameWidth)));
            row.addEntry(formatCell(new RowCell(cust_protokoll_pop_multi_meds_applications.getLngText(
                    MyUtils.getValueFromItem(postMedItem, "TYPE", String.class)), premedTypeWidth)));
            row.addEntry(formatCell(new RowCell(tfz.print(
                    MyUtils.getValueFromItem(postMedItem, "TIME", java.sql.Time.class).getTime()
                    ), premedTimeWidth)));
            tables.get("postmed").add(row);
        }
        
        // times
        float timesTimeWidth = 1f;
        float timesRateWidth = 1f;
        float timesFreeWidth = 1f;
        
        TableRow timesHeader = new TableRow(14f);
        timesHeader.addEntry(formatHeader(new RowCell("Start", timesTimeWidth)));
        timesHeader.addEntry(formatHeader(new RowCell("Stop", timesTimeWidth)));
        timesHeader.addEntry(formatHeader(new RowCell("Rate in ml/h", timesRateWidth)));
        timesHeader.addEntry(formatHeader(new RowCell("frei laufend", timesFreeWidth)));
        tables.get("times").add(timesHeader);

        for (Item preMedItem : bean.getTimes()) {
            
            TableRow row = new TableRow(12f);
            row.addEntry(formatCell(new RowCell(tfz.print(
                    MyUtils.getValueFromItem(preMedItem, "START", java.sql.Time.class).getTime()
                    ), timesTimeWidth)));
            row.addEntry(formatCell(new RowCell(tfz.print(
                    MyUtils.getValueFromItem(preMedItem, "STOP", java.sql.Time.class).getTime()
                    ), timesTimeWidth)));
            row.addEntry(formatCell(new RowCell(Float.toString(MyUtils.getValueFromItem(preMedItem, "RATE", Float.class)), timesRateWidth)));
            row.addEntry(formatCell(new RowCell(booleanConverter.convertToPresentation(
                    MyUtils.getValueFromItem(preMedItem, "FREE", Boolean.class), String.class, null), timesFreeWidth)));
            tables.get("times").add(row);
        }
        
        //vitals
        float vitalsTimeWidth = 1f;
        float vitalsPressureWidth = 2f;
        float vitalsOxiWidth = 2f;
        float vitalsPulseWidth = 1f;
        float vitalsTempWidth = 1.5f;
        float vitalsCommentWidth = 4f;
        
        TableRow vitalsHeader = new TableRow(14f);
        vitalsHeader.addEntry(formatHeader(new RowCell("Uhrzeit", vitalsTimeWidth)));
        vitalsHeader.addEntry(formatHeader(new RowCell("Blutdruck (mm Hg)", vitalsPressureWidth)));
        vitalsHeader.addEntry(formatHeader(new RowCell("Pulsoximeter (SpO2)", vitalsOxiWidth)));
        vitalsHeader.addEntry(formatHeader(new RowCell("Puls", vitalsPulseWidth)));
        vitalsHeader.addEntry(formatHeader(new RowCell("Temperatur", vitalsTempWidth)));
        vitalsHeader.addEntry(formatHeader(new RowCell("Kommentar", vitalsCommentWidth)));
        tables.get("vitals").add(vitalsHeader);
        
        for (Item vitalsItem : bean.getVitals()) {
            
            TableRow row = new TableRow(12f);
            row.addEntry(formatCell(new RowCell(tfz.print(
                    MyUtils.getValueFromItem(vitalsItem, "TIME", java.sql.Time.class).getTime()
                    ) +
                    (MyUtils.getValueFromItem(vitalsItem, "FLAG_IS_MAX_REACT", Boolean.class) ? " *" : "")
                    , vitalsTimeWidth)));
            row.addEntry(formatCell(new RowCell(MyUtils.getValueFromItem(vitalsItem, "PRESSURE", String.class), vitalsPressureWidth)));
            row.addEntry(formatCell(new RowCell(Float.toString(MyUtils.getValueFromItem(vitalsItem, "OXI", Float.class)), vitalsOxiWidth)));
            row.addEntry(formatCell(new RowCell(Integer.toString(MyUtils.getValueFromItem(vitalsItem, "PULSE", Integer.class)), vitalsPulseWidth)));
            row.addEntry(formatCell(new RowCell(Float.toString(MyUtils.getValueFromItem(vitalsItem, "TEMP", Float.class)), vitalsTempWidth)));
            row.addEntry(formatCell(new RowCell(MyUtils.getValueFromItem(vitalsItem, "COMMENT", String.class), vitalsCommentWidth)));
            
            tables.get("vitals").add(row);
        }
        
        // charges
        float chargesCountWidth = 1f;
        float chargesNumberWidth = 4f;
        
        TableRow chargesHeader = new TableRow(14f);
        chargesHeader.addEntry(formatHeader(new RowCell("Anzahl", chargesCountWidth)));
        chargesHeader.addEntry(formatHeader(new RowCell("Chargen-Nummer", chargesNumberWidth)));
        tables.get("charges").add(chargesHeader);
        
        for (Item chargesItem : bean.getCharges()) {
            
            TableRow row = new TableRow(12f);
            row.addEntry(formatCell(new RowCell(Integer.toString(MyUtils.getValueFromItem(chargesItem, "COUNT", Integer.class)), chargesCountWidth)));
            row.addEntry(formatCell(new RowCell(MyUtils.getValueFromItem(chargesItem, "NR", String.class), chargesNumberWidth)));
            tables.get("charges").add(row);
        }
        
        return tables;
        
    }
    
    private RowCell formatCell(RowCell cell) {
        
        cell.setFontSize(8);
        cell.setBorder(false);
        cell.setCompact(true);
        
        return cell;
     }
    
    private RowCell formatHeader(RowCell cell) {

       cell.setFontSize(8);
       cell.setFont(PDType1Font.HELVETICA_BOLD);
       cell.setBorder(false);
       cell.setCompact(true);

       return cell;
    }
    
    private ArrayList<FormElement> fieldListFromItem(Item item){
        ArrayList<FormElement> fieldList = new ArrayList<FormElement>();
        
        for (Object pid : item.getItemPropertyIds()) {
            fieldList.add(new FormElement((String) pid, item.getItemProperty(pid).getValue()));
        }
        
        return fieldList;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    private void lngFill() {
        cust_protokoll_solvents = new I18nConverter("cust_protokoll_solvents", "TYPE");
        cust_protokoll_complications = new I18nConverter("cust_protokoll_complications", "PC_INTENSITY");
        cust_protokoll_pop_multi_meds_applications = new I18nConverter("cust_protokoll_pop_multi_meds_applications", "TYPE");
        cust_protokoll_pop_typ = new I18nConverter("cust_protokoll_pop_typ", "ID", "STRING");
        booleanConverter = new BooleanToTextConverter();
    }
    
    
    

    
    private String generatePdfName(PropertysetItem item){
        
        StringBuffer str = new StringBuffer();
        
        DateTimeFormatter fmt = DateTimeFormat.forPattern("YYYY-MM-dd");
        DateTimeFormatter fmtp = DateTimeFormat.forStyle("M-");
        LocalDate ldt = new LocalDate();
        LocalDate ldtp = new LocalDate();
        if(item.getItemProperty("DATE").getValue() != null){
            ldtp = new LocalDate(MyUtils.getValueFromItem(item, "DATE", java.sql.Timestamp.class).getTime());
        }
        
        
        str.append("POP-");
        str.append(fmt.print(ldt));
        str.append("-[");
        str.append(item.getItemProperty("beanId").getValue());
        str.append("]");
        
        
        String code = MyUtils.getValueFromItem(item, "ID_Patient", String.class);
        if(code != null && !code.equals("") ){
            str.append("-[");
            str.append(code);
            str.append("]");
        }
        
        if(item.getItemProperty("DATE").getValue() != null && ldtp.getYear() > 1900){
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
        
        String sql = "SELECT *"
         + "\n " + ", cust_protokoll_pop_daten.ID as beanId"
         + "\n " + ", CONCAT(PSL_TITEL,' ',PSA_NAME,' ',PSL_NAME,', ',PSL_VORNAME) as PATNAME" // pump has a NAME field
         + "\n " + ", CONCAT(AUL_VORNAME,' ',AUL_NAME) as NURSE"
         + "\n " + ", reaction_today.PC_INTENSITY as PC_INTENSITY_TODAY"
         + "\n " + ", reaction_recent.PC_INTENSITY as PC_INTENSITY_RECENT"
         + "\n " + "-- remappings"
         + "\n " + ", pump.NAME as rPUMP"
         + "\n " + ", tube.VALUE as rPUMP_TUBE"
         + "\n " + ", solvent.TYPE as rMED_SOLVED_IN"
         + "\n "
         + "\n " + "FROM"
         + "\n "
         + "\n " + "    cust_protokoll_pop_daten"
         + "\n " + "    LEFT JOIN cust_patient_stammdaten_liste ON ID_PATIENT = PSL_ID"
         + "\n " + "    LEFT JOIN cust_patient_stammdaten_anrede ON PSL_ANREDE = PSA_ID"
         + "\n " + "    LEFT JOIN cust_protokoll_pop_meta_dynamic ON ID_DELEGATION = PPOPMD_ID"
         + "\n " + "    LEFT JOIN cust_verk_haupt ON PPOPMD_VH_ID = VH_ID"
         + "\n " + "    LEFT JOIN cust_protokoll_pop_meta_static ON PPOPMD_VH_ID = PPOPMS_VH_ID"
         + "\n " + "    -- nurse"
         + "\n " + "    LEFT JOIN cust_krankenschwester_stammdaten_ks ON ID_NURSE = KSK_ID"
         + "\n " + "    LEFT JOIN cms_auth_stammdaten_user as aul_nurse ON aul_nurse.AUL_ID = KSK_U_ID"
         + "\n " + "    -- product"
         + "\n " + "    LEFT JOIN cust_prescriptions_products ON VH_PROJEKT_ID = PP_ID_PROJECT"
         + "\n " + "    -- needle"
         + "\n " + "    LEFT JOIN cust_protokoll_nadeltypen ON PUNKT_NADEL = PNT_ID"
         + "\n " + "    -- intensities"
         + "\n " + "    LEFT JOIN cust_protokoll_complications as reaction_today ON reaction_today.PC_ID = INFUSION_REACTION_TODAY_INTENSITY"
         + "\n " + "    LEFT JOIN cust_protokoll_complications as reaction_recent ON reaction_recent.PC_ID = INFUSION_REACTION_RECENT_INTENSITY"
         + "\n " + "    -- pumps"
         + "\n " + "    LEFT JOIN cust_protokoll_pumps as pump ON PUMP = pump.ID"
         + "\n " + "    LEFT JOIN cust_protokoll_pop_pump_tubes as tube ON PUMP_TUBE = tube.ID"
         + "\n " + "    -- solvent"
         + "\n " + "    LEFT JOIN cust_protokoll_solvents as solvent ON MED_SOLVED_IN = solvent.ID"
         + "\n " 
         + "\n " + "WHERE"
         + "\n " + "    cust_protokoll_pop_daten.ID IN ("+Joiner.on(",").join(values)+")"
         + "\n "
         + "\n " + "GROUP BY"
         + "\n " + "    cust_protokoll_pop_daten.ID";

        //db.DB_Data_Get.DB_DEBUG_SQL_STRING();
        
        db.CustomQuery.setSqlString(sql);
        
//        db.debugNextQuery(true);
        
        return db.CustomQuery.query();
        

    }
    
//    SELECT *
//    FROM cust_protokoll_pop_multi_meds
//    LEFT JOIN cust_protokoll_pop_multi_meds_applications on ADM_TYPE = cust_protokoll_pop_multi_meds_applications.ID
//    ORDER BY ID_DATA ASC, TIME ASC, cust_protokoll_pop_multi_meds.ID ASC
//    -- where ID_DATA IN (62,82);
    
    private Container getMultiMeds(Collection values) {
        
        DBClass db = new DBClass();
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("*");
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Verknüpfung("cust_protokoll_pop_multi_meds", "LEFT JOIN", "cust_protokoll_pop_multi_meds_applications", "ADM_TYPE", "ID");
        
        db.DB_Data_Get.DB_Filter.DB_WHERE_In("cust_protokoll_pop_multi_meds", "ID_DATA", Joiner.on(",").join(values), "");
        
        db.DB_Data_Get.DB_Ordnen.DB_Ordnen("ID_DATA", "ASC");
        db.DB_Data_Get.DB_Ordnen.DB_Ordnen("TIME", "ASC");
        db.DB_Data_Get.DB_Ordnen.DB_Ordnen("cust_protokoll_pop_multi_meds.ID", "ASC");
        
//        db.debugNextQuery(true);
        
        return db.DB_Data_Get.DB_SEND_AND_GET_Container();
        
    }
    
    private Container getMultiTimes(Collection values) {
        
        DBClass db = new DBClass();
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("*");
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("TIME_TO_SEC(SUBTIME(`STOP`, `START`)) as duration");
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Einzeln("cust_protokoll_pop_multi_infusion_times");
        db.DB_Data_Get.DB_Filter.DB_WHERE_In("cust_protokoll_pop_multi_infusion_times", "ID_DATA", Joiner.on(",").join(values), "");
        
        db.DB_Data_Get.DB_Ordnen.DB_Ordnen("ID_DATA", "ASC");
        db.DB_Data_Get.DB_Ordnen.DB_Ordnen("START", "ASC");
        db.DB_Data_Get.DB_Ordnen.DB_Ordnen("ID", "ASC");
        
//        db.debugNextQuery(true);
        
        return db.DB_Data_Get.DB_SEND_AND_GET_Container();
        
    }
    
    private Container getMultiVitals(Collection values) {
        
        DBClass db = new DBClass();
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("*");
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Einzeln("cust_protokoll_pop_multi_vitals");
        db.DB_Data_Get.DB_Filter.DB_WHERE_In("cust_protokoll_pop_multi_vitals", "ID_DATA", Joiner.on(",").join(values), "");
        
        db.DB_Data_Get.DB_Ordnen.DB_Ordnen("ID_DATA", "ASC");
        db.DB_Data_Get.DB_Ordnen.DB_Ordnen("TIME", "ASC");
        db.DB_Data_Get.DB_Ordnen.DB_Ordnen("ID", "ASC");
        
//        db.debugNextQuery(true);
        
        return db.DB_Data_Get.DB_SEND_AND_GET_Container();
        
    }
    
    private Container getCharges(Collection values) {
        
        DBClass db = new DBClass();
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("*");
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Einzeln("cust_protokoll_pop_chargen");
        db.DB_Data_Get.DB_Filter.DB_WHERE_In("cust_protokoll_pop_chargen", "ID_DATA", Joiner.on(",").join(values), "");
        
        db.DB_Data_Get.DB_Ordnen.DB_Ordnen("ID_DATA", "ASC");
        db.DB_Data_Get.DB_Ordnen.DB_Ordnen("ID", "ASC");
        
//        db.debugNextQuery(true);
        
        return db.DB_Data_Get.DB_SEND_AND_GET_Container();
        
    }
    
    
    private class DataBean {
        
        private Integer id;
        
        private Item dataItem;
        private ArrayList<Item> preMed;
        private ArrayList<Item> postMed;
        private ArrayList<Item> times;
        private ArrayList<Item> vitals;
        private ArrayList<Item> charges;
        
        
        public DataBean(Item item) {
            setDataItem(item);
            setId(MyUtils.getValueFromItem(item, "beanId", Integer.class));
            
            preMed = new ArrayList<Item>();
            postMed = new ArrayList<Item>();
            times = new ArrayList<Item>();
            vitals = new ArrayList<Item>();
            charges = new ArrayList<Item>();
            
        }


        public Integer getId() {
            return id;
        }


        public void setId(Integer id) {
            this.id = id;
        }


        public Item getDataItem() {
            return dataItem;
        }


        public void setDataItem(Item dataItem) {
            this.dataItem = dataItem;
        }


        public ArrayList<Item> getPreMed() {
            return preMed;
        }


        public ArrayList<Item> getPostMed() {
            return postMed;
        }


        public ArrayList<Item> getTimes() {
            return times;
        }


        public ArrayList<Item> getVitals() {
            return vitals;
        }

        public ArrayList<Item> getCharges() {
            return charges;
        }

        public void setPreMed(ArrayList<Item> preMed) {
            this.preMed = preMed;
        }


        public void setPostMed(ArrayList<Item> postMed) {
            this.postMed = postMed;
        }


        public void setTimes(ArrayList<Item> times) {
            this.times = times;
        }


        public void setVitals(ArrayList<Item> vitals) {
            this.vitals = vitals;
        }

        public void setCharges(ArrayList<Item> charges) {
            this.charges = charges;
        }
        
        // add
        
        public void addMed(Item medItem) {
            
            String flag = MyUtils.getValueFromItem(medItem, "FLAG_TYPE", String.class);
            
            if(flag.equals("type_pre")) {
                preMed.add(medItem);
            }
            
            if(flag.equals("type_post")) {
                postMed.add(medItem);
            }
            
        }


        public void addTimes(Item timesItem) {
            this.times.add(timesItem);
        }


        public void addVitals(Item vitalsItem) {
            this.vitals.add(vitalsItem);
        }
        
        public void addCharge(Item chargeItem) {
            this.charges.add(chargeItem);
        }
        
        
    }
    
}
