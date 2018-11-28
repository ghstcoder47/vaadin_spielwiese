package archenoah.lib.tool.pdf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import com.vaadin.server.VaadinService;

public class FontProvider {

    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FontProvider.class);
    private static final String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
    
    public enum FONT {
        LiberationSansRegular("LiberationSans-Regular.ttf"),
        LiberationSansBold("LiberationSans-Bold.ttf"),
        LiberationSansItalic("LiberationSans-Italic.ttf"),
        LiberationSansBoldItalic("LiberationSans-BoldItalic.ttf");
        
        private String location;
        
        FONT(String str){
            location = str;
        }
        
        String getLocation(){
            return location;
        }
    }
    
    public static PDType0Font load(PdfPopulater pdf, FONT font) {
        return FontProvider.load(pdf.getDocument(), font);
    }
    
    static PDType0Font load(PDDocument pdf, FONT font) {
        
        try (
            InputStream fontStream = new FileInputStream(basepath + "/VAADIN/fonts/" + font.getLocation());
        ){
            return PDType0Font.load(pdf, fontStream);
        } catch (FileNotFoundException e) {
            log.error("font {} not found!", font.getLocation());
            return null;
        } catch (IOException e){
            log.error(e.getMessage());
            return null;
        }
        
        
        
    }

}
