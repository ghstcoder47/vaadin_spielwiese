package archenoah.lib.vaadin.Language.i18n;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.comunication.dbclass.DBClass.CustomQuery;
import archenoah.lib.vaadin.CustomConverterFactorys.DataviewConverter;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.UI;

public class I18nConverter implements DataviewConverter<Object>{
    
    // {section fields}
    // ****************
    
    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(I18nConverter.class);
    
    final public static String CAPTION_PROPERTY = "i18n_caption";
    
    private String table;
    private String value;
    private String caption;
    
    private Container baseContainer; 
    private IndexedContainer localizedContainer;
    
    HashMap<String, String> localizedCaptions = new HashMap<String, String>();
    
    private HashMap<Object, String> modelPresentationMap = new HashMap<Object, String>();
    private HashMap<String, Object> presentationModelMap = new HashMap<String, Object>();
    
    // {end fields}

    // {section constructors}
    // **********************
    /**
     * 
     * @param table the name of the table
     * @param value the column containing the value attribute
     */
    public I18nConverter(String table, String value) {
        this.table = table;
        this.value = value;
        init();
    }
    
    /**
     * 
     * @param table the name of the table
     * @param value the column containing the value of the attribute
     * @param caption the column containing the raw caption name
     */
    public I18nConverter(String table, String value, String caption) {
        this.table = table;
        this.value = value;
        this.caption = caption;
        init();
    }
    
    public void init() {
        dbGetContainer();
        getLocalizedContainer();
    }
    
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    @Override
    public Class<Object> getModelType() {
        return Object.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    
    public String getLocalizedCaption(String rawCaption) {
        return localizedCaptions.get(rawCaption);
    }
    
    public String getIdCaption(Object id) {
        return modelPresentationMap.get(id);
    }
    
    /**
     * backwards compatibility for LngConverter migration
     * @param value
     * @return
     */
    @Deprecated
    public String getLngText(String value) {
        return getIdCaption(value);
    }
    
    public String getRawFromIdConverter(String value) {
        if(value == null) {
            return null;
        }
        
        for (Object iid : baseContainer.getItemIds()) {
            String raw = MyUtils.getValueFromItem(baseContainer.getItem(iid), caption, String.class);
            if(value.equals(raw)) {
                return getLocalizedCaption(raw);
            }
        }
        
        return null;
    }
    
    @Override
    public void attachDatasource(AbstractSelect select){
        
        select.setContainerDataSource(getLocalizedContainer());
        select.setItemCaptionPropertyId(CAPTION_PROPERTY);
        
    }
    
    /**
     * Use for persistent Container that is attached as a single Datasource only<  <br />
     * If the same container values are to be used by multiple components, use generateLocalizedContainer instead
     * to prevent IllegalStateExceptions
     * @return
     */
    public IndexedContainer getLocalizedContainer(){
        
        if(localizedContainer == null) {
            localizedContainer = generateLocalizedContainer();
        }
                
        return localizedContainer;
        
    }
    
    @SuppressWarnings("unchecked")
    public IndexedContainer generateLocalizedContainer() {
        
        Container con = baseContainer;
        IndexedContainer lcon = new IndexedContainer();
        lcon.addContainerProperty(CAPTION_PROPERTY, String.class, "err_no_caption");
        
        if (con != null) {

            for (Object pid : con.getContainerPropertyIds()) { // add other propertyids for data handling
                lcon.addContainerProperty(pid, con.getType(pid), null);
            }

            for (Object cid : con.getItemIds()) {
                
                Item item = con.getItem(cid);
                
                Object iid = MyUtils.getValueFromItem(item, value, Object.class);
                Object rco = caption != null ? MyUtils.getValueFromItem(baseContainer.getItem(cid), caption, Object.class): iid;
                lcon.addItem(iid);
                if(rco != null) {
                    modelPresentationMap.put(iid, rco.toString());
                }

                for (Object pid : con.getContainerPropertyIds()) {
                    try {
                        lcon.getContainerProperty(iid, pid).setValue(con.getContainerProperty(cid, pid).getValue());
                    } catch (Exception e) {
                    }
                }

            }
            
            localizedCaptions = new I18nManager(table, modelPresentationMap.values()).getLocalizedCaptions();
            for (Object key : modelPresentationMap.keySet()) {
                modelPresentationMap.put(key, localizedCaptions.get(modelPresentationMap.get(key)));
            }

            for(Entry<Object, String> entry : modelPresentationMap.entrySet()){
                presentationModelMap.put(entry.getValue(), entry.getKey());
            }
            
            for (Object iid : lcon.getItemIds()) {
                lcon.getContainerProperty(iid, CAPTION_PROPERTY).setValue(
                    modelPresentationMap.get(lcon.getContainerProperty(iid, value).getValue()));
            }
            
        }
        
        return lcon;
    }
    
    @Override
    public Object convertToModel(String value, Class<? extends Object> targetType, Locale locale)
        throws com.vaadin.data.util.converter.Converter.ConversionException {
        return presentationModelMap.get(value);
    }

    @Override
    public String convertToPresentation(Object value, Class<? extends String> targetType, Locale locale)
        throws com.vaadin.data.util.converter.Converter.ConversionException {
        return modelPresentationMap.get(value);
    }
    
    @SuppressWarnings("unchecked")
    public <T> T convertToModel(String value, Class<? extends T> targetType)
        throws com.vaadin.data.util.converter.Converter.ConversionException {
        return (T) convertToModel(value, targetType, UI.getCurrent().getLocale());
    }

    public String convertToPresentation(Object value, Class<? extends String> targetType)
        throws com.vaadin.data.util.converter.Converter.ConversionException {
        return convertToPresentation(value, targetType, UI.getCurrent().getLocale());
    }
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    // {end privatemethods}

    // {section database}
    // ******************
    private void dbGetContainer(){
        
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "select * from " + table;
        q.setSqlString(sql);
        baseContainer = q.query();
        
    }
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
    


}
