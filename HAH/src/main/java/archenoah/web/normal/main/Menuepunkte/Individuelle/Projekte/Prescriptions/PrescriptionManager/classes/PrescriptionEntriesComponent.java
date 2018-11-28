package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Prescriptions.PrescriptionManager.classes;

import java.util.ArrayList;
import java.util.HashSet;

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
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.VerticalLayout;

public class PrescriptionEntriesComponent extends MultiRowDataComponent{

    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
    
    private VerticalLayout mainLayout;
    
    // {section fields}
    // ****************
    
    private ComboBox project;
    private ComboBox product;
    private ComboBox size;
    private IntStepper count;
    private IntStepper quantity;
    
    
    private Integer patientId;
//    MultiRowDataComponent multiRow;
    
    
    // {end fields}

    // {section constructors}
    // **********************
    public PrescriptionEntriesComponent() {
        super();

        project = new ComboBox();
        project.setCaption("Projekt");
        project.setRequired(true);
        project.setWidth("140px");
        MultiRowDataField projectField = new MultiRowDataField(project, "PLE_ID_PROJECT", Integer.class);
        projectField.setDataSource(dbGetProjects(), "PSLV_ID", "PSLV_NAME");
        projectField.setFieldRatio(0);
        projectField.setRatio(1);
        
        product = new ComboBox();
        product.setCaption("Produkt");
        product.setRequired(true);
        product.setWidth("100%");
        MultiRowDataField productField = new MultiRowDataField(product, "PLE_ID_PRODUCT", Integer.class);
        productField.setDataSource(dbGetProducts(), "PP_ID", "PP_PRODUCT");
        productField.setFieldRatio(1);
        productField.setRatio(1);
        
        size = new ComboBox();
        size.setCaption("Packungsgröße");
        size.setWidth("100%");
        MultiRowDataField sizeField = new MultiRowDataField(size, "PLE_ID_SIZE", Integer.class);
        sizeField.setDataSource(dbGetSizes(), "PPS_ID", "name");
        sizeField.setFieldRatio(1);
        sizeField.setRatio(1);
        
        count = new IntStepper();
        count.setCaption("Anzahl");
        count.setMinValue(1);
        count.setWidth("60px");
        MultiRowDataField countField = new MultiRowDataField(count, "PLE_SIZE_COUNT", Integer.class);
        countField.setFieldRatio(0);
        countField.setRatio(0);
        countField.setDefaultValue(0);
        
        quantity = new IntStepper();
        quantity.setCaption("Menge");
        quantity.setMinValue(1);
        quantity.setWidth("60px");
        quantity.setRequired(true);
        quantity.addValidator(new Validator() {
            
            @Override
            public void validate(Object value) throws InvalidValueException {
                
                if((Integer) value < 1) {
                    throw new InvalidValueException("Ungültige Menge");
                }
                
            }
        });
        MultiRowDataField quantityField = new MultiRowDataField(quantity, "PLE_QUANTITY", Integer.class);
        quantityField.setFieldRatio(0);
        quantityField.setRatio(0);
        quantityField.setDefaultValue(0);
        
        setListeners();
        setProductFilter();
        setSizeFilter();
        
//        setSaveListener(getSaveListener());
        
        // init multirow
        MultiRowDataFieldRows rows = new MultiRowDataFieldRows();
        rows.addFieldRow(projectField, productField);
        rows.addFieldRow(sizeField, countField, quantityField);
        
        setup(
                "cust_prescriptions_list_entries"
                , "PLE_ID"
                , "PLE_PL_ID"
                , rows);
        setVisibleColumns(new Object[] {"PLE_ID_PRODUCT", "PLE_ID_SIZE", "PLE_SIZE_COUNT", "PLE_QUANTITY"});
//        mainLayout.addComponent(multiRow);
//        setSizeFull();
        setParentId(0);
        

    }

    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    public void setPatient(Integer patientId) {
        this.patientId = patientId;
    }
    
    public void setProject(Integer projectId) {
        setProjectFilter(projectId); 
        project.setValue(projectId);
    }
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    public boolean removeAllEntries() {
        
        Boolean success = true;
        
        try {
            // needs to be copied or iteration stops after removing one item
            ArrayList<Object> iids = new ArrayList<Object>(getContainer().getItemIds());
            for (Object iid : iids) {
                log.info("iid: {}", iid);
                removeEntry(iid);
            }
            save();
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        
        return success;

    }
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    
//    private MultiRowSaveListener getSaveListener() {
//        
//        return new MultiRowSaveListener() {
//            
//            @Override
//            public void onSave(ArrayList<Item> insertedItems, ArrayList<PropertysetItem> deletedItems) {
//                
//                if(patientId == null) {
//                    throw new IllegalArgumentException("patientId not set!");
//                }
//                
//                PrescriptionBalance balance = new PrescriptionBalance();
//                balance.setPatientId(patientId);
//                
//                HashMap<Integer, Integer> deltas = new HashMap<Integer, Integer>();
//                
//                for (Item insertItem : insertedItems) {
//                    
//                    Integer product = MyUtils.getValueFromItem(insertItem, "PLE_ID_PRODUCT", Integer.class);
//                    Integer quantity = MyUtils.getValueFromItem(insertItem, "PLE_QUANTITY", Integer.class);
//                    
//                    deltas.put(product, quantity + (deltas.get(product) != null ? deltas.get(product) : 0));
//                    
//                }
//                
//                for (Item deleteItem : deletedItems) {
//                    
//                    Integer product = MyUtils.getValueFromItem(deleteItem, "PLE_ID_PRODUCT", Integer.class);
//                    Integer quantity = MyUtils.getValueFromItem(deleteItem, "PLE_QUANTITY", Integer.class);
//                    
//                    deltas.put(product, (-1 * quantity) + (deltas.get(product) != null ? deltas.get(product) : 0));
//                    
//                }
//                
//                log.info("deltas: {}", deltas);
//
//                for (Entry<Integer, Integer> delta : deltas.entrySet()) {
//                    balance.editBalance(delta.getKey(), delta.getValue(), PrescriptionBalance.PRESCRIPTION, null, Objects.toString(getParentId()));
//                }
//                
//            }
//        };
//        
//    }
    
    private void setListeners() {
        
        project.addValueChangeListener(new ValueChangeListener() {
            
            @Override
            public void valueChange(ValueChangeEvent event) {
                
                setProductFilter();
                setSizeFilter();
                
            }
        });
        
        product.addValueChangeListener(new ValueChangeListener() {
            
            @Override
            public void valueChange(ValueChangeEvent event) {
                
                setSizeFilter();
                
            }
        });
        
        
        ValueChangeListener sizeListener = new ValueChangeListener() {
            
            @Override
            public void valueChange(ValueChangeEvent event) {
                
                Boolean hasSize = size.getValue() != null;
                
                count.setNullValueAllowed(!hasSize);
                count.setValue(hasSize ? 0 : null);
                quantity.setValue(0);
                
                count.setEnabled(hasSize);
                count.setStyleName(hasSize ? "" : "nogrey");
                quantity.setEnabled(!hasSize);
                quantity.setStyleName(hasSize ? "nogrey" : "");
                
            }
        };
        
        project.addValueChangeListener(sizeListener);
        size.addValueChangeListener(sizeListener);
        product.addValueChangeListener(sizeListener);
        
        count.addValueChangeListener(new ValueChangeListener() {
            
            @Override
            public void valueChange(ValueChangeEvent event) {
                
                Boolean hasSize = size.getValue() != null;
                if(hasSize) {
                    
                    Integer countSize = MyUtils.getValueFromItem(size.getItem(size.getValue()),
                        "PPS_SIZE", Integer.class);
                    
                    if(countSize != null) {
                        quantity.setValue(count.getValue() * countSize);
                    }
                    
                    
                }
                
            }
        });
        

    }
    
    private void setProjectFilter(final Integer projectId) {
        
        IndexedContainer con = ((IndexedContainer) project.getContainerDataSource());
        con.removeAllContainerFilters();
        con.addContainerFilter(new Filter() {
            
            @Override
            public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
                Integer id = MyUtils.getValueFromItem(item, "PSLV_ID", Integer.class);
                return MyUtils.equalsWithNulls(projectId, id) || MyUtils.equalsWithNulls(1000, id);
            }
            
            @Override
            public boolean appliesToProperty(Object propertyId) {
                return "PSLV_ID".equals(propertyId);
            }
        });

    }
    
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
    
    private void setSizeFilter() {
        size.setValue(null);
        IndexedContainer con = ((IndexedContainer) size.getContainerDataSource());
        con.removeAllContainerFilters();
        
        final HashSet<String> countries = MyUtils.getUserData().getCountry().getCountries();
        
        con.addContainerFilter(new Filter() {
            
            @Override
            public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {

                return MyUtils.equalsWithNulls(product.getValue(), MyUtils.getValueFromItem(item, "PPS_ID_PRODUCT", Integer.class)) 
                    && (countries.size() > 0 ? countries.contains(MyUtils.getValueFromItem(item, "PPS_COUNTRY", String.class)) : true);
            }
            
            @Override
            public boolean appliesToProperty(Object propertyId) {
                return "PPS_ID_PRODUCT".equals(propertyId);
            }
        });
    }
    
    // {end privatemethods}

    // {section database}
    // ******************
    
    private Container dbGetProjects() {
        
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "select"
         + "\n " + "    PSLV_ID"
         + "\n " + "    , PSLV_NAME"
         + "\n " + "from cust_projekte_stammdaten_liste"
         + "\n " + "order by PSLV_NAME";
        q.setSqlString(sql);
//        q.db.debugNextQuery(true);
        
        return q.query();
        
    }
    
    private Container dbGetProducts() {
        
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "select"
         + "\n " + "    PP_ID"
         + "\n " + "    , PP_PRODUCT"
         + "\n " + "    , PP_ID_PROJECT"
         + "\n " + "from cust_prescriptions_products"
         + "\n " + "order by PP_PRODUCT";
        q.setSqlString(sql);
//        q.db.debugNextQuery(true);
        
        return q.query();
        
    }
    
    private Container dbGetSizes() {
        
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "select"
         + "\n " + "    PPS_ID"
         + "\n " + "    , PPS_ID_PRODUCT"
         + "\n " + "    , PPS_SIZE"
         + "\n " + "    , PPS_COUNTRY"
         + "\n " + "    , CONCAT('[', PPS_COUNTRY, '] ', PPS_NAME, ' [', PPS_SIZE, ']' ) as name"
         + "\n " + "from cust_prescription_product_sizes"
         + "\n " + "order by PPS_COUNTRY, PPS_NAME, PPS_SIZE";
        q.setSqlString(sql);
//        q.db.debugNextQuery(true);
        
        return q.query();
        
    }
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
    
}
