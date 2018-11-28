package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.combined.classes;

import java.util.ArrayList;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.tepi.filtertable.FilterTable;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.comunication.dbclass.DBClass.CustomQuery;
import archenoah.web.normal.UserInfo.UserData;

import com.google.common.base.Joiner;

public class SentStatusHandler {

    private Object projectProperty;
    private Object dataProperty;
    private FilterTable table;
    
    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SentStatusHandler.class);
    
    public SentStatusHandler(FilterTable table, Object projectProperty, Object dataProperty) {
        this.table = table;
        this.projectProperty = projectProperty;
        this.dataProperty = dataProperty;
    }
    
    public void markSent() {
        
        markSent(getIdList());
        
    }

    public void unmarkSent() {
        
        unmarkSent(getIdList());
        
    }
    
    public void markSent(ArrayList<Pair<Integer, Integer>> idList) {
        
        Integer uid = UserData.get().getUserId();
        
        ArrayList<String> list = new ArrayList<String>(); 
        
        for (Pair<Integer, Integer> entry : idList) {
            list.add("("+ entry.getKey() + ", " + entry.getValue() + ", " + uid + ")");
        }
        
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "insert into cust_protokoll_sent (ID_PROJECT, ID_DATA, ID_USER)"
             + "\n " + "VALUES"
             + Joiner.on(",\n").join(list)
             + "\n " + "ON DUPLICATE KEY UPDATE DATE_SENT = DATE_SENT, ID_USER = ID_USER";
        q.setSqlString(sql);
        q.update();
        
    }
    
    public void unmarkSent(ArrayList<Pair<Integer, Integer>> idList) {
        
        ArrayList<String> list = new ArrayList<String>(); 
        
        for (Pair<Integer, Integer> entry : idList) {
            list.add("(ID_PROJECT = " + entry.getKey() + " and ID_DATA = " + entry.getValue() + ")"); 
        }
        
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "delete from cust_protokoll_sent where \n"
             + Joiner.on("\n or ").join(list) + ";";
        q.setSqlString(sql);
        q.update();
        
    }
    
    private ArrayList<Pair<Integer, Integer>> getIdList() {
        
        if(table == null) {
            throw new IllegalArgumentException("table not set");
        }
        if(projectProperty == null) {
            throw new IllegalArgumentException("projectProperty not set");
        }
        if(dataProperty == null) {
            throw new IllegalArgumentException("dataProperty not set");
        }
        
        ArrayList<Pair<Integer, Integer>> pairs = new ArrayList<Pair<Integer, Integer>>();
        
        if(table.isMultiSelect()) {
            
            Set<Object> values = (Set<Object>) table.getValue();
            for (Object iid : values) {
                
                
                Integer data = MyUtils.getValueFromItem(table.getItem(iid), dataProperty, Integer.class);
                
                Integer project = null;
                Long lp = MyUtils.getValueFromItem(table.getItem(iid), projectProperty, Long.class);
                if(lp != null) {
                    project = lp.intValue();
                }
                
                if(project != null && data != null) {
                    pairs.add(Pair.of(project, data));
                }
                
                
            }
            
        }
        
        return pairs;
    }
    
}
