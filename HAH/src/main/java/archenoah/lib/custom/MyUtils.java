package archenoah.lib.custom;

import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import archenoah.web.normal.UserInfo.UserData;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.vaadin.data.Container;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.UI;

public class MyUtils {
    
    
    public static void setUserData(Integer userId) {
        
        if(userId != null && userId > 0) {
            UI.getCurrent().getUI().getSession().getSession().setAttribute("UserData", new UserData(userId));
        }
        
    }
    
    public static UserData getUserData() {
        return (UserData) UI.getCurrent().getUI().getSession().getSession().getAttribute("UserData");
    }
    
    /**
     * 
     * @param a
     * @param b
     * @return true if both objects are equal, or both are null, false otherwise. 
     */
    public static final boolean equalsWithNulls(Object a, Object b) {

        if (a == b)
            return true;
        if ((a == null) || (b == null))
            return false;
        return a.equals(b);

    }
    
    public static final Integer BigDecimalToInt(BigDecimal bd) {
        
        Integer res = null;
        
        if(bd != null) {
            res = bd.intValueExact();
        }
        
        return res;
    }
    
    public static BigDecimal toBigDecimal(Object value) {
        return toBigDecimal(value, null);
    }
    
    public static BigDecimal toBigDecimal( Object value , Integer decimals) {
        BigDecimal ret = null;
        if( value != null ) {
            if( value instanceof BigDecimal ) {
                ret = (BigDecimal) value;
            } else if( value instanceof BigInteger ) {
                ret = new BigDecimal( (BigInteger) value );
            } else if( value instanceof Number ) {
                ret = new BigDecimal( ((Number)value).doubleValue());
            } else if( value instanceof String ) {
                
                String val = ((String) value).trim();
                
                DecimalFormat decimalFormat = new DecimalFormat();
                decimalFormat.setParseBigDecimal(true);
                ParsePosition pos = new ParsePosition(0);
                ret = (BigDecimal) decimalFormat.parse(val, pos);
                
                // ignores strings linke "12,00a"
                if(pos.getIndex() < val.length()) {
                    ret = null;
                }
                
            } else {
                throw new ClassCastException("Not possible to coerce ["+value+"] from class "+value.getClass()+" into a BigDecimal.");
            }
        }
        if(decimals != null) {
            return ret.setScale(decimals, RoundingMode.HALF_UP);
        }else {
            return ret;
        }
    }
    
    public static void setValueExt(AbstractField field, Object value) {
    if (field.isReadOnly()) {
        field.setReadOnly(false);
        field.setValue(value);
        field.setReadOnly(true);
    } else {
        field.setValue(value);
    }
    }
    
    /**
     * 
     * @param tab
     * @param sheet
     * @return true if given tab id is selected
     */
    public static final boolean isSelectedTab(Integer tab, TabSheet sheet) {
        return sheet.getTab(tab).equals(sheet.getTab(sheet.getSelectedTab()));
    }

    /**
     * 
     * @param tab
     * @param sheet
     * @return true if given tab is selected
     */
    public static final boolean isSelectedTab(Tab tab, TabSheet sheet) {
        return tab.equals(sheet.getTab(sheet.getSelectedTab()));
    }

    /**
     * 
     * @param tab
     * @param event
     * @return true if given tab id is selected
     */
    public static final boolean isSelectedTab(Integer tab, SelectedTabChangeEvent event) {
        return isSelectedTab(tab, event.getTabSheet());
    }

    /**
     * 
     * @param tab
     * @param event
     * @return true if given tab is selected
     */
    public static final boolean isSelectedTab(Tab tab, SelectedTabChangeEvent event) {
        return isSelectedTab(tab, event.getTabSheet());
    }
    
    /**
     * Gets the selected Tab position
     * @param tabSheet
     * @return
     */
    public static final int getSelectedTabIndex(TabSheet tabSheet) {
        int index = -1;
        if (tabSheet.getComponentCount() > 0) {
            index = tabSheet.getTabPosition(tabSheet.getTab(tabSheet
                    .getSelectedTab()));
        }
        return index;
    }
    
    public static LocalDate plusWeekDays(LocalDate date, int days) {
        int i=0;
        while(i<days)
        {
            date = date.plusDays(1);
            if(date.getDayOfWeek()<=5){
                i++;
            }

        }
        
        return date;
    }
    
    public static LocalDate minusWeekDays(LocalDate date, int days) {
        int i=0;
        while(i<days)
        {
            date = date.minusDays(1);
            if(date.getDayOfWeek()<=5){
                i++;
            }

        }
        
        return date;
    }
    
    public static final String formatSqlDate(LocalDate date){
        return formatSqlDate(date.toDate());
    }
    
    public static final String formatSqlDate(LocalDateTime dateTime){
        return formatSqlDate(dateTime.toDate());
    }
    
    public static final String formatSqlDate(Date date){
        
        if(date == null) {
            return null;
        }
        
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
        
    }
    
    public static final String formatSqlValue(Object value) {
        return value == null ? "NULL" : "'" + value.toString().replaceAll("'", "\'") + "'";
    }
    
    public static final LocalDate getWeekMonday(Date date, Integer weekOffset){
        return getWeekMonday(new LocalDate(date), weekOffset);
    }
    
    public static final LocalDate getWeekMonday(LocalDate date, Integer weekOffset){
        
        return date.plusWeeks(weekOffset).withDayOfWeek(DateTimeConstants.MONDAY);
        
    }
    
    public static final String printMapToXML(Map map) {
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XMLEncoder xmlEncoder = new XMLEncoder(bos);
        xmlEncoder.writeObject(map);
        xmlEncoder.flush();
        xmlEncoder.close();

        return bos.toString();

    }
    
    public static final IndexedContainer convertIndex(Container con, Object newIndexProperty) {
        
        IndexedContainer indexedContainer = new IndexedContainer();
        
        for (Object cpid : con.getContainerPropertyIds()) {
            indexedContainer.addContainerProperty(cpid, con.getType(cpid), null);
        }
        
        for (Object id : con.getItemIds()) {
            
            Item item = con.getItem(id);
            
            Item indexedItem = indexedContainer.addItem(item.getItemProperty(newIndexProperty).getValue());
            
            for (Object pid : item.getItemPropertyIds()) {
                
                indexedItem.getItemProperty(pid).setValue(item.getItemProperty(pid).getValue());
                
            }
            
        }
        
        return indexedContainer;
        
    }

    public static IndexedContainer cloneContainer(IndexedContainer original) {
        
        Collection<Filter> filters = original.getContainerFilters();
        original.removeAllContainerFilters();
        IndexedContainer clone = new IndexedContainer();

        for (Object pid : original.getContainerPropertyIds()) {
            clone.addContainerProperty(pid, original.getType(pid), null);
        }
        
        for (Object iid : original.getItemIds()) {
            
            clone.addItem(iid);
            for (Object pid : original.getContainerPropertyIds()) {
                clone.getContainerProperty(iid, pid).setValue(original.getContainerProperty(iid, pid).getValue());
            }
            
        }
        
        for (Filter filter : filters) {
            original.addContainerFilter(filter);
        }
        
        return clone;
        
    }
    
    public static Item getItemFromContainer(Container con, String propertyId, Object propertyValue) {
        
        Item item = null;
        
        for (Object iid : con.getItemIds()) {
            if(equalsWithNulls(con.getItem(iid).getItemProperty(propertyId).getValue(), propertyValue)){
                item = con.getItem(iid);
                break;
            }
        }
        return item;
    }
    
    public static <T> T getValueFromItem(Item item, Object propertyId, Class<T> type){
        
        Object res = null;
        
        if(item != null && item.getItemProperty(propertyId) != null && item.getItemProperty(propertyId).getValue() != null) {
            res = item.getItemProperty(propertyId).getValue();
        }
        
        return type.cast(res);
        
    }
    
    public static Item getFirstItemFromContainer(Container con) {
        Item item = null;
        
        if(con != null && con.size() > 0) {
            item = con.getItem(con.getItemIds().iterator().next());
        }
        
        return item;
    }
    
    public static <T> T getFirstValueFromContainer(Container con, Object propertyId, Class<T> type) {
        
        return getValueFromItem(getFirstItemFromContainer(con), propertyId, type);
        
    }
    
    public static Collection<String> wrapStringCollection(Collection<String> coll, final String character){
        return Collections2.transform(coll, new Function<String, String>() {

            @Override
            public String apply(String from) {
                return character + from + character;
            }
            
        });
    }
    
    public static String formatItem(Item item) {
        
        if(item == null) {
            return null;
        }
        
        String res = "";
            
        for (Object pid : item.getItemPropertyIds()) {
            res += "[" + pid + " = " + item.getItemProperty(pid).getValue() + "] ";
        }
        
        return res;
    }
    
    public static Boolean checkItemIntegrity(Item a, Item b) {
        
        if(a == null || b == null) {
            return false;
        }
        
        Boolean ok = true;
        
        try {
            
            ArrayList<Object> aList = new ArrayList<Object>(a.getItemPropertyIds());
            aList.removeAll(b.getItemPropertyIds());
            ArrayList<Object> bList = new ArrayList<Object>(b.getItemPropertyIds());
            bList.removeAll(a.getItemPropertyIds());
            
            if(aList.size() > 0 || bList.size() > 0) {
                System.out.println("property ids for items are different. a-b: "+aList+", b-a: "+ bList);
                return false;
            }
            
            
            for (Object pid : a.getItemPropertyIds()) {
                if(!equalsWithNulls(a.getItemProperty(pid).getValue(), b.getItemProperty(pid).getValue())) {
                    ok = false;
                    System.out.println(pid +" is not the same for both items. a: "+a.getItemProperty(pid).getValue()+", b: " + b.getItemProperty(pid).getValue());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ok = false;
        }
        
        return ok;
    }
}









