package archenoah.web.normal.main.Menuepunkte.Individuelle.AuditManager.AuditLogging.classes;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.tepi.filtertable.FilterTable;

import archenoah.config.CMS_Config_Std;
import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.comunication.dbclass.DBClass.CustomQuery;
import archenoah.lib.tool.pdf.OnDemandFileDownloader;
import archenoah.lib.tool.pdf.OnDemandFileDownloader.OnDemandStreamResource;
import archenoah.lib.tool.pdf.PdfPopulater;
import archenoah.lib.tool.pdf.PdfPopulater.RowCell;
import archenoah.lib.tool.pdf.PdfPopulater.TableParameters;
import archenoah.lib.tool.pdf.PdfPopulater.TableRow;
import archenoah.lib.vaadin.Language.i18n.I18nConverter;
import be.quodlibet.boxable.HorizontalAlignment;

import com.google.common.base.Joiner;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Layout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

public class AuditPdfPrint {
    
    // {section fields}
    // ****************
    private org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
    
    private CMS_Config_Std config = CMS_Config_Std.getInstance();
    
    private HashMap<Integer, AuditBean> dataMap;
    
    private FilterTable table;
    
//    private final String LNG_ID = "512";
    private final String tableIdCol = "ALL_ID";
    
    private DateTimeFormatter fmt;
    private I18nConverter ratingConverter;

    private I18nConverter statusConverter;
    
    private enum Table {
        HEADER,
        LOGO,
        CONTENT
    }
    
    // {end fields}

    

    // {section constructors}
    // **********************
    public AuditPdfPrint(FilterTable table) {
        this.table = table;
        init();
    }
    
    public AuditPdfPrint() {
        init();
    }
    
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    
    public void attach(AbstractComponent component){
        
        new OnDemandFileDownloader(new OnDemandStreamResource() {

            @Override
            public String getFilename() {
                return "AUDITS-"+ DateTimeFormat.forStyle("MM").print(new DateTime())  + ".zip";
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
        
        final String downloadButtonID = "downloadPdfAudit";
        
        final Button downloadInvisibleButton = new Button();
        downloadInvisibleButton.setId(downloadButtonID);
        downloadInvisibleButton.addStyleName("hidden");
        layout.addComponent(downloadInvisibleButton);
        attach(downloadInvisibleButton);
        
        menuItem.setCommand(new Command() {
            
            @Override
            public void menuSelected(MenuItem selectedItem) {
                 com.vaadin.server.Page.getCurrent().getJavaScript().execute("document.getElementById('"+downloadButtonID+"').click();");
            }
        });
    }
    
//    public void test() {
//        
//        fillBeanList(new ArrayList<Object>(Arrays.asList(1)));
//        
//
//        for (Entry<Integer, AuditBean> entry : dataMap.entrySet()) {
//            log.info("beanId {}", entry.getKey());
//            
//            AuditBean bean = entry.getValue();
//            
//            for (Item finding : bean.getFindings().values()) {
//                log.info("finding: {}", finding);
//            }
//            
//            for (Entry<Integer, ArrayList<Item>> actions : bean.getActions().entrySet()) {
//                log.info("actions: {}", actions.getValue());
//            }
//            
//        }
//    }
    
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    
    private void init(){
        
        fmt = DateTimeFormat.forStyle("M-");
        ratingConverter = new I18nConverter("cust_audit_master_rating", "AMR_TOKEN");
        statusConverter = new I18nConverter("cust_taskmanager_master_status", "TMMST_TOKEN");
    }
    
   
   private byte[] generateOutput(){
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        
        
        ArrayList<String> filenames = new ArrayList<String>();
           
        ZipOutputStream zos = new ZipOutputStream(os);
        
        fillBeanList();
        
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
       
       PdfPopulater pdfSrc = new PdfPopulater("pdf_blank");
       byte[] fileByteArray = pdfSrc.getFileByteArray();
       pdfSrc.cleanup();
       
       for (Entry<Integer, AuditBean> entry : dataMap.entrySet()) {
           
           if(entry == null){
               continue;
           }
           
           Integer beanId = entry.getKey();
           AuditBean bean = entry.getValue();
           
//           PropertysetItem propertyItem = getPropertyItem(bean);
           
           
           
           PdfPopulater pdf = new PdfPopulater(fileByteArray);
           HashMap<Table, ArrayList<TableRow>> tables = getTables(bean);
           
           TableParameters paramsHeader = new TableParameters();
           paramsHeader.marginTopFirstPage = 0.05f;
           paramsHeader.marginTop = 0.05f;
           paramsHeader.marginSides = 0.03f;
           paramsHeader.marginBottom = 1f-0.2f;
           paramsHeader.width = 0.85f;
           pdf.fillTable(tables.get(Table.HEADER), paramsHeader);
           
           TableParameters paramsLogo = new TableParameters();
           paramsLogo.marginTopFirstPage = 0.05f;
           paramsLogo.marginTop = 0.05f;
           paramsLogo.marginSides = 0.83f;
           paramsLogo.marginBottom = 1f-0.2f;
           paramsLogo.width = 0.15f;
           paramsLogo.leftMarginOnly = true;
           pdf.fillTable(tables.get(Table.LOGO), paramsLogo);
           
           TableParameters paramsContent = new TableParameters();
           paramsContent.marginTopFirstPage = 0.2f;
           paramsContent.marginTop = 0.05f;
           paramsContent.marginSides = 0.03f;
           paramsContent.marginBottom = 0.05f;
           pdf.fillTable(tables.get(Table.CONTENT), paramsContent);
           
           
           
           //check if filename exists and append counter until it doesn't
           String filename = generatePdfName(bean.getDataItem());
           String fileZipName = filename;
           Integer counter = 0;
           while(filenames.contains(fileZipName)){
               fileZipName = filename + "-(" + ++counter + ")";
           }
           filenames.add(fileZipName);
           zos.putNextEntry(new ZipEntry(fileZipName + ".pdf"));

           zos.write(pdf.finish().toByteArray());
           zos.closeEntry();
           
       }
   }
   
   private String generatePdfName(Item item){
       
       StringBuffer str = new StringBuffer();
       
       DateTimeFormatter fmt = DateTimeFormat.forPattern("YYYY-MM-dd");
       DateTimeFormatter fmtp = DateTimeFormat.forStyle("M-");
       LocalDate ldt = new LocalDate();
       LocalDate ldtp = new LocalDate();
       if(item.getItemProperty("ALL_DATE").getValue() != null){
           ldtp = new LocalDate(MyUtils.getValueFromItem(item, "ALL_DATE", java.sql.Date.class).getTime());
       }
       
       
       str.append("AUDIT-");
       str.append(fmt.print(ldt));
       str.append("-[");
       str.append(item.getItemProperty("beanId").getValue());
       str.append("]");
       
       if(item.getItemProperty("ALL_DATE").getValue() != null && ldtp.getYear() > 1900){
           str.append("-");
           str.append(fmtp.print(ldtp));
       }
       
       return str.toString();
       
   }
   
   private HashMap<Table, ArrayList<TableRow>> getTables(AuditBean bean){
       
       HashMap<Table, ArrayList<TableRow>> tables = new HashMap<Table, ArrayList<TableRow>>(); 
       
       float headerLeft = 6f;
       float headerRight = 1f;
       
       float contentId = 0.5f;
       float contentTitle = 3f;
       float contentObservations = 6f;
       float contentRating = 1.5f;
       
       float actionId = 0.5f;
       float actionType = 1f;
       float actionDesc = 3f;
       float actionSolution = 4f;
       float actionDue = 1f;
       float actionStatus = 1.5f;
       
       
       
              
//       TableRow header = new TableRow(30f);
//       
//       RowCell headerLeftCell = new RowCell(null, headerLeft);
//       header.addEntry(headerLeftCell);
//       header.addEntry(formatCell(new RowCell("Healthcare at Home", headerRight)));
       
       RowCell formatHeader = new RowCell(null, 1f);
       formatHeader.setFontSize(8);
       formatHeader.setFont(PDType1Font.HELVETICA_BOLD);
       formatHeader.setBackground(Color.lightGray);
       
       RowCell formatFinding = new RowCell(null, 1f);
       formatFinding.setFontSize(8);
       
       RowCell formatAction = new RowCell(null, 1f);
       formatAction.setFontSize(8);
       
       RowCell formatActionHeader = new RowCell(null, 1f);
       formatActionHeader.setFontSize(8);
       formatActionHeader.setFont(PDType1Font.HELVETICA_BOLD);
       formatActionHeader.setBackground(Color.decode("#F0F0F0"));
       
       RowCell formatSpacer = new RowCell(null, 1f);
       formatSpacer.setBackground(Color.lightGray);
       RowCell formatIndent = new RowCell(null, 1f);

       
       
       tables.put(Table.CONTENT, new ArrayList<PdfPopulater.TableRow>());
       
       TableRow contentHeader = new TableRow(12f);
       contentHeader.addEntry(new RowCell("#", contentId).with(formatHeader));
       contentHeader.addEntry(new RowCell("Finding", contentTitle).with(formatHeader));
       contentHeader.addEntry(new RowCell("Beobachtungen", contentObservations).with(formatHeader));
       contentHeader.addEntry(new RowCell("Wertung", contentRating).with(formatHeader));
//       tables.get(Table.CONTENT).add(contentHeader);
       
       TableRow actionsHeader = new TableRow(10f);
       actionsHeader.addEntry(new RowCell("#", actionId).with(formatActionHeader));
       actionsHeader.addEntry(new RowCell("Action", actionType).with(formatActionHeader));
       actionsHeader.addEntry(new RowCell("Ursache", actionDesc).with(formatActionHeader));
       actionsHeader.addEntry(new RowCell("Maßnahme", actionSolution).with(formatActionHeader));
       actionsHeader.addEntry(new RowCell("Fällig", actionDue).with(formatActionHeader));
       actionsHeader.addEntry(new RowCell("Status", actionStatus).with(formatActionHeader));
//       tables.get(Table.CONTENT).add(actionsHeader);
       
       
       for (Entry<Integer, Item> findingEntry : bean.getFindings().entrySet()) {
           
           tables.get(Table.CONTENT).add(contentHeader);
           
           Item finding = findingEntry.getValue();
           
           TableRow row = new TableRow(10f);
           row.addEntry(new RowCell(Integer.toString(MyUtils.getValueFromItem(finding, "ALF_ID", Integer.class)), contentId).with(formatFinding));
           row.addEntry(new RowCell(MyUtils.getValueFromItem(finding, "ALF_TITLE", String.class), contentTitle).with(formatFinding));
           row.addEntry(new RowCell(MyUtils.getValueFromItem(finding, "ALF_OBSERVATIONS", String.class), contentObservations).with(formatFinding));
           
           row.addEntry(new RowCell(ratingConverter.getLngText(MyUtils.getValueFromItem(finding, "AMR_TOKEN", String.class)), contentRating).with(formatFinding));
           tables.get(Table.CONTENT).add(row);
           
           ArrayList<Item> actions = bean.getActions().get(findingEntry.getKey());
           
           if(actions.size() > 0) {
               tables.get(Table.CONTENT).add(actionsHeader);
           }
           
           for (Item item : actions) {
               TableRow actionRow = new TableRow(10f);
               actionRow.addEntry(new RowCell(Integer.toString(MyUtils.getValueFromItem(item, "TMT_ID", Integer.class)), actionId).with(formatAction));
               actionRow.addEntry(new RowCell(MyUtils.getValueFromItem(item, "type", String.class), actionType).with(formatAction));
               actionRow.addEntry(new RowCell(MyUtils.getValueFromItem(item, "TMT_DESCRIPTION", String.class), actionDesc).with(formatAction));
               actionRow.addEntry(new RowCell(MyUtils.getValueFromItem(item, "TMT_SOLUTION", String.class), actionSolution).with(formatAction));
               actionRow.addEntry(new RowCell(fmt.print(MyUtils.getValueFromItem(item, "TMT_DUE", java.sql.Date.class).getTime()), actionDue).with(formatAction));
               actionRow.addEntry(new RowCell(statusConverter.getLngText(MyUtils.getValueFromItem(item, "TMMST_TOKEN", String.class)), actionStatus).with(formatAction));
               tables.get(Table.CONTENT).add(actionRow);    
           }
//           TableRow spacer = new TableRow(0.1f);
//           spacer.addEntry(new RowCell(null,1f).with(formatSpacer));
//           tables.get(Table.CONTENT).add(spacer);

           
       }
       
       // Header
       
       
       RowCell fh = new RowCell(null, 1f);
       fh.setFontSize(14);
       fh.setFont(PDType1Font.HELVETICA_BOLD);
       fh.setBorder(false);
       
       RowCell fb = new RowCell(null, 1f);
       fb.setFontSize(10);
       fb.setBorder(false);
       
       RowCell fr = new RowCell(null, 1f).with(fb);
       fr.setAlign(HorizontalAlignment.RIGHT);
       
       
       tables.put(Table.HEADER, new ArrayList<PdfPopulater.TableRow>());
       
       TableRow headerTitle = new TableRow(16f);
       headerTitle.addEntry(new RowCell("Auditbericht mit Maßnahmenplan", 2f).with(fh)); 
       headerTitle.addEntry(new RowCell( formatNull(MyUtils.getValueFromItem(bean.getDataItem(), "ALT_NAME", String.class))
            + " vom " + fmt.print(MyUtils.getValueFromItem(bean.getDataItem(), "ALL_DATE", java.sql.Date.class).getTime()), 1f).with(fr)); 
       tables.get(Table.HEADER).add(headerTitle);
       
       TableRow headerInfo = new TableRow(12f);
       headerInfo.addEntry(new RowCell("Auditierter Bereich: " + formatNull(MyUtils.getValueFromItem(bean.getDataItem(), "ALD_NAME", String.class)), 2f).with(fb)); 
       headerInfo.addEntry(new RowCell("Druckdatum: " + fmt.print(new Date().getTime()), 1f).with(fr));
       tables.get(Table.HEADER).add(headerInfo);
       
       TableRow headerAuditor = new TableRow(12f);
       headerAuditor.addEntry(new RowCell("Auditor: " + formatNull(MyUtils.getValueFromItem(bean.getDataItem(), "ALL_AUDITOR", String.class)), 1f).with(fb));
       tables.get(Table.HEADER).add(headerAuditor); 
       
       // Logo
       
       RowCell lf = new RowCell(null, 1f);
       lf.setFontSize(20);
       lf.setBorder(false);
       lf.setAlign(HorizontalAlignment.CENTER);
       lf.setFont(PDType1Font.HELVETICA_BOLD);
       
       tables.put(Table.LOGO, new ArrayList<PdfPopulater.TableRow>());
       TableRow logoRow = new TableRow(40f);
       logoRow.addEntry(new RowCell("Healthcare <br />at Home", 1f).with(lf));
       tables.get(Table.LOGO).add(logoRow); 
       
       return tables;
       
   }
   
    private String formatNull(String value) {

        if (value == null) {
            return "n.a.";
        }

        return value;

    }
   
//   @SuppressWarnings("rawtypes")
//   private PropertysetItem getPropertyItem(AuditBean bean){
//       
//       ArrayList<FormElement> fieldList = new ArrayList<FormElement>();
//       
//       Item rowItem = bean.getDataItem();
//       PropertysetItem item = new PropertysetItem();
//       
//       //item.getItemProperty("DATE").setValue();
//       
//       //System.out.println(rowItem.getItemProperty("ID"));
//       
//       for (Object pid : rowItem.getItemPropertyIds()) {
//           
//           item.addItemProperty(pid, rowItem.getItemProperty(pid));
//           
//       }
//       
////       // DATE
////       if(rowItem.getItemProperty("DATE").getValue() != null){
////           LocalDate date = new LocalDate(rowItem.getItemProperty("DATE").getValue());
////           item.addItemProperty("INF_Date", new ObjectProperty<String>(fmt.print(date)));
////       }
//
//       
//       return item;
//       
//       
//       
//       
//   }
   
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
       
       // setup Bean List
       dataMap = new HashMap<Integer, AuditBean>();
       for (Object iid : rows.getItemIds()) {
           AuditBean bean = new AuditBean(rows.getItem(iid));
           
           log.debug("adding bean with id {}", bean.getId());
           
           dataMap.put(bean.getId(), bean);
       }
       
       // add Findings
       Container findings = getFindings(pvdValues);
       for (Object iid : findings.getItemIds()) {
           Item item = findings.getItem(iid);
           AuditBean bean = dataMap.get(MyUtils.getValueFromItem(item, "beanId", Integer.class));
           
           log.debug("adding finding {} to bean {}", item, bean);
           
           if(bean != null) {
               bean.addFinding(item);
           }else {
               log.warn("no DataBean found for finding {}", item);
           }
       }
       
       // add Actions
       Container actions = getActions(pvdValues);
       for (Object iid : actions.getItemIds()) {
           Item item = actions.getItem(iid);
           AuditBean bean = dataMap.get(MyUtils.getValueFromItem(item, "beanId", Integer.class));
           
           log.debug("adding action {} to bean {}", item, bean);
           
           if(bean != null) {
               bean.addAction(item);
           }else {
               log.warn("no DataBean found for action {}", item);
           }
       }

   }
   
    // {end privatemethods}

    // {section database}
    // ******************
   
    private Container getExportData(Collection values) {

        CustomQuery q = new DBClass().CustomQuery;
        String sql = "select"
         + "\n " + "    ALL_ID as beanId"
         + "\n " + "    , ALL_DATE"
         + "\n " + "    , ALT_NAME"
         + "\n " + "    , ALD_NAME"
         + "\n " + "    , ALL_AUDITOR"
         + "\n " + "from cust_audit_logging_logs"
         + "\n " + "left join cust_audit_logging_type on ALL_ID_TYPE = ALT_ID"
         + "\n " + "left join cust_audit_logging_division on ALL_ID_DIVISION = ALD_ID"
         + "\n " + "where ALL_ID IN ("+Joiner.on(",").join(values)+")";
        q.setSqlString(sql);
//        q.db.debugNextQuery(true);

        return q.query();

    }
   
    private Container getFindings(Collection values) {

        CustomQuery q = new DBClass().CustomQuery;
        String sql = "select"
         + "\n " + "    ALF_ID"
         + "\n " + "    , ALF_ID_AUDIT as beanId"
         + "\n " + "    , ALF_TITLE"
         + "\n " + "    , ALF_OBSERVATIONS"
         + "\n " + "    , AMR_TOKEN"
         + "\n " + "from cust_audit_logging_findings"
         + "\n " + "left join cust_audit_master_rating on ALF_RATING = cust_audit_master_rating.AMR_ID"
         + "\n " + "where ALF_ID_AUDIT IN ("+Joiner.on(",").join(values)+")";
        q.setSqlString(sql);
//        q.db.debugNextQuery(true);
        
        return q.query();

    }
    
    private Container getActions(Collection values) {
        
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "select"
         + "\n " + "    ALF_ID_AUDIT as beanId"
         + "\n " + "    , TMT_ID"
         + "\n " + "    , TMETT_ID_TOPIC"
         + "\n " + "    , IF(TMT_ID_TYPE = 101, 'CA', 'PA') as `type`"
         + "\n " + "    , TMT_DESCRIPTION"
         + "\n " + "    , TMT_SOLUTION"
         + "\n " + "    , TMT_DUE"
         + "\n " + "    , TMMST_TOKEN"
         + "\n " + "from cust_audit_logging_findings"
         + "\n " + "inner join cust_taskmanager_meeting_topic_tasks on ALF_ID = TMETT_ID_TOPIC and TMETT_TYPE = 'TYPE_CAPA'"
         + "\n " + "left join cust_taskmanager_tasks on TMETT_ID_TASK = cust_taskmanager_tasks.TMT_ID"
         + "\n " + "left join cust_taskmanager_master_status on TMT_ID_STATUS = TMMST_ID"
         + "\n " + ""
         + "\n " + "where ALF_ID_AUDIT IN ("+Joiner.on(",").join(values)+")";
        q.setSqlString(sql);
//        q.db.debugNextQuery(true);
        
        return q.query();
        
    }
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
   
   
    private class AuditBean {

        private Integer id;

        private Item dataItem;
        private LinkedHashMap<Integer, Item> findings;
        private LinkedHashMap<Integer, ArrayList<Item>> actions;

        public AuditBean(Item item) {
            setDataItem(item);
            setId(MyUtils.getValueFromItem(item, "beanId", Integer.class));
            findings = new LinkedHashMap<Integer, Item>();
            actions = new LinkedHashMap<Integer, ArrayList<Item>>();
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

        public LinkedHashMap<Integer, Item> getFindings() {
            return findings;
        }

        public void setFindings(LinkedHashMap<Integer, Item> findings) {
            this.findings = findings;
        }

        public LinkedHashMap<Integer, ArrayList<Item>> getActions() {
            return actions;
        }

        public void setActions(LinkedHashMap<Integer, ArrayList<Item>> actions) {
            this.actions = actions;
        }

        // add

        public void addFinding(Item finding) {

            Integer fid = MyUtils.getValueFromItem(finding, "ALF_ID", Integer.class);

            findings.put(fid, finding);
            actions.put(fid, new ArrayList<Item>());
        }

        public void addAction(Item action) {

            Integer fid = MyUtils.getValueFromItem(action, "TMETT_ID_TOPIC", Integer.class);
            
            if (actions.get(fid) == null) {
                actions.put(fid, new ArrayList<Item>());
            }

            actions.get(fid).add(action);
        }
    }
}