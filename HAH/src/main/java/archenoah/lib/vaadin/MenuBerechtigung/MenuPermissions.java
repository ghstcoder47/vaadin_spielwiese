package archenoah.lib.vaadin.MenuBerechtigung;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.comunication.dbclass.DBClass.CustomQuery;
import archenoah.lib.vaadin.Language.i18n.I18nManager;
import archenoah.web.normal.UserInfo.UserData;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Individuelle_Manue_Comands;
import archenoah.web.normal.main.Menuepunkte.Standard.Standard_Manue_Comands;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;

public class MenuPermissions {


    // {section fields}
    // ****************
    
    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MenuPermissions.class);
    
    private MenuBar men = (MenuBar) UI.getCurrent().getSession().getSession().getAttribute("menue");
    private MenuItem root = men.getItems().get(0);
    
    private Individuelle_Manue_Comands cmd = (Individuelle_Manue_Comands) UI.getCurrent().getSession().getSession().getAttribute("individualCommands");
    private Standard_Manue_Comands def = (Standard_Manue_Comands) UI.getCurrent().getSession().getSession().getAttribute("standardCommands");
    
    private HashMap<MenuItem, MenuProperties> menuProperties = new HashMap<>();
    
   
    // {end fields}

    // {section constructors}
    // **********************
    public MenuPermissions() {
        generateMenuItemList(root);
        localizeMenu();
        dbGetPermissions();
        setPermissions(root);
        
        openDefaultSet();
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    private void generateMenuItemList(MenuItem menu) {
        
        menuProperties.put(menu, new MenuProperties(menu));
        
        if(menu.getChildren() == null) {
            return;
        }
        
        for (MenuItem sub : menu.getChildren()) {
            generateMenuItemList(sub);
        }
        
    }
    
    private void localizeMenu() {
        
        ArrayList<String> captions = new ArrayList<String>();
        for (MenuProperties p : menuProperties.values()) {
            captions.add(p.getCaption());
        }
        
        I18nManager i18n = new I18nManager("menus", captions);
        
        for (Entry<MenuItem, MenuProperties> entry : menuProperties.entrySet()) {
            entry.getKey().setText(i18n.get(entry.getValue().getCaption()));
        }
    }
    
    private void setPermissions(MenuItem menu) {
        
        if(menu.hasChildren()) {
            
            for (MenuItem child : new CopyOnWriteArrayList<MenuItem>(menu.getChildren())) {
                setPermissions(child);
            }
        }
        
        if(!menu.hasChildren() && !menuProperties.get(menu).isVisible() && menu != root) {
            menu.getParent().removeChild(menu);
        }
        
    }
    
    private void openDefaultSet() {

        if(UserData.get().getNurse().isNurse() && UserData.get().getNurse().getNurseId() != null) {
            runMenuItemCommand(root, cmd.NurseCockpit);
        }

    }

    private void runMenuItemCommand(MenuItem item, Command command) {

        Command lcom = item.getCommand();

        if (lcom != null) {

            if (command.hashCode() == lcom.hashCode()) {
                lcom.menuSelected(item);
                return;
            }

        }

        if (item.hasChildren()) {
            for (MenuItem child : item.getChildren()) {
                runMenuItemCommand(child, command);
            }
        }

    }
    
    // {end privatemethods}

    // {section database}
    // ******************
    
    private void dbGetPermissions() {
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "select "
         + "\n " + "    ASM_U_MEN as menu,"
         + "\n " + "    MAX(ALM_ERSTELLEN) as c,"
         + "\n " + "    MAX(ALM_LESEN) as r,"
         + "\n " + "    MAX(ALM_BEARBEITEN) as u,"
         + "\n " + "    MAX(ALM_LOESCHEN) as d"
         + "\n " + ""
         + "\n " + "    from cms_auth_liste_menu"
         + "\n " + "left join cms_auth_stammdaten_menu on ALM_U_ID = ASM_ID"
         + "\n " + ""
         + "\n " + "where ASM_BER = 1 and ALM_G_ID IN ("
         + "\n " + "    select AL_G_ID from cms_auth_liste_gu"
         + "\n " + "    where AL_U_ID = "+UserData.get().getUserId()+")"
         + "\n " + ""
         + "\n " + "group by ALM_U_ID";
        q.setSqlString(sql);
//        q.db.debugNextQuery(true);
        
        Container con = MyUtils.convertIndex(q.query(), "menu");
        
        for (MenuProperties p : menuProperties.values()) {
            
            Item item = con.getItem(p.getCaption());
            
            if(item != null) {
                p.setCreate(MyUtils.getValueFromItem(item, "c", Integer.class) == 1);
                p.setRead(MyUtils.getValueFromItem(item, "r", Integer.class) == 1);
                p.setUpdate(MyUtils.getValueFromItem(item, "u", Integer.class) == 1);
                p.setDelete(MyUtils.getValueFromItem(item, "d", Integer.class) == 1);
            }
            
        }
    }
    
    // {end database}

    // {section classes}
    // ****************
    
    public static class MenuProperties{
        
        private MenuItem menu;
        
        private String caption;
        private boolean create = true;
        private boolean read = true;
        private boolean update = true;
        private boolean delete = true;
        
        public MenuProperties(MenuItem menu) {
            this.menu = menu;
            this.caption = menu.getText();
        }
        
        public boolean isVisible() {
            return create || read || update || delete;
        }
        
        public MenuItem getMenu() {
            return menu;
        }
        public void setMenu(MenuItem menu) {
            this.menu = menu;
        }
        public String getCaption() {
            return caption;
        }
        public void setCaption(String caption) {
            this.caption = caption;
        }
        public void setCrud(boolean c, boolean r, boolean u, boolean d) {
            this.create = c;
            this.read = r;
            this.update = u;
            this.delete = d;
        }
        public boolean isCreate() {
            return create;
        }
        public void setCreate(boolean create) {
            this.create = create;
        }
        public boolean isRead() {
            return read;
        }
        public void setRead(boolean read) {
            this.read = read;
        }
        public boolean isUpdate() {
            return update;
        }
        public void setUpdate(boolean update) {
            this.update = update;
        }
        public boolean isDelete() {
            return delete;
        }
        public void setDelete(boolean delete) {
            this.delete = delete;
        }
        
    }
    
    // {end layout}
}
