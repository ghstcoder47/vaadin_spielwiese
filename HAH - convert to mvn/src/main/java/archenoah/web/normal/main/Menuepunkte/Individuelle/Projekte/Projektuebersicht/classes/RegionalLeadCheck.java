package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.classes;

import java.text.DateFormat;
import java.util.Date;
import java.util.Map;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.comunication.dbclass.DBClass.CustomQuery;
import archenoah.lib.vaadin.Components.Steppers.IntStepper;
import archenoah.web.normal.UserInfo.UserData;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.DateField;

public class RegionalLeadCheck extends CheckBox{


    // {section fields}
    // ****************
    
    public enum FieldType {
        
        DATE,
        USER,
        CHECK
        
    }
    
    private DateField date;
    private IntStepper user;
 
     // {end fields}

    // {section constructors}
    // **********************
    public RegionalLeadCheck() {
        date = new DateField();
        user = new IntStepper(); 
    }
    
    public void init() {
        configureComponents();
        updateTooltip();
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    
    // {end gettersandsetters}
    
    // {section publicmethods}
    // ***********************

    public void addToDBMap(Map<Object, String> map, FieldType type, String column) {
        
        if(map == null) {
            return;
        }
        
        Object field = null;
        switch (type) {
        case CHECK:
            field = this;
            break;
        case DATE:
            field = date;
            break;
        case USER:
            field = user;
            break;

        default:
            break;
            
        }
        
        if(field != null) {
            map.put(field, column);
        }
        
    }
    

    // {end publicmethods}

    // {section privatemethods}
    // ************************
    private void configureComponents() {
        
        ValueChangeListener vcl = new ValueChangeListener() {
            
            @Override // thanks organize imports
            public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
                
                boolean changed = false;
                
                if(getValue()) {
                    
                    if(date.getValue() == null) {
                        date.setValue(new Date());
                        changed = true;
                    }
                    
                    if(user.getValue() == null) {
                        user.setValue(UserData.get().getUserId());
                        changed = true;
                    }
                }else {
                    date.setValue(null);
                    user.setValue(null);
                    changed = true;
                }
                
                
                
                if(changed) {
                    updateTooltip();
                }
            }

        };
        
        addValueChangeListener(vcl);
        
    }
    
    private void updateTooltip() {
        
        if(user.getValue() == null) {
            setDescription(null);
            return;
        }
        
        Date td = date.getValue();
        if(td == null) {
            setDescription(null);
            return;
        }
        
        String formatted = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(td);
        setDescription(dbGetUser(user.getValue()) + "<br />" + formatted);
        
    }
    
    // {end privatemethods}

    // {section database}
    // ******************
    private String dbGetUser(Integer userId) {
       CustomQuery q = new DBClass().CustomQuery;
       String sql = "select CONCAT(AUL_VORNAME, ' ', AUL_NAME) as user from cms_auth_stammdaten_user where AUL_ID = " + userId;
       q.setSqlString(sql);
       
       return MyUtils.getValueFromItem(MyUtils.getFirstItemFromContainer(q.query()), "user", String.class);
       
    }
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
