package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektzuweisung.Form.classes;

import java.math.BigDecimal;
import java.util.HashMap;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.comunication.dbclass.DBClass.CustomQuery;
import archenoah.lib.vaadin.Components.MultiRowData.MultiRowDataComponent;
import archenoah.lib.vaadin.Components.MultiRowData.MultiRowDataField;
import archenoah.lib.vaadin.Components.MultiRowData.MultiRowDataFieldRows;
import archenoah.lib.vaadin.Components.Steppers.IntStepper;

import com.vaadin.data.Container;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.Validator;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.ComboBox;

@SuppressWarnings("serial")
public class MetaProductsComponent extends MultiRowDataComponent{

    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
    
    // {section fields}
    // ****************
    private IntStepper project;
    private ComboBox product;
    private IntStepper count;
    
    MultiRowDataField projectField;
    MultiRowDataField productField;
    MultiRowDataField countField;
    
    // {end fields}

    // {section constructors}
    // **********************
    public MetaProductsComponent() {
        
        super();

        project = new IntStepper();
        project.setCaption("Projekt");
        project.setRequired(true);
        project.setWidth("140px");
        project.setVisible(false);
        projectField = new MultiRowDataField(project, "PMP_ID_PROJECT", Integer.class);
        projectField.isParent(true);
        
        product = new ComboBox();
        product.setCaption("Produkt");
        product.setRequired(true);
        product.setWidth("100%");
        product.setNullSelectionAllowed(false);
        productField = new MultiRowDataField(product, "PMP_ID_PRODUCT", String.class);
        productField.setDataSource(dbGetProducts(), "PP_ID", "PP_PRODUCT");
        productField.setFieldRatio(1);
        
        count = new IntStepper();
        count.setCaption("Menge");
        count.setMinValue(1);
        count.setRequired(true);
        count.setWidth("80px");
        count.addValidator(new Validator() {
            
            @Override
            public void validate(Object value) throws InvalidValueException {
                
                if((Integer) value < 1) {
                    throw new InvalidValueException("Ungültige Menge");
                }
                
            }
        });
        countField = new MultiRowDataField(count, "PMP_COUNT", Integer.class);
        countField.setFieldRatio(0);
        countField.setDefaultValue(0);
        
        setProductFilter();
        
    }
    
    public void setup(Integer projectId, Integer metaId) {
        // init multirow
        MultiRowDataFieldRows rows = new MultiRowDataFieldRows();
        rows.addFieldRow(projectField, productField, countField);
        
        log.info("setup: pid: {}, mid {}", projectId, metaId);
        
        setup(
                "cust_protokoll_meta_products"
                , "PMP_ID"
                , "PMP_ID_META"
                , rows);
        setVisibleColumns(new Object[] {"PMP_ID_PRODUCT", "PMP_COUNT"});
        setProjectId(projectId);
        setParentId(metaId);
    }
    
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    public void setProjectId(Integer projectId) {
        project.setValue(projectId);
        setProductFilter();
    }
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    public static HashMap<Integer, Integer> getProductMapFor(Integer projectId, Integer metaId){
        if(metaId == null || projectId == null) {
            throw new IllegalArgumentException("projectId & metaId must be provided");
        }
        
        return dbGetProductMap(projectId, metaId);
        
    }
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    private void setProductFilter() {
        
        IndexedContainer con = ((IndexedContainer) product.getContainerDataSource());
        con.removeAllContainerFilters();
        con.addContainerFilter(new Filter() {
            
            @Override
            public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
                return MyUtils.equalsWithNulls(project.getValue(), MyUtils.getValueFromItem(item, "PP_ID_PROJECT", Integer.class));
            }
            
            @Override
            public boolean appliesToProperty(Object propertyId) {
                return "PP_ID_PROJECT".equals(propertyId);
            }
        });

        // preselect if we only have one entry
        if(product.getItemIds().size() == 1) {
            product.setValue(product.getItemIds().iterator().next());            
        }
        
    }
    // {end privatemethods}

    // {section database}
    // ******************
    private Container dbGetProducts() {
        
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "select"
         + "\n " + "    PP_ID"
         + "\n " + "    , PP_PRODUCT"
         + "\n " + "    , PP_ID_PROJECT"
         + "\n " + "from cust_prescriptions_products";
        q.setSqlString(sql);
//        q.db.debugNextQuery(true);
        
        return q.query();
        
    }
    
    private static HashMap<Integer, Integer> dbGetProductMap(int projectId, int metaId){
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "select PMP_ID_PRODUCT, SUM(PMP_COUNT) as count"
             + "\n " + "from cust_protokoll_meta_products"
             + "\n " + "where PMP_ID_META = " + metaId
             + "\n " + "and PMP_ID_PROJECT = " + projectId
             + "\n " + "group by PMP_ID_PRODUCT";
        q.setSqlString(sql);
        
        HashMap<Integer, Integer> productMap = new HashMap<Integer, Integer>();
        
        Container con = q.query();
        if(con != null && con.size() > 0) {
            for (Object iid : con.getItemIds()) {
                Item item = con.getItem(iid);
                
                BigDecimal bd = MyUtils.getValueFromItem(item, "count", BigDecimal.class);
                if(bd == null) {
                    continue;
                }
                
                Integer count = bd.intValue();
                
                productMap.put(MyUtils.getValueFromItem(item, "PMP_ID_PRODUCT", Integer.class), count);
            }
        }
        
        return productMap;
    }
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
