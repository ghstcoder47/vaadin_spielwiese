package archenoah.lib.custom.comments;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.vaadin.Components.MultiRowData.MultiRowDataComponent;
import archenoah.lib.vaadin.Components.MultiRowData.MultiRowInsertListener;
import archenoah.lib.vaadin.Components.MultiRowData.MultiRowSaveListener;
import archenoah.lib.vaadin.Language.i18n.I18nCB;
import archenoah.lib.vaadin.Language.i18n.I18nManager;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.CustomTable;
import com.vaadin.ui.CustomTable.ColumnGenerator;

public class CommentComponent extends MultiRowDataComponent{

    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
    
    // {section fields}
    // ****************
    
    private CommentInput input;
    private Filter originFilter;
    private Filter idFilter;
    
    private Integer typeId;

    private I18nManager i18n;

    private final static class caption extends I18nCB {
        static final I18nCB wrapper = set();
    }
    
    // {end fields}

    // {section constructors}
    // **********************
    public CommentComponent() {
        super();
        
        i18n = new I18nManager(this);
        
        input = new CommentInput();
        input.setAuthor(MyUtils.getUserData().getUserId());
        
        addGeneratedColumn("wrapper", i18n.get(caption.wrapper), new ColumnGenerator() {
            
            @Override
            public Object generateCell(CustomTable source, Object itemId, Object columnId) {
                CommentWrapper wrapper = new CommentWrapper();
                wrapper.setOriginConverter(source.getConverter("C_ID_ORIGIN"));
                wrapper.setAuthorConverter(source.getConverter("C_ID_AUTHOR"));
                wrapper.setValue(source.getItem(itemId));
                wrapper.setReadOnly(true);
                
                return wrapper;
            }
        });
        
        setSaveListener(getSaveListener());
        setInsertListener(getInsertListener());
        setContainerSort(new Object[] {"C_ID"}, new boolean[] {false});
        prependNewItems(true);
        
        allowRead(false);
        allowDelete(false);
        
        setup(
                "cust_comments"
                , "C_ID"
                , "C_ID_MAIN"
                , input);
        setVisibleColumns(new Object[] {"wrapper"});
        setParentId(0);
        
    }

    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    public void setOrigin(Class origin) {
        if(origin != null) {
            setOrigin(origin.getSimpleName());
        }else {
            setOrigin((String) null);
        }
    }
    
    public void setOrigin(String originString) {
        input.setOrigin(originString);
    }
    
    public void setType(String typeString) {
        input.setType(typeString);
    }
    
    public void setDataId(Integer dataId) {
        input.setData(dataId);
    }
    
    public void filterOrigin(Class origin) {
        if(origin != null) {
            filterOriginString(origin.getSimpleName());
        }else {
            filterOriginString(null);
        }
    }
    
    public void filterOriginString(final String originString) {
        
        removeContainerFilter(originFilter);
        if(originString == null) {
            return;
        }
        
        final Integer originId = input.getOriginConverter().convertToModel(originString, Integer.class);
        
        originFilter = new Filter() {
            
            @Override
            public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
                return MyUtils.equalsWithNulls(MyUtils.getValueFromItem(item, "C_ID_ORIGIN", Integer.class), originId);
            }
            
            @Override
            public boolean appliesToProperty(Object propertyId) {
                return "C_ORIGIN".equals(propertyId);
            }
        };
        
        addContainerFilter(originFilter);
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
                return MyUtils.equalsWithNulls(MyUtils.getValueFromItem(item, "C_DATA", Integer.class), dataId);
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
