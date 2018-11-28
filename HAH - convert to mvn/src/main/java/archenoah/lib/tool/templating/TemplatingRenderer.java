package archenoah.lib.tool.templating;

import java.io.OutputStream;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import archenoah.lib.tool.comunication.dbclass.DBClass;

import com.vaadin.data.Item;


public class TemplatingRenderer {
    
    // {section fields}
    // ****************
    JtwigTemplate template;
    JtwigModel model;
    // {end fields}

    // {section constructors}
    // **********************
    public TemplatingRenderer() {
        template = JtwigTemplate.inlineTemplate("[err_no_template_set]");
        initNewModel();
    }
    
    public TemplatingRenderer(String id) {
        loadTemplate(id);
        initNewModel();
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    public String render() {
        return template.render(model);
    }
    
    public void render(OutputStream outputStream) {
        template.render(model, outputStream);
    }
    
    public void loadTemplate(String id) {
        template = JtwigTemplate.inlineTemplate(dbGetTemplate(id));
    }
    
    public void clearModel() {
        initNewModel();
    }
    
    public void with(String name, Object value) {
        model.with(name, value);
    }
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    private void initNewModel() {
        model = JtwigModel.newModel();
    }
    
    // {end privatemethods}

    // {section database}
    // ******************
    
    private String dbGetTemplate(String id) {
        
        String template = "[err_no_template_found: " + id + "]";
        
        String table = "cms_templating";
        
        DBClass db = new DBClass();
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Einzeln(table);
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_All();
        db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein(table, "CT_ID", "=", "'"+id+"'", "");
        
        Item item = db.DB_Data_Get.DB_SEND_AND_GET_FIRST_ITEM();
        
        if(item != null) {
            try {
                template = (String) item.getItemProperty("CT_TEMPLATE").getValue();
            } catch (Exception e) {
                template = "[err_reading_template]";
            }
            
        }
        
        return template;
        
    }
    
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
