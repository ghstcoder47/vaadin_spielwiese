package archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.Patientenverwaltung.classes;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.vaadin.Components.MultiRowData.MultiRowDataComponent;
import archenoah.lib.vaadin.Components.MultiRowData.MultiRowInsertListener;
import archenoah.lib.vaadin.Components.MultiRowData.MultiRowSaveListener;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.CustomTable;
import com.vaadin.ui.CustomTable.ColumnGenerator;

public class PatientCommentComponent extends MultiRowDataComponent{

    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
    
    // {section fields}
    // ****************
    
    private PatientCommentInput input;
    private Filter typeFilter;
    private Filter idFilter;

    // {end fields}

    // {section constructors}
    // **********************
    public PatientCommentComponent() {
        super();

        input = new PatientCommentInput();
        input.setAuthor(MyUtils.getUserData().getUserId());
        
        addGeneratedColumn("wrapper", "Kommentare", new ColumnGenerator() {
            
            @Override
            public Object generateCell(CustomTable source, Object itemId, Object columnId) {
                PatientCommentWrapper wrapper = new PatientCommentWrapper();
                wrapper.setTypeConverter(source.getConverter("PC_TYPE"));
                wrapper.setAuthorConverter(source.getConverter("PC_ID_AUTHOR"));
                wrapper.setValue(source.getItem(itemId));
                wrapper.setReadOnly(true);
                
                return wrapper;
            }
        });
        
        setSaveListener(getSaveListener());
        setInsertListener(getInsertListener());
        setContainerSort(new Object[] {"PC_ID"}, new boolean[] {false});
        prependNewItems(true);
        
        allowRead(false);
        allowDelete(false);
        
        setup(
                "cust_patient_comments"
                , "PC_ID"
                , "PC_ID_PATIENT"
                , input);
        setVisibleColumns(new Object[] {"wrapper"});
        setParentId(0);
        

    }

    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    public void setType(Class type) {
        if(type != null) {
            setType(type.getSimpleName());
        }else {
            setType((String) null);
        }
    }
    
    public void setType(String typeString) {
        input.setType(typeString);
    }
    
    public void setDataId(Integer dataId) {
        input.setData(dataId);
    }
    
    public void filterType(Class type) {
        if(type != null) {
            filterTypeString(type.getSimpleName());
        }else {
            filterTypeString(null);
        }
    }
    
    public void filterTypeString(final String typeString) {
        
        removeContainerFilter(typeFilter);
        if(typeString == null) {
            return;
        }
        
        typeFilter = new Filter() {
            
            @Override
            public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
                return MyUtils.equalsWithNulls(MyUtils.getValueFromItem(item, "PC_TYPE", String.class), typeString);
            }
            
            @Override
            public boolean appliesToProperty(Object propertyId) {
                return "PC_TYPE".equals(propertyId);
            }
        };
        
        addContainerFilter(typeFilter);
    }
    
    public void filterDataId(final Integer dataId) {
        
        log.info("dataId: {}", dataId);
        
        removeContainerFilter(idFilter);
        if(dataId == null) {
            return;
        }
        
        idFilter = new Filter() {
            
            @Override
            public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
                return MyUtils.equalsWithNulls(MyUtils.getValueFromItem(item, "PC_DATA", Integer.class), dataId);
            }
            
            @Override
            public boolean appliesToProperty(Object propertyId) {
                return "PC_DATA".equals(propertyId);
            }
        };
        
        addContainerFilter(idFilter);
    }
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    
    @Override
    public void allowAdd(boolean allowed) {
        super.allowAdd(allowed);
    }
    
    @Override
    public void addTopComponent(AbstractComponent component) {
        super.addTopComponent(component);
    }
    
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    
    private MultiRowSaveListener getSaveListener() {
        return null;
    }
    
    private MultiRowInsertListener getInsertListener() {
        return new MultiRowInsertListener() {
            
            @Override
            public void onInsert(Item insertedItem) {
                saveAndUpdate();
            }
        };
    }
    
    // {end privatemethods}

    // {section database}
    // ******************
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
    
}
