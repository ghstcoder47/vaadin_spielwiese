package archenoah.lib.vaadin.CustomConverterFactorys;

import java.util.HashMap;
import java.util.Locale;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Embedded;

public class BooleanToIconConverter implements Converter<Embedded, Boolean> {
    
    private final HashMap<Boolean, Embedded> byModel = new HashMap<Boolean, Embedded>();
    private final HashMap<Embedded, Boolean> byPresentation = new HashMap<Embedded, Boolean>();
    
    //new ThemeResource("image-res/icons-white-16/User.png")
    //ThemeResource statusGreen = new ThemeResource("../icons/status-green.png");
//  Item row = this.addItem("first");
//  Embedded statusImage = new Embedded("status",statusGreen);
    
    public BooleanToIconConverter() {
        fillMaps();
    }

    @Override
    public Boolean convertToModel(Embedded value, Class<? extends Boolean> targetType, Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        return byPresentation.get(value);
    }

    @Override
    public Embedded convertToPresentation(Boolean value, Class<? extends Embedded> targetType, Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        return null;
    }

    @Override
    public Class<Boolean> getModelType() {
        return Boolean.class;
    }

    @Override
    public Class<Embedded> getPresentationType() {
        return Embedded.class;
    }
    
    private void fillMaps(){
        
        ThemeResource yes = new ThemeResource("image-res/icons/submit-black-16.png");
        ThemeResource no = new ThemeResource("image-res/icons/close-black-16.png");
        Embedded statusYes = new Embedded("",yes);
        Embedded statusNo = new Embedded("",no);
        
        fillEntry(true, statusYes);
        fillEntry(false, statusNo);
    }
    
    private void fillEntry(Boolean bool, Embedded emb){
        byModel.put(bool, emb);
        byPresentation.put(emb, bool);
    }
    
}
