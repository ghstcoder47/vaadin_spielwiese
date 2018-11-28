package archenoah.web.normal.main.Menuepunkte.Individuelle.Ordermanager.OpenTickets.classes;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.joda.time.LocalDate;

import archenoah.config.CMS_Config_Std;
import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.pdf.OnDemandFileDownloader;
import archenoah.lib.tool.pdf.OnDemandFileDownloader.OnDemandStreamResource;
import archenoah.lib.tool.pdf.PdfPopulater;
import archenoah.lib.tool.pdf.PdfPopulater.FormElement;
import archenoah.lib.tool.pdf.PdfPopulater.RowCell;
import archenoah.lib.tool.pdf.PdfPopulater.TableParameters;
import archenoah.lib.tool.pdf.PdfPopulater.TableRow;
import be.quodlibet.boxable.HorizontalAlignment;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.ui.AbstractComponent;

public class TicketContentsPdf {

    // {section fields}
    // ****************
    private org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
    private CMS_Config_Std config = CMS_Config_Std.getInstance();
    
    private Integer ticketId = 0;
    
    private Item ticketData;
    private Container ticketContents;
    private HashMap<Integer, ArrayList<Item>> ticketContentsByPatient;
    private Container ticketSums;
    
    private enum PTYPE{
        PATIENT,
        APO
    }
    
    // {end fields}

    

    // {section constructors}
    // **********************
    public TicketContentsPdf() {
        configureComponents();
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    public Integer getTicketId() {
        return this.ticketId;
    }
    public Boolean setTicketId(Integer ticketId) {
        this.ticketId = ticketId != null ? ticketId : 0;
        return getTicket();
    }
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    /**
     * should not need be used unless ticketContents have changed after setting id
     */
    public boolean getTicket() {
        ticketData = dbGetTicketData();
        ticketContents = dbGetTicketContents();
        ticketContentsByPatient = sortPatientContents();
        ticketSums = dbGetTicketSums();
        return checkData();
    }
    
    public void attach(AbstractComponent component){
        
        new OnDemandFileDownloader(new OnDemandStreamResource() {

            @Override
            public String getFilename() {
                return "TICKET-"+ticketId+".zip";
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
    
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    
    private void configureComponents() {
        
        
    }
    
    private HashMap<Integer, ArrayList<Item>> sortPatientContents(){
        
        if(ticketContents == null || ticketContents.size() == 0) {
            return null;
        }
        
        HashMap<Integer, ArrayList<Item>> map = new HashMap<Integer, ArrayList<Item>>();
        
        
        for (Object iid : ticketContents.getItemIds()) {
            
            Item item = ticketContents.getItem(iid);
            
            Integer patientId = MyUtils.getValueFromItem(item, "PSL_ID", Integer.class);
            
            if(map.get(patientId) == null) {
                map.put(patientId, new ArrayList<Item>());
            }
            
            map.get(patientId).add(item);
            
        }
        
        return map;
        
    }
    
    private Boolean checkData() {
        
        Boolean ok = true;
        
        if(ticketData == null) {
            ok = false;
            log.warn("ticketData is null!");
        }
        
        if(ticketContents == null || ticketContents.size() == 0) {
            ok = false;
            log.warn("no ticketContents found!");
        }
        
        return ok;
    }
    
    private Object getRawItemValue(Item item, String propertyId) {
        Object res = null;
        
        if(item.getItemProperty(propertyId) != null) {
            res = item.getItemProperty(propertyId).getValue();
        }
        
        return res;
    }
    
    private String getItemValue(Item item, String propertyId) {
        String value = "";

        if (item.getItemProperty(propertyId) != null) {
            if (item.getItemProperty(propertyId).getValue() != null) {

                value = item.getItemProperty(propertyId).getValue().toString();

            } else {
                value = null;
            }
        }

        return value;
    }
    
    private String catchNull(String value) {
        return value != null ? value : "";
    }
    
    private byte[] generateOutput() {
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(os, Charset.forName("UTF-8"));
        
        try {
            zos.putNextEntry(new ZipEntry(generateNurseTicketName()));
            zos.write(generateNurseTicket());
            zos.closeEntry();
            
            zos.putNextEntry(new ZipEntry(generateLabelsName()));
            zos.write(generateLabels());
            zos.closeEntry();
            
            for (Entry<Integer, ArrayList<Item>> entry : ticketContentsByPatient.entrySet()) {
                
                try {
                    
                    HashMap<PTYPE, byte[]> pdfs = generatePatientTicket(entry.getValue());
                    
                    if(pdfs.containsKey(PTYPE.PATIENT)) {
                        zos.putNextEntry(new ZipEntry(generatePatientTicketName(entry.getValue())));
                        zos.write(pdfs.get(PTYPE.PATIENT));
                        zos.closeEntry();
                    }
                    
                    if(pdfs.containsKey(PTYPE.APO)) {
                        zos.putNextEntry(new ZipEntry(generatePatientApoTicketName(entry.getValue())));
                        zos.write(pdfs.get(PTYPE.APO));
                        zos.closeEntry();
                    }
                    
                    
                } catch (IOException e) {
                    log.error("IOException while generating Patient Output");
                    continue;
                }
                
            }
            
            
            zos.finish();
            zos.close();
            
        } catch (IOException e) {
            log.error("IOException while generating Output");
        }
            
        return os.toByteArray();
    }
    
    private String generateNurseTicketName() {
        return "TICKET-" +ticketId+"-NURSE.pdf";
    }
    
    private String generateLabelsName() {
        return "TICKET-" +ticketId+"-LABELS.pdf";
    }
    
    private String generatePatientTicketName(ArrayList<Item> ticketContentItemList) {
        return generatePatientTicketName(ticketContentItemList, false);
    }
    
    private String generatePatientApoTicketName(ArrayList<Item> ticketContentItemList) {
        return generatePatientTicketName(ticketContentItemList, true);
    }
    
    private String generatePatientTicketName(ArrayList<Item> ticketContentItemList, Boolean apo) {
        
        if(ticketContentItemList == null || ticketContentItemList.size() == 0) {
            return "";
        }
        
        Item firstContentItem = ticketContentItemList.get(0);
        
        return "TICKET-" +ticketId+ "-" + MyUtils.getValueFromItem(firstContentItem, "patient", String.class)
                + (apo ? "-APO" : "") + ".pdf";
    }
    
    private byte[] generateLabels() {
  
        
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        final ByteArrayOutputStream res = new ByteArrayOutputStream();
        
        PDFMergerUtility merger = new PDFMergerUtility();
        merger.setDestinationStream(res);
        
        // create empty template pdf
        PDRectangle rec = new PDRectangle(159, 88);
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(rec);
        document.addPage(page);
        try {
            document.save(os);
        } catch (IOException e) {
            log.error("error saving label pdf, {}", e);
        }
        try {
            document.close();
        } catch (IOException e) {
            log.error("error closing label pdf, {}", e);
        }
        
        // params
        TableParameters headerParams = new TableParameters();
        headerParams.marginBottom = 0.50f; // (15+20 / 88)
        
        TableParameters bodyParams = new TableParameters();
        bodyParams.marginTop = 0.18f; // (15 / 88) + 0.1
        bodyParams.marginTopFirstPage = 0.18f;
        
        // templates
        RowCell base = new RowCell();
        base.setFontSize(11);
        base.setBorder(false);
        base.setCompact(true);
        
        RowCell headerTpl = new RowCell().with(base);
        headerTpl.setFontSize(8);
        headerTpl.setFont(PDType1Font.HELVETICA_BOLD);
//        headerTpl.setBackground(Color.BLUE);
        
        RowCell dateTpl = new RowCell().with(base);
        dateTpl.setAlign(HorizontalAlignment.RIGHT);
//        dateTpl.setBackground(Color.GREEN);
        
        RowCell addrTpl = new RowCell().with(base);
//        addrTpl.setBackground(Color.ORANGE);
        addrTpl.setFontSize(10);
        addrTpl.setSpacing(1.2f);
        
        RowCell productTpl = new RowCell().with(base);
        productTpl.setFont(PDType1Font.HELVETICA_BOLD);
//        productTpl.setBackground(Color.RED);
        
        // constant header
        final TableRow header = new TableRow(15f);
        header.addEntry(new RowCell("Fachapotheke für seltene Krankheiten", 1f).with(headerTpl));
        
        Container entries = dbGetTicketLabelContents(); 
        
        for (Object iid : entries.getItemIds()) {
            
            Item item = entries.getItem(iid);
            
            PdfPopulater labels = new PdfPopulater(os.toByteArray());
            ArrayList<TableRow> labelTables = new ArrayList<TableRow>();
            ArrayList<TableRow> labelBody= new ArrayList<TableRow>();

            labelTables.add(header);
            
            TableRow date = new TableRow(15f);
            date.addEntry(new RowCell(MyUtils.getValueFromItem(item, "dates", String.class), 1f).with(dateTpl));
            labelTables.add(date);
            
            String address = MyUtils.getValueFromItem(item, "patient", String.class);
            address += "\n" + MyUtils.getValueFromItem(item, "patient_street", String.class);
            address += "\n" + MyUtils.getValueFromItem(item, "key_country", String.class);
            address +=  " " + MyUtils.getValueFromItem(item, "patient_city", String.class);
            
            String contents = MyUtils.BigDecimalToInt(MyUtils.getValueFromItem(item, "quantity", BigDecimal.class)) + " ";
            contents += MyUtils.getValueFromItem(item, "product", String.class);
            
            TableRow addr = new TableRow(47f);
            addr.addEntry(new RowCell(address, 1f).with(addrTpl));
            labelBody.add(addr);
            
            TableRow cont = new TableRow(20f);
            cont.addEntry(new RowCell(contents, 1f).with(productTpl));
            labelBody.add(cont);
            
            labels.fillTable(labelTables, headerParams);
            labels.fillTable(labelBody,bodyParams);
            
            merger.addSource(new ByteArrayInputStream(labels.finish().toByteArray()));
        }
        
        try {
            merger.mergeDocuments(MemoryUsageSetting.setupTempFileOnly());
        } catch (IOException e) {
            // TASK Auto-generated catch block
            e.printStackTrace();
        }
        
        return res.toByteArray();
    }
    
    private byte[] generateNurseTicket() { //TASK: multifile / zip
        
        String pdf_key_nurse = "ticket_nurse";
        
        String countryKey = getItemValue(ticketData, "key_country");
        if(countryKey != null) {
            pdf_key_nurse += "_" + countryKey;
        }else {
            pdf_key_nurse += "_DE"; // TASK add messagebox country select
        }
        
        PdfPopulater pdfNurse;
        try {
            pdfNurse = new PdfPopulater(pdf_key_nurse);
        } catch (IllegalArgumentException e) {
            log.error("not pdf blob found!");
            return new byte[0];
        }
        
        ArrayList<FormElement> fieldsNurse = new ArrayList<FormElement>();
        ArrayList<TableRow> tableNurseContents = new ArrayList<TableRow>();
        ArrayList<TableRow> tableNurseSums = new ArrayList<TableRow>();
        
       
        // Default fields
        
        String address = "";
        
        if(getItemValue(ticketData, "location_id") == null) {
            address += catchNull(getItemValue(ticketData, "nurse_name")) + "\n";
            address += catchNull(getItemValue(ticketData, "nurse_street")) + "\n";
            address += catchNull(getItemValue(ticketData, "nurse_city")) + "\n";
        }else {
            address += catchNull(getItemValue(ticketData, "location_name")) + "\n";
            String ext = catchNull(getItemValue(ticketData, "location_extended"));
            if(ext != null && !ext.equals("")) {
                address += ext + "\n";
            }
            address += catchNull(getItemValue(ticketData, "location_street")) + "\n";
            address += catchNull(getItemValue(ticketData, "location_city")) + "\n";
        }
        
        LocalDate dateDelivery = null;
        String dateShipping = ""; 
        try {
            
            dateDelivery = new LocalDate(getRawItemValue(ticketData, "date_delivery"));
            dateShipping = dateDelivery.minusDays(1).toString("dd.MM.yyyy");
        
        } catch (Exception e) {
            // TASK: handle exception
        }
        
        fieldsNurse.add(new FormElement("PrintDate", new LocalDate().toString("dd.MM.yyyy")));
        fieldsNurse.add(new FormElement("Lieferadresse", address));
        fieldsNurse.add(new FormElement("Nurse_Name", getItemValue(ticketData, "nurse_name")));
        fieldsNurse.add(new FormElement("Lieferant", getItemValue(ticketData, "shipping_company")));
        fieldsNurse.add(new FormElement("Versanddatum", dateShipping));
        
        fieldsNurse.add(new FormElement("OMT_ID", getItemValue(ticketData, "OMT_ID")));
        
        // Contents Table
        
        float widthPatient = 4f;
        float widthProduct = 2f;
        float widthDates = 2f;
        float widthQuantity = 1f;
        float widthBalance = 1f;
        
        TableRow contentsHeader = new TableRow(15f);
        contentsHeader.addEntry(formatHeader(new RowCell("Patient", widthPatient)));
        contentsHeader.addEntry(formatHeader(new RowCell("Artikel", widthProduct)));
        contentsHeader.addEntry(formatHeader(new RowCell("Daten", widthDates)));
        contentsHeader.addEntry(formatHeader(new RowCell("RX", widthBalance)));
        contentsHeader.addEntry(formatHeader(new RowCell("Menge", widthQuantity)));

        tableNurseContents.add(contentsHeader);

        Boolean preorder = false; 
        for (Object iid : ticketContents.getItemIds()) {
            
            Item item = ticketContents.getItem(iid);
            
            TableRow row = new TableRow(8f);
            
            row.addEntry(formatCell(new RowCell(getItemValue(item, "patient"), widthPatient)));
            row.addEntry(formatCell(new RowCell(getItemValue(item, "product"), widthProduct)));
            row.addEntry(formatCell(new RowCell(getItemValue(item, "dates"), widthDates)));
            
            RowCell cellQuantity = formatCell(new RowCell(getItemValue(item, "balance"), widthQuantity));
            Integer balance = MyUtils.BigDecimalToInt((BigDecimal) getRawItemValue(item, "balance"));
            Integer quantity = MyUtils.BigDecimalToInt((BigDecimal) getRawItemValue(item, "quantity"));
            if(balance == null
                || (quantity != null
                && balance - quantity < 0)
            ) {
                cellQuantity.setBackground(Color.RED);
                preorder = true;
            }
            row.addEntry(cellQuantity);
            
            row.addEntry(formatCell(new RowCell(getItemValue(item, "quantity"), widthBalance)));
            
            tableNurseContents.add(row);
        }
        fieldsNurse.add(new FormElement("Vorbezug", preorder));
        
        // Counts Table
        
        for (Object iid : ticketSums.getItemIds()) {
            
            Item item = ticketSums.getItem(iid);
            
            TableRow row = new TableRow(8f);
            row.addEntry(formatCell(new RowCell(getItemValue(item, "product"), 5f)));
            row.addEntry(formatCell(new RowCell(getItemValue(item, "sum"), 1f)));
            
            tableNurseSums.add(row);
            
        }
        
        pdfNurse.fillFields(fieldsNurse);
        
        pdfNurse.fillTable("Lieferliste", tableNurseContents);
        pdfNurse.fillTable("Gesamtmengen", tableNurseSums);
        
        return pdfNurse.finish().toByteArray();

        
        
    }
    
    private HashMap<PTYPE, byte[]> generatePatientTicket(ArrayList<Item> ticketContentItemList) {
        
        HashMap<PTYPE, byte[]> pdfs = new HashMap<PTYPE, byte[]>();
        
        String pdf_key_patient = "ticket_patient";
        String pdf_key_apo = "ticket_patient_apo";
        
        if(ticketContentItemList == null || ticketContentItemList.size() == 0) {
            return pdfs;
        }
        
        Item firstContentItem = ticketContentItemList.get(0);
        
        String countryKey = getItemValue(firstContentItem, "key_country");
        if(countryKey != null) {
            pdf_key_patient += "_" + countryKey;
            pdf_key_apo += "_" + countryKey;
        }else {
            pdf_key_patient += "_DE"; // TASK add messagebox country select
            pdf_key_apo += "_DE"; 
        }
        
        ArrayList<FormElement> fieldsPatient= new ArrayList<FormElement>();
        ArrayList<TableRow> tablePatientContents = new ArrayList<TableRow>();
        
        // Default fields
        
        String address = "";
        
        address += catchNull(getItemValue(firstContentItem, "patient")) + "\n";
        address += catchNull(getItemValue(firstContentItem, "patient_street")) + "\n";
        address += catchNull(getItemValue(firstContentItem, "patient_city")) + "\n";

        
        LocalDate dateDelivery = null;
        String dateShipping = ""; 
        try {
            
            dateDelivery = new LocalDate(getRawItemValue(ticketData, "date_delivery"));
            dateShipping = dateDelivery.minusDays(1).toString("dd.MM.yyyy");
        
        } catch (Exception e) {
            // TASK: handle exception
        }
        
        FormElement patientAddress = new FormElement("Lieferadresse", address);
        
        fieldsPatient.add(new FormElement("PrintDate", new LocalDate().toString("dd.MM.yyyy")));
        fieldsPatient.add(patientAddress);
        fieldsPatient.add(new FormElement("Lieferant", getItemValue(ticketData, "shipping_company")));
        fieldsPatient.add(new FormElement("Versanddatum", dateShipping));
        fieldsPatient.add(new FormElement("Nurse_Name", getItemValue(ticketData, "nurse_name")));
        fieldsPatient.add(new FormElement("OMT_ID", getItemValue(ticketData, "OMT_ID")));
        
        // Contents Table
        
        float widthPatient = 4f;
        float widthBirthdate = 2f;
        float widthProduct = 2f;
        float widthQuantity = 1f;
        
        TableRow contentsHeader = new TableRow(15f);
        contentsHeader.addEntry(formatHeader(new RowCell("Patient", widthPatient)));
        contentsHeader.addEntry(formatHeader(new RowCell("Geburtsdatum", widthBirthdate)));
        contentsHeader.addEntry(formatHeader(new RowCell("Artikel", widthProduct)));
        contentsHeader.addEntry(formatHeader(new RowCell("Menge", widthQuantity)));

        tablePatientContents.add(contentsHeader);
        
        Boolean preorder = false;
        for (Item item : ticketContentItemList) {
            TableRow row = new TableRow(8f);
            
            row.addEntry(formatCell(new RowCell(getItemValue(item, "patient"), widthPatient)));
            row.addEntry(formatCell(new RowCell(new LocalDate(getRawItemValue(item, "patient_birthdate")).toString("dd.MM.yyyy"), widthBirthdate)));
            row.addEntry(formatCell(new RowCell(getItemValue(item, "product"), widthProduct)));
//            row.addEntry(formatCell(new RowCell(getItemValue(item, "quantity"), widthQuantity)));
            
            RowCell cellQuantity = formatCell(new RowCell(getItemValue(item, "quantity"), widthQuantity));
            Integer balance = MyUtils.BigDecimalToInt((BigDecimal) getRawItemValue(item, "balance"));
            Integer quantity = MyUtils.BigDecimalToInt((BigDecimal) getRawItemValue(item, "quantity"));
            if(balance != null
                    && quantity != null
                    && balance - quantity < 0) {
                cellQuantity.setBackground(Color.RED);
                preorder = true;
            }
            row.addEntry(cellQuantity);

            tablePatientContents.add(row);
        }
        fieldsPatient.add(new FormElement("Vorbezug", preorder));
        
        
        
        
        
        try {
            PdfPopulater pdfPatient = null;
            pdfPatient = new PdfPopulater(pdf_key_patient);
            if (pdfPatient != null) {
                pdfPatient.fillFields(fieldsPatient);
                pdfPatient.fillTable("Lieferliste", tablePatientContents);
                pdfs.put(PTYPE.PATIENT, pdfPatient.finish().toByteArray());
            }
        } catch (IllegalArgumentException e) {
            log.error("not patient pdf blob found!");
        }
        
        
        
        // hacky override of patient address for APO pdf
        if(getItemValue(ticketData, "location_id") != null
            && "PATIENT".equals(getItemValue(ticketData, "type"))) {
            
            String location = "";
            
            location += catchNull(getItemValue(ticketData, "location_name")) + "\n";
            String ext = catchNull(getItemValue(ticketData, "location_extended"));
            if(ext != null && !ext.equals("")) {
                location += ext + "\n";
            }
            location += catchNull(getItemValue(ticketData, "location_street")) + "\n";
            location += catchNull(getItemValue(ticketData, "location_city")) + "\n";
            
            fieldsPatient.remove(patientAddress);
            fieldsPatient.add(new FormElement("Lieferadresse", location));
        }
        
        
        try {
            PdfPopulater pdfApo = null;
            pdfApo = new PdfPopulater(pdf_key_apo);
            if (pdfApo != null) {
                pdfApo.fillFields(fieldsPatient);
                pdfApo.fillTable("Lieferliste", tablePatientContents);
                pdfs.put(PTYPE.APO, pdfApo.finish().toByteArray());
            }
        } catch (IllegalArgumentException e) {
            log.error("not apo pdf blob found!");
        }
        
        return pdfs;
     
    }
    
    private RowCell formatCell(RowCell cell) {
        
        cell.setFontSize(9);
        
        return cell;
     }
    
    private RowCell formatHeader(RowCell cell) {

       cell.setBackground(Color.LIGHT_GRAY);
       cell.setFontSize(10);
       cell.setFont(PDType1Font.HELVETICA_BOLD);

       return cell;
    }
    
    // {end privatemethods}

    // {section database}
    // ******************
    
    
    private Item dbGetTicketData() {
        
        DBClass db = new DBClass();
        
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("OMT_ID");
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("OMT_TYPE as type");
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("OMT_ID_LOCATION as location_id");
        
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("CONCAT(AUL_VORNAME, ' ', AUL_NAME) as nurse_name");
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("KSK_STRASSE as nurse_street");
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("CONCAT(KSK_PLZ, ' ', KSK_ORT) as nurse_city");
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("SL_NAME as location_name");
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("SL_STREET as location_street");
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("SL_EXTENDED as location_extended");
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("CONCAT(SL_PLZ, ' ', SL_CITY) as location_city");
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("SC_NAME as shipping_company");
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("OMT_DELIVERY_DATE as date_delivery");
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("OMT_KEY_COUNTRY as key_country");

        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Verknüpfung("cust_krankenschwester_stammdaten_ks", "LEFT JOIN /*2*/", "cms_auth_stammdaten_user", "KSK_U_ID", "AUL_ID");
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Verknüpfung("cust_shipping_companies", "RIGHT JOIN /*4*/", "cust_ordermanager_tickets", "SC_ID", "OMT_ID_SHIPPING_COMPANY");
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Verknüpfung("cust_shipping_locations", "right JOIN /*3*/", "cust_ordermanager_tickets", "SL_ID", "OMT_ID_LOCATION");
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Verknüpfung("cms_auth_stammdaten_user", "left JOIN /*1*/", "cust_ordermanager_tickets", "AUL_ID", "OMT_ID_USER");
        
        db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cust_ordermanager_tickets", "OMT_ID", "=", ticketId.toString(), "");
        
//        db.debugNextQuery(true);
        
        return db.DB_Data_Get.DB_SEND_AND_GET_FIRST_ITEM();
        
    }
    
    private Container dbGetTicketContents() {
        return dbGetTicketContents(false);
    }
    
    private Container dbGetTicketLabelContents() {
        return dbGetTicketContents(true);
    }
    
    private Container dbGetTicketContents(boolean grouped) {
        
        DBClass db = new DBClass();
        
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln(" CONCAT("
                                                    + "\n " + "        IF(PSA_NAME IS NOT NULL, PSA_NAME, ''), '" + (grouped ? "\n" : " " ) + "',"
                                                    + "\n " + "        PSL_VORNAME, ' ',"
                                                    + "\n " + "        PSL_NAME"
                                                    + "\n " + "    ) as patient");
//        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("OMT_STATUS_SENT_DATE as sent");
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("PP_PRODUCT as product");
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("SUM(OMTC_QTY) as quantity");
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("balance");

        String format = grouped ? "%d.%m.%Y" : "%d.%m";
        
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("GROUP_CONCAT(DATE_FORMAT(date, '"+format+"')  separator ', ') as dates");
       
        
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("PSL_ID");
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("PSL_GEB_DATUM as patient_birthdate");
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("PSL_STRASSE as patient_street");
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("CONCAT(PSL_PLZ, ' ', PSL_ORT) as patient_city");
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("PSL_COUNTYCODE as key_country");
        
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Verknüpfung("cust_ordermanager_ticket_contents", "LEFT JOIN", "cust_prescriptions_products", "OMTC_ID_PRODUCT", "PP_ID");
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Verknüpfung("cust_ordermanager_ticket_contents", "RIGHT JOIN", "view_balance_rx", "OMTC_ID_PATIENT", "PB_ID_PATIENT and OMTC_ID_PRODUCT = PB_ID_PRODUCT");
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Verknüpfung("cust_ordermanager_ticket_contents", "RIGHT JOIN", "cust_patient_stammdaten_liste", "OMTC_ID_PATIENT", "PSL_ID");
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Verknüpfung("cust_patient_stammdaten_liste", "LEFT JOIN", "cust_patient_stammdaten_anrede", "PSL_ANREDE", "PSA_ID");
        
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Verknüpfung("cust_ordermanager_ticket_contents", "RIGHT JOIN", "view_protokoll__overview", "OMTC_ID_DATA", "id and OMTC_ID_PROJECT = view_protokoll__overview.project");
        
        db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cust_ordermanager_ticket_contents", "OMTC_ID_TICKET", "=", ticketId.toString(), "");
        
        db.DB_Data_Get.DB_Gruppieren.DB_Gruppieren(grouped ? "OMTC_ID": "PP_ID, PSL_ID");
        
//        db.debugNextQuery(true);
        
        return db.DB_Data_Get.DB_SEND_AND_GET();
        
        
    }
    
    private Container dbGetTicketSums() {
        
        DBClass db = new DBClass();
        
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("PP_PRODUCT as product");
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("SUM(OMTC_QTY) as sum");
        
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Verknüpfung("cust_ordermanager_ticket_contents", "LEFT JOIN", "cust_prescriptions_products", "OMTC_ID_PRODUCT", "PP_ID");
        
        db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cust_ordermanager_ticket_contents", "OMTC_ID_TICKET", "=", ticketId.toString(), "");
        
        db.DB_Data_Get.DB_Gruppieren.DB_Gruppieren("OMTC_ID_PRODUCT");
        
//        db.debugNextQuery(true);
        
        return db.DB_Data_Get.DB_SEND_AND_GET();
        
        
    }
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
