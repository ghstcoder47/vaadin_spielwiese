package archenoah.lib.vaadin.Components.MultiRowData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import archenoah.lib.custom.MyUtils;

import com.vaadin.data.Container;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractSelect;

@SuppressWarnings("rawtypes")
public class MultiRowDataField {
    
    // {section fields}
    // ****************
    public Class type;
    
    private AbstractField field;
    private String column;
    
    private Object defaultValue;
    
    private Integer ratio;
    private Integer fieldRatio;
    
    private boolean isParent = false;
    
    private Container dataSource;
    private Object idProperty;
    private Object captionProperty;
    private Collection<Object> additionalColumns;
    private Converter<String, ?> converter;
    private MultiRowDataFieldConverter fieldConverter;

    // {end fields}

    // {section constructors}
    // **********************
    /**
     * 
     * @param field the input field
     * @param table the database table for this value
     * @param column the database field for this value
     */
    public MultiRowDataField(AbstractField field, String column) {
        this(field, column, field.getType());
    }
    
    public MultiRowDataField(AbstractField field, String column, Class type) {
        this.field = field;
        this.column = column;
        this.type = type;
    }
    
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    /**
     * 
     * @return the input field
     */
    public AbstractField getField() {
        return field;
    }

    /**
     * 
     * @return the database column
     */
    public String getColumn() {
        return column;
    }
    
    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * @return the converter
     */
    public Converter<String, ?> getConverter() {
        return converter;
    }

    /**
     * @param converter the converter to set
     */
    public void setConverter(Converter<String, ?> converter) {
        this.converter = converter;
    }

    /**
     * @return the fieldConverter
     */
    public MultiRowDataFieldConverter getFieldConverter() {
        return fieldConverter;
    }

    /**
     * @param fieldConverter the fieldConverter to set
     */
    public void setFieldConverter(MultiRowDataFieldConverter fieldConverter) {
        this.fieldConverter = fieldConverter;
    }

    /**
     * @return the table expandratio
     */
    public Integer getRatio() {
        return ratio;
    }

    /**
     * @param ratio the expandratio for the table column
     */
    public void setRatio(Integer ratio) {
        this.ratio = ratio;
    }

    /**
     * @return the field expandratio
     */
    public Integer getFieldRatio() {
        return fieldRatio;
    }

    /**
     * @param ratio the expandratio for the field
     */
    public void setFieldRatio(Integer ratio) {
        this.fieldRatio = ratio;
    }
    
    /**
     * 
     * @param dataSource
     * @param idProperty
     * @param captionProperty
     */
    public void setDataSource(Container dataSource, Object idProperty, Object captionProperty) {
        this.dataSource = dataSource;
        this.idProperty = idProperty;
        this.captionProperty = captionProperty;
        processDataSource();
    }
    
    public Container getDataSource() {
        return dataSource;
    }

    public Object getIdProperty() {
        return idProperty;
    }

    public Object getCaptionProperty() {
        return captionProperty;
    }
    
    public boolean isParent() {
        return isParent;
    }
    
    public void isParent(boolean isParent) {
        this.isParent = isParent;
    }
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    @Override
    public String toString() {
        return getColumn();
    }
    
    /**
     * 
     * @return an unodifiable collection of ids in datasource that are not main id or caption
     */
    public Collection<Object> getAdditionalColumns(){
        
        return additionalColumns;
        
    }
    
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    private void processDataSource() {
        
        if(dataSource != null && field instanceof AbstractSelect) {
            
            AbstractSelect field = (AbstractSelect) this.field;
            
            field.setContainerDataSource(MyUtils.convertIndex(dataSource, idProperty));
            field.setItemCaptionPropertyId(captionProperty);
            
            
            ArrayList<Object> ids = new ArrayList<Object>();
            if(dataSource != null) {
                
                ids.addAll(dataSource.getContainerPropertyIds());
                ids.remove(idProperty);
                ids.remove(captionProperty);
               
            }
            additionalColumns = Collections.unmodifiableCollection(ids);
            
        }
    }
    // {end privatemethods}
    // {section database}
    // ******************
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
