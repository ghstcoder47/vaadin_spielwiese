package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.expenses.classes;

import java.util.ArrayList;
import java.util.Set;

import org.joda.time.LocalDateTime;
import org.tepi.filtertable.FilterTable;

import com.google.common.base.Joiner;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.comunication.dbclass.DBClass.CustomQuery;
import archenoah.web.normal.UserInfo.UserData;

public class SentStatusHandler {

	private FilterTable table;
    private Object dataProperty;
    
    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SentStatusHandler.class);
    
    public SentStatusHandler(FilterTable table, Object dataProperty) {
        this.table = table;
        this.dataProperty = dataProperty;
    }
    
    public void checked() {
        
        checkSent(getIdList());
        
    }
    
    public void checkSent(ArrayList<Integer> idList) {

        ArrayList<String> list = new ArrayList<String>(); 
        
        for (Integer entry : idList) {
            list.add("" + entry +"");
        }
        
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "UPDATE cust_expenses_data"
             + "\n " + "SET"
             + "\n " + "CED_STATUS = 3"
             + "\n " + ", CED_STATUS_CHECKED_DATE = '" + MyUtils.formatSqlDate(new LocalDateTime()) + "'"
             + "\n " + ", CED_STATUS_CHECKED_ID_USER = " + UserData.get().getUserId().toString()
             + "\n " + "WHERE" 
             + "\n " + "CED_ID IN("
             + Joiner.on(",\n").join(list)
             + "\n " + 	")"
             + "\n " + "AND"
             + "\n " + "	CED_STATUS = 2";
        
        q.setSqlString(sql);
        
//        q.db.debugNextQuery(true);
        
        q.update();
                
    }
    
    private ArrayList<Integer> getIdList() {
        
        if(table == null) {
            throw new IllegalArgumentException("table not set");
        }
        if(dataProperty == null) {
            throw new IllegalArgumentException("dataProperty not set");
        }
        
        ArrayList<Integer> pairs = new ArrayList<Integer>();
                
        if(table.isMultiSelect()) {
            
            Set<Object> values = (Set<Object>) table.getValue();
            for (Object iid : values) {
                
                Integer data = MyUtils.getValueFromItem(table.getItem(iid), dataProperty, Integer.class);
                 
                if(data != null) {
                    pairs.add(data);
                }
                
            }
            
        }
        
        return pairs;
    }
    
}
