package archenoah.web.normal.main.Menuepunkte.Individuelle.TaskSystem.MeetingLogging.classes;

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

import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.tepi.filtertable.FilterTable;

import archenoah.config.CMS_Config_Std;
import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.comunication.dbclass.DBClass.CustomQuery;
import archenoah.lib.tool.pdf.FontProvider;
import archenoah.lib.tool.pdf.FontProvider.FONT;
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

public class MeetingPdfPrint {
    
    // {section fields}
    // ****************
    private org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
    
    private CMS_Config_Std config = CMS_Config_Std.getInstance();
    
    private HashMap<Integer, MeetingBean> dataMap;
    
    private FilterTable table;
    
    private final String tableIdCol = "TMEL_ID";
    
    private DateTimeFormatter fmt;

    private I18nConverter statusConverter;
    private I18nConverter typeConverter;
    
    private enum Table {
        HEADER,
        LOGO,
        CONTENT
    }
    
    // {end fields}

    

    // {section constructors}
    // **********************
    public MeetingPdfPrint(FilterTable table) {
        this.table = table;
        init();
    }
    
    public MeetingPdfPrint() {
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
            public byte[] getOutput() {
                return generateOutput();
            }

            @Override
            public String getContentType() {
                return "application/zip";
            }
            
            @Override
            public String getFilename() {
                return "MEETING-"+ DateTimeFormat.forStyle("MM").print(new DateTime())  + ".zip";
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
        statusConverter = new I18nConverter("cust_taskmanager_master_status", "TMMST_TOKEN");
        typeConverter = new I18nConverter("cust_taskmanager_master_types", "TMMT_TOKEN");
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
       
       for (Entry<Integer, MeetingBean> entry : dataMap.entrySet()) {
           
           if(entry == null){
               continue;
           }
           
           Integer beanId = entry.getKey();
           MeetingBean bean = entry.getValue();
           
//           PropertysetItem propertyItem = getPropertyItem(bean);
           
           Float offset = 0.2f;
           
           
           PdfPopulater pdf = new PdfPopulater(fileByteArray);
           HashMap<Table, ArrayList<TableRow>> tables = getTables(bean, pdf);
           
           TableParameters paramsHeader = new TableParameters();
           paramsHeader.marginTopFirstPage = 0.05f;
           paramsHeader.marginTop = 0.05f;
           paramsHeader.marginSides = 0.03f;
           paramsHeader.marginBottom = 1f-offset;
           paramsHeader.width = 0.85f;
           pdf.fillTable(tables.get(Table.HEADER), paramsHeader);
           
           TableParameters paramsLogo = new TableParameters();
           paramsLogo.marginTopFirstPage = 0.05f;
           paramsLogo.marginTop = 0.05f;
           paramsLogo.marginSides = 0.83f;
           paramsLogo.marginBottom = 1f-offset;
           paramsLogo.width = 0.15f;
           paramsLogo.leftMarginOnly = true;
           pdf.fillTable(tables.get(Table.LOGO), paramsLogo);
           
           TableParameters paramsContent = new TableParameters();
           paramsContent.marginTopFirstPage = offset;
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
       if(item.getItemProperty("TMEL_DATE").getValue() != null){
           ldtp = new LocalDate(MyUtils.getValueFromItem(item, "TMEL_DATE", java.sql.Date.class).getTime());
       }
       
       
       str.append("MEETING-");
       str.append(fmt.print(ldt));
       str.append("-[");
       str.append(item.getItemProperty("beanId").getValue());
       str.append("]");
       
       if(item.getItemProperty("TMEL_DATE").getValue() != null && ldtp.getYear() > 1900){
           str.append("-");
           str.append(fmtp.print(ldtp));
       }
       
       return str.toString();
       
   }
   
   private HashMap<Table, ArrayList<TableRow>> getTables(MeetingBean bean, PdfPopulater pdf){
       
       HashMap<Table, ArrayList<TableRow>> tables = new HashMap<Table, ArrayList<TableRow>>(); 
       
       PDType0Font LiberationSansBold = FontProvider.load(pdf, FONT.LiberationSansBold);
       
       float headerLeft = 6f;
       float headerRight = 1f;
       
       float contentId = 1f;
       float contentTitle = 2f;
       float contentObservations = 4f;
       float contentRating = 1f;
       
              
//       TableRow header = new TableRow(30f);
//       
//       RowCell headerLeftCell = new RowCell(null, headerLeft);
//       header.addEntry(headerLeftCell);
//       header.addEntry(formatCell(new RowCell("Healthcare at Home", headerRight)));
       
       RowCell formatHeader = new RowCell(null, 1f);
       formatHeader.setFontSize(8);
       formatHeader.setFont(LiberationSansBold);
       formatHeader.setBackground(Color.lightGray);
       RowCell formatFinding = new RowCell(null, 1f);
       formatFinding.setFontSize(8);
       RowCell formatAction = new RowCell(null, 1f);
       formatAction.setFontSize(8);
       RowCell formatSpacer = new RowCell(null, 1f);
       formatSpacer.setBackground(Color.lightGray);
       RowCell formatIndent = new RowCell(null, 1f);

       
       
       tables.put(Table.CONTENT, new ArrayList<PdfPopulater.TableRow>());
       
       TableRow contentParticipants = new TableRow(12f);
       contentParticipants.addEntry(new RowCell("Teilnehmer", 1f).with(formatHeader));
       contentParticipants.addEntry(new RowCell(Joiner.on(", ").join(bean.getParticipants().values()), 10f).with(formatFinding));
       tables.get(Table.CONTENT).add(contentParticipants);
       
       TableRow contentHeader = new TableRow(12f);
       contentHeader.addEntry(new RowCell("Thema", contentTitle).with(formatHeader));
       contentHeader.addEntry(new RowCell("Notizen", contentObservations).with(formatHeader));
       tables.get(Table.CONTENT).add(contentHeader);
       
       TableRow actionsHeader = new TableRow(10f);
//       actionsHeader.addEntry(new RowCell(null, 1f).with(formatHeader));
       actionsHeader.addEntry(new RowCell("Typ", 1f).with(formatHeader));
       actionsHeader.addEntry(new RowCell("Zusammenfassung", 4f).with(formatHeader));
       actionsHeader.addEntry(new RowCell("Beschreibung", 4f).with(formatHeader));
       actionsHeader.addEntry(new RowCell("Lösung", 4f).with(formatHeader));
       actionsHeader.addEntry(new RowCell("Fällig", 1f).with(formatHeader));
       actionsHeader.addEntry(new RowCell("Status", 1f).with(formatHeader));
       tables.get(Table.CONTENT).add(actionsHeader);
       
       
       for (Entry<Integer, Item> findingEntry : bean.getTopics().entrySet()) {
           
           Item finding = findingEntry.getValue();
           
           TableRow row = new TableRow(10f);
           row.addEntry(new RowCell(MyUtils.getValueFromItem(finding, "TMET_TITLE", String.class), contentTitle).with(formatFinding));
           row.addEntry(new RowCell(MyUtils.getValueFromItem(finding, "TMET_RESULT", String.class), contentObservations).with(formatFinding));
           tables.get(Table.CONTENT).add(row);
           
           
           ArrayList<Item> actions = bean.getTasks().get(findingEntry.getKey());
           
           for (Item item : actions) {
               TableRow actionRow = new TableRow(10f);
//               actionRow.addEntry(new RowCell(null, 1f).with(formatIndent));
               actionRow.addEntry(new RowCell(typeConverter.getLngText(MyUtils.getValueFromItem(item, "TMMT_TOKEN", String.class)), 1f).with(formatAction));
               actionRow.addEntry(new RowCell(MyUtils.getValueFromItem(item, "TMT_TITLE", String.class), 4f).with(formatAction));
               actionRow.addEntry(new RowCell(MyUtils.getValueFromItem(item, "TMT_DESCRIPTION", String.class), 4f).with(formatAction));
               actionRow.addEntry(new RowCell(MyUtils.getValueFromItem(item, "TMT_SOLUTION", String.class), 4f).with(formatAction));
               String due = "";
               if (MyUtils.getValueFromItem(item, "TMT_DUE", java.sql.Date.class) != null) {
                   due = fmt.print(MyUtils.getValueFromItem(item, "TMT_DUE", java.sql.Date.class).getTime());
               }
               actionRow.addEntry(new RowCell(due, 1f).with(formatAction));
               actionRow.addEntry(new RowCell(statusConverter.getLngText(MyUtils.getValueFromItem(item, "TMMST_TOKEN", String.class)), 1f).with(formatAction));
               tables.get(Table.CONTENT).add(actionRow);    
           }
           TableRow spacer = new TableRow(0.1f);
           spacer.addEntry(new RowCell(null,1f).with(formatSpacer));
           tables.get(Table.CONTENT).add(spacer);
           
       }
       
       // Header
       
       
       RowCell fh = new RowCell(null, 1f);
       fh.setFontSize(14);
       fh.setFont(LiberationSansBold);
       fh.setBorder(false);
       
       RowCell fb = new RowCell(null, 1f);
       fb.setFontSize(10);
       fb.setBorder(false);
       
       RowCell fr = new RowCell(null, 1f).with(fb);
       fr.setAlign(HorizontalAlignment.RIGHT);
       
       
       tables.put(Table.HEADER, new ArrayList<PdfPopulater.TableRow>());
       
       TableRow headerTitle = new TableRow(16f);
       headerTitle.addEntry(new RowCell("Meetingprotokoll mit Aufgaben", 2f).with(fh)); 
       headerTitle.addEntry(new RowCell( "vom " + fmt.print(MyUtils.getValueFromItem(bean.getDataItem(), "TMEL_DATE", java.sql.Date.class).getTime()), 1f).with(fr)); 
       tables.get(Table.HEADER).add(headerTitle);
       
       TableRow headerInfo = new TableRow(12f);
       headerInfo.addEntry(new RowCell("Team: " + formatNull(MyUtils.getValueFromItem(bean.getDataItem(), "TMEG_NAME", String.class)), 2f).with(fb)); 
       headerInfo.addEntry(new RowCell("Druckdatum: " + fmt.print(new Date().getTime()), 1f).with(fr));
       tables.get(Table.HEADER).add(headerInfo);
       
       TableRow headerAuditor = new TableRow(12f);
       headerAuditor.addEntry(new RowCell("Protokollant: " + formatNull(MyUtils.getValueFromItem(bean.getDataItem(), "editor", String.class)), 1f).with(fb));
       tables.get(Table.HEADER).add(headerAuditor);
       
       // Logo
       
       RowCell lf = new RowCell(null, 1f);
       lf.setFontSize(20);
       lf.setBorder(false);
       lf.setAlign(HorizontalAlignment.CENTER);
       lf.setFont(LiberationSansBold);
       
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
       dataMap = new HashMap<Integer, MeetingBean>();
       for (Object iid : rows.getItemIds()) {
           MeetingBean bean = new MeetingBean(rows.getItem(iid));
           
           log.debug("adding bean with id {}", bean.getId());
           
           dataMap.put(bean.getId(), bean);
       }
       
       // add Findings
       Container findings = getTopics(pvdValues);
       for (Object iid : findings.getItemIds()) {
           Item item = findings.getItem(iid);
           MeetingBean bean = dataMap.get(MyUtils.getValueFromItem(item, "beanId", Integer.class));
           
           log.debug("adding finding {} to bean {}", item, bean);
           
           if(bean != null) {
               bean.addTopic(item);
           }else {
               log.warn("no DataBean found for finding {}", item);
           }
       }
       
       // add Actions
       Container actions = getTasks(pvdValues);
       for (Object iid : actions.getItemIds()) {
           Item item = actions.getItem(iid);
           MeetingBean bean = dataMap.get(MyUtils.getValueFromItem(item, "beanId", Integer.class));
           
           log.debug("adding action {} to bean {}", item, bean);
           
           if(bean != null) {
               bean.addTask(item);
           }else {
               log.warn("no DataBean found for action {}", item);
           }
       }

       // add Participants
       Container participants = getParticipants(pvdValues);
       for (Object iid : participants.getItemIds()) {
           Item item = participants.getItem(iid);
           MeetingBean bean = dataMap.get(MyUtils.getValueFromItem(item, "beanId", Integer.class));
           
           log.debug("adding participant {} to bean {}", item, bean);
           
           if(bean != null) {
               bean.addParticipant(item);
           }else {
               log.warn("no DataBean found for participant {}", item);
           }
       }
       
   }
   
    // {end privatemethods}

    // {section database}
    // ******************
   
    private Container getExportData(Collection values) {

        CustomQuery q = new DBClass().CustomQuery;
        String sql = "select"
         + "\n " + "    TMEL_ID as beanId"
         + "\n " + "    , TMEL_DATE"
         + "\n " + "    , TMEG_NAME"
         + "\n " + "    , CONCAT(ASA_NAME, ' ', AUL_VORNAME, ' ', AUL_NAME) as editor"
         + "\n " + "from cust_taskmanager_meeting_logs"
         + "\n " + "left join cms_auth_stammdaten_user on TMEL_ID_EDITOR = AUL_ID"
         + "\n " + "left join cms_auth_stammdaten_anrede on AUL_ANREDE_ID = ASA_ID"
         + "\n " + "left join cust_taskmanager_meeting_groups on TMEL_ID_TEAM = TMEG_ID"
         + "\n " + "where TMEL_ID IN ("+Joiner.on(",").join(values)+")";
        q.setSqlString(sql);
//        q.db.debugNextQuery(true);

        return q.query();

    }
   
    private Container getTopics(Collection values) {

        CustomQuery q = new DBClass().CustomQuery;
        String sql = "select"
         + "\n " + "    TMET_ID"
         + "\n " + "    , TMET_ID_LOG as beanId"
         + "\n " + "    , TMET_TITLE"
         + "\n " + "    , TMET_RESULT"
         + "\n " + "from cust_taskmanager_meeting_topics"
         + "\n " + "where TMET_ID_LOG IN ("+Joiner.on(",").join(values)+")";
        q.setSqlString(sql);
//        q.db.debugNextQuery(true);
        
        return q.query();

    }
    
    private Container getTasks(Collection values) {
        
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "select"
         + "\n " + "    TMET_ID_LOG as beanId"
         + "\n " + "    , TMETT_ID_TOPIC"
         + "\n " + "    , TMMT_TOKEN"
         + "\n " + "    , TMT_TITLE"
         + "\n " + "    , TMT_DESCRIPTION"
         + "\n " + "    , TMT_SOLUTION"
         + "\n " + "    , TMT_DUE"
         + "\n " + "    , TMMST_TOKEN"
         + "\n " + "from cust_taskmanager_meeting_topics"
         + "\n " + "inner join cust_taskmanager_meeting_topic_tasks on TMET_ID = TMETT_ID_TOPIC and TMETT_TYPE = 'TYPE_TASK'"
         + "\n " + "left join cust_taskmanager_tasks on TMETT_ID_TASK = cust_taskmanager_tasks.TMT_ID"
         + "\n " + "left join cust_taskmanager_master_status on TMT_ID_STATUS = TMMST_ID"
         + "\n " + "left join cust_taskmanager_master_types on TMT_ID_TYPE = TMMT_ID"
         + "\n " + ""
         // exclude nonexisting but still linked tasks
         + "\n " + "where TMT_ID IS NOT NULL and TMET_ID_LOG IN ("+Joiner.on(",").join(values)+")";
        q.setSqlString(sql);
//        q.db.debugNextQuery(true);
        
        return q.query();
        
    }
    
    private Container getParticipants(Collection values) {
        
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "select TMEP_ID_MEETING as beanId"
         + "\n " + "    , AUL_ID"
         + "\n " + "    , CONCAT(ASA_NAME, ' ', AUL_VORNAME, ' ', AUL_NAME) as participant"
         + "\n " + " from cust_taskmanager_meeting_participants"
         + "\n " + " left join cms_auth_stammdaten_user on AUL_ID = TMEP_ID_USER"
         + "\n " + " left join cms_auth_stammdaten_anrede on AUL_ANREDE_ID = ASA_ID"
         + "\n " + "where TMEP_ID_MEETING IN ("+Joiner.on(",").join(values)+")";
        
        q.setSqlString(sql);
        
        return q.query();
        
    }
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
   
   
    private class MeetingBean {

        private Integer id;

        private Item dataItem;
        private LinkedHashMap<Integer, Item> topics;
        private LinkedHashMap<Integer, ArrayList<Item>> tasks;
        private HashMap<Integer, String> participants = new HashMap<Integer, String>();

        public MeetingBean(Item item) {
            setDataItem(item);
            setId(MyUtils.getValueFromItem(item, "beanId", Integer.class));
            topics = new LinkedHashMap<Integer, Item>();
            tasks = new LinkedHashMap<Integer, ArrayList<Item>>();
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

        public LinkedHashMap<Integer, Item> getTopics() {
            return topics;
        }

        public void setTopics(LinkedHashMap<Integer, Item> topics) {
            this.topics = topics;
        }

        public LinkedHashMap<Integer, ArrayList<Item>> getTasks() {
            return tasks;
        }

        public void setTasks(LinkedHashMap<Integer, ArrayList<Item>> tasks) {
            this.tasks = tasks;
        }

        public HashMap<Integer, String> getParticipants() {
            return participants;
        }
        
        // add

        public void addTopic(Item topic) {

            Integer fid = MyUtils.getValueFromItem(topic, "TMET_ID", Integer.class);

            topics.put(fid, topic);
            tasks.put(fid, new ArrayList<Item>());
        }

        public void addTask(Item task) {

            Integer fid = MyUtils.getValueFromItem(task, "TMETT_ID_TOPIC", Integer.class);
            
            if (tasks.get(fid) == null) {
                tasks.put(fid, new ArrayList<Item>());
            }

            tasks.get(fid).add(task);
        }
        
        public void addParticipant(Item item) {
            
            Integer pid = MyUtils.getValueFromItem(item, "AUL_ID", Integer.class);
            participants.put(pid, MyUtils.getValueFromItem(item, "participant", String.class));

        }
    }
}