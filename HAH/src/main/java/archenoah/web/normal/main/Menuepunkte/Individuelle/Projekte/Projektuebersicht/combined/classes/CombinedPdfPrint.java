package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.combined.classes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.zip.ZipOutputStream;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.tepi.filtertable.FilterTable;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.pdf.OnDemandFileDownloader;
import archenoah.lib.tool.pdf.OnDemandFileDownloader.OnDemandStreamResource;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.pop.classes.PoPPdfPrint;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.replagal.classes.ReplagalPdfPrint;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.vpriv.classes.VprivPdfPrint;

import com.vaadin.data.Item;
import com.vaadin.server.Page;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Layout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

public class CombinedPdfPrint {
    
    public interface CombinedPrintable{
        /**
         * zos must be close manually!
         * @param zos
         * @param filenames
         * @param idList
         */
        public void writeEntriestoZip(ZipOutputStream zos, ArrayList<String> filenames, ArrayList<Object> idList);
    }
    
    private org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
    private FilterTable table;
    
    
    
    public CombinedPdfPrint(FilterTable table) {
        this.table = table;
    }

    
    /* ******************
     * Public 
     */
    
    public void attach(AbstractComponent component, final String text, final boolean includeName){
        
        new OnDemandFileDownloader(new OnDemandStreamResource() {

            @Override
            public byte[] getOutput() {
                return generateOutput(includeName);
            }

            @Override
            public String getContentType() {
                return "application/zip";
            }
            
            @Override
            public String getFilename() {
                return "COMBINED-" + text + "-" + DateTimeFormat.forStyle("SS").print(new DateTime())  + ".zip";
            }
        }).extend(component);
    }

    public void attach(Layout layout, MenuBar.MenuItem menuItem, final boolean includeName){
        
        final String downloadButtonID = "downloadPdfCombined-" + (includeName ? "named" : "anonymous");
        
        final Button downloadInvisibleButton = new Button();
        downloadInvisibleButton.setId(downloadButtonID);
        downloadInvisibleButton.addStyleName("hidden");
        layout.addComponent(downloadInvisibleButton);
        attach(downloadInvisibleButton, menuItem.getText(), includeName);
        
        menuItem.setCommand(new Command() {
            
            @Override
            public void menuSelected(MenuItem selectedItem) {
                // TASK Auto-generated method stub
                Page.getCurrent().getJavaScript().execute("document.getElementById('"+downloadButtonID+"').click();");
            }
        });
    }
    
    /* ********************
     * Private
     */
    
    private byte[] generateOutput(boolean includeName){
        
        ArrayList<String> filenames = new ArrayList<String>();
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(os);
        
        HashMap<String, CombinedPrintable> printers = new HashMap<String, CombinedPrintable>();
        printers.put("REP", new ReplagalPdfPrint(includeName));
        printers.put("VPRIV", new VprivPdfPrint(includeName));
        printers.put("POP", new PoPPdfPrint(includeName));
        
        
        
        HashMap<String, ArrayList<Object>> idLists = generateIdLists();
        
        try {
            
            for (Entry<String, ArrayList<Object>> entry : idLists.entrySet()) {
                if(printers.get(entry.getKey()) != null) {
                    printers.get(entry.getKey()).writeEntriestoZip(zos, filenames, entry.getValue());
                }
            }
            
            zos.finish();
            zos.close();

            
        } catch (IOException e) {
            // TASK Auto-generated catch block
            e.printStackTrace();
        }
        
        return os.toByteArray();
    }
    
    private HashMap<String, ArrayList<Object>> generateIdLists(){
        
        HashMap<String, ArrayList<Object>> list = new HashMap<String, ArrayList<Object>>();
        
        for (Object tid : (Collection<Object>) table.getValue()) {
            
            Item item = table.getItem(tid);
            
            String code = MyUtils.getValueFromItem(item, "project_code", String.class); 
            
            if(list.get(code) == null) {
                list.put(code, new ArrayList<Object>());
            }
            
            list.get(code).add(MyUtils.getValueFromItem(item, "id", Integer.class));
            
        }
        
        return list;
    }
    
}
