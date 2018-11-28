package archenoah.scheduler.jobs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import archenoah.config.CMS_Config_Std;
import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.comunication.dbclass.DBClass.CustomQuery;
import archenoah.lib.tool.comunication.dbclass.SanitizeMySQL;

import com.google.common.base.Joiner;

public class TimebutlerSyncJob implements Job {

    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TimebutlerSyncJob.class);
    
    private static HashMap<String, ValueConverter> columnMap = new HashMap<String, ValueConverter>(); 
    
    static {
        
        columnMap.put("ID", new StringConverter("ID") {});
        columnMap.put("From", new DateConverter("FROM") {});
        columnMap.put("To", new DateConverter("TO") {});
        columnMap.put("Half a day", new BooleanConverter("HALF_DAY") {});
        columnMap.put("Morning", new BooleanConverter("MORNING") {});
        columnMap.put("User ID", new StringConverter("ID_TB") {});
        columnMap.put("Employee number", new StringConverter("ID_USER") {});
        columnMap.put("Type", new StringConverter("TYPE") {});
        columnMap.put("Extra vacation day", new BooleanConverter("EXTRA_DAY") {});
        columnMap.put("State", new StringConverter("STATE") {});
        columnMap.put("Substitute state", new StringConverter("STATE_SUB") {});
        columnMap.put("Workdays", new StringConverter("WORKDAYS") {}); // use autocast durin insert
        columnMap.put("hours", new StringConverter("HOURS") {}); // see above
        columnMap.put("Medical certificate (sick leave only)", new BooleanConverter("MEDICAL") {});
        columnMap.put("Comments", new StringConverter("COMMENTS") {});
        
    }
    
    static {
        // Ignore invalid Certificates for localhost debugging
        // Do not use in production
        // UrlConnectionHttpsOverride.deactivateCertValidation();
    }
    
    private ArrayList<ValueConverter> captions = new ArrayList<ValueConverter>();
    
    public TimebutlerSyncJob() {
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        if(CMS_Config_Std.getInstanceXmlOnly().timebutlerApiKey == null 
            || CMS_Config_Std.getInstanceXmlOnly().timebutlerApiKey == "") {
            
            log.error("timebutler apiKey not defined!");
            JobExecutionException e = new JobExecutionException("timebutler apiKey not defined!");
            e.setRefireImmediately(false);
            e.setUnscheduleAllTriggers(true);
            throw e;
        }
        
        if(CMS_Config_Std.getInstanceXmlOnly().timebutlerUrl == null 
            || CMS_Config_Std.getInstanceXmlOnly().timebutlerUrl == "") {
            
            log.error("timebutler url not defined!");
            JobExecutionException e = new JobExecutionException("timebutler url not defined!");
            e.setRefireImmediately(false);
            e.setUnscheduleAllTriggers(true);
            throw e;
        }

        LocalDate ld = new LocalDate();
        int moy = ld.getMonthOfYear();
        
        if(moy <= 3) {
            getYear(ld.getYear() - 1);
        }
        getYear(ld.getYear());
        if(moy >= 10) {
            getYear(ld.getYear() + 1);
        }
        
    }
    
    private void getYear(int year) throws JobExecutionException{
        
        log.info("running timebutler sync job for {}", year);
        
        CMS_Config_Std conf = CMS_Config_Std.getInstanceXmlOnly();
        
        String urls = "https://"+conf.timebutlerUrl+"/api/v1/absences?year="+year+"&auth="+conf.timebutlerApiKey;   
        
        ArrayList<ArrayList<String>> rows = new ArrayList<>(); 
        
        String delete = "delete from cust_timebutler_data where YEAR(`FROM`) = " + year;
        StringBuilder insert = null;
        
        captions.clear();
        
        try {

            URL url = new URL(urls);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            
            if(!MyUtils.equalsWithNulls(conn.getResponseCode(), 200)){
                log.error("error getting timebutler data: {} {}", conn.getResponseCode(), conn.getResponseCode());
                return;
            }
            
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;

            boolean firstLine = true;
            
            
            while ((line = rd.readLine()) != null) {

                ArrayList<String> fields = new ArrayList<String>(Arrays.asList(line.split(";")));
                
                if(firstLine) {
                    firstLine = false;
                    for (String value : fields) {
                        captions.add(columnMap.get(value));
                    }
                    continue;
                    
                }
                
                ArrayList<String> row = new ArrayList<String>();
                
                for (int i = 0; i < fields.size(); i++) {
                    String value =  fields.get(i);
                    ValueConverter conv = captions.get(i);
                    if (conv == null) {
                        continue;
                    }
                    row.add(conv.convert(value));
                    
                }
                
                if(row.size() > 0) {
                    rows.add(row);
                }
            }
            rd.close();
            conn.disconnect();
            
        } catch (Exception e) {
            
            log.error("Exception syncing data: {}", e);
            JobExecutionException jee = new JobExecutionException("Exception syncing data: " + e.getClass());
            jee.setRefireImmediately(false);
            jee.setUnscheduleAllTriggers(true);
            throw jee;
            
        }
            
        // build isert/update query

        if (rows.size() > 0) {

            insert = new StringBuilder("insert into cust_timebutler_data (");
            Joiner.on(",").appendTo(insert, captions);
            insert.append(") VALUES");

            for (ArrayList<String> row : rows) {
                insert.append("\n(");
                Joiner.on(",").appendTo(insert, row);
                insert.append("),");
            }

            insert.replace(insert.length() - 1, insert.length(), "");

            insert.append("\n ON DUPLICATE KEY UPDATE ");
            for (ValueConverter caption : captions) {
                insert.append(caption.toString());
                insert.append(" = VALUES(");
                insert.append(caption.toString());
                insert.append("),");
            }

            insert.replace(insert.length() - 1, insert.length(), "");

        }

        CustomQuery q = new DBClass().CustomQuery;
        q.setSqlString(delete);
//        q.db.debugNextQuery(true);
        q.update();

        if(insert != null) {
            q.setSqlString(insert.toString());
//            q.db.debugNextQuery(true);
            int res = q.update();
            
            if(res == 0) {
                log.error("Expected to insert {} entries, but insert query returned 0", rows.size());
                JobExecutionException jee = new JobExecutionException("Expected to insert "+rows.size()+" entries, but insert query returned 0");
                jee.setRefireImmediately(false);
                jee.setUnscheduleAllTriggers(true);
                throw jee;
            }
            
            log.info("job complete, inserted {} entries", res);
            
        }else {
            log.info("job complete, no entries found for {}", year);
        }
        
    }
    
    private static abstract class ValueConverter{
        
        String targetColumn;
        public ValueConverter(String targetColumn) {
            this.targetColumn = targetColumn;
        }
        
        public String getTargetColumn() {
            return targetColumn;
        }
        
        @Override
        public String toString() {
            return "`" + getTargetColumn() + "`";
        }
        
        public String convert(String value) {
            return SanitizeMySQL.escape(value);
        };
    }

    private static class DateConverter extends ValueConverter{

        public DateConverter(String targetColumn) {
            super(targetColumn);
        }

        @Override
        public String convert(String value) {
            DateTime date = DateTime.parse(value, DateTimeFormat.forPattern("dd/MM/yyyy"));
            return date != null ? "'" + MyUtils.formatSqlDate(date.toLocalDate()) + "'" : "NULL";
            
        }
    }
    
    private static class BooleanConverter extends ValueConverter{

        public BooleanConverter(String targetColumn) {
            super(targetColumn);
        }

        @Override
        public String convert(String value) {
            return MyUtils.equalsWithNulls("true", value) ? "1" : "0";
            
        }
    }
    
    private static class StringConverter extends ValueConverter{

        public StringConverter(String targetColumn) {
            super(targetColumn);
        }

        @Override
        public String convert(String value) {
            
            if(value != null && value.trim().length() == 0) {
                return "NULL";
            }
            
            return "'" + SanitizeMySQL.escape(value) + "'";
            
        }
    }
}
