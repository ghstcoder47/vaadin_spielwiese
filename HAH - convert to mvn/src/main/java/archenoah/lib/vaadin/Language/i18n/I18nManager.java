package archenoah.lib.vaadin.Language.i18n;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.comunication.dbclass.DBClass.CustomQuery;

import com.google.common.base.Joiner;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.CustomTable;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;

import de.steinwedel.messagebox.MessageBox;
import de.steinwedel.messagebox.MessageBoxListener;

public class I18nManager {

    // {section fields}
    // ****************
    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(I18nManager.class);
    
    private boolean debug = false;
    
    private Object root;
    private Class<?> rootClass;
    private Class<?> force;
    private String rootName;   
    private String locale;
    private Collection<String> captions;
    
    private HashMap<String, String> localizedCaptions = new HashMap<String, String>();
    private HashMap<Object, String> originalCaptions = new HashMap<Object, String>();
    
    private HashMap<I18nCB, String> captionBeanFields = new HashMap<I18nCB, String>();
    
    private HashMap<I18nNB, String> notifierBeanFields = new HashMap<I18nNB, String>();
    private HashMap<I18nMB, String> messageBeanFields = new HashMap<I18nMB, String>();
    
    private ArrayList<Object> replaceComponents = new ArrayList<Object>(); 
    private ArrayList<Object> ignoreComponents = new ArrayList<Object>();
    private HashMap<CustomTable, ArrayList<Object>> tableProperties = new HashMap<CustomTable, ArrayList<Object>>();
    
    private static String ntf = "ntf:";
    private static String msg = "msg:";
    private static String enm = "enm:";
    private static String title = ".title";
    private static String content = ".content";
    
    private static I18nManager i18n;
    private static HashMap<I18nNB, String> globalNotifierBeanFields = new HashMap<I18nNB, String>();
    private static HashMap<I18nMB, String> globalMessageBeanFields = new HashMap<I18nMB, String>();
    private static HashMap<I18nCB, String> globalCaptionBeanFields = new HashMap<I18nCB, String>();
    
    private static HashSet<I18nManager> registeredManagers = new HashSet<I18nManager>();
    
    static {
        initStatic();
    }
    
    private static void initStatic() {
        ArrayList<String> strings = new ArrayList<String>(); 
        
        try {
            for (Field field : I18nGlobalCaptions.class.getDeclaredFields()) {
                field.setAccessible(true);
                Object obj = field.get(I18nGlobalCaptions.class);
                if(obj instanceof I18nCB) {
                    globalCaptionBeanFields.put((I18nCB) obj, field.getName());
                    strings.add(field.getName());
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        
        try {
            for (Field field : I18nGlobalNotifiers.class.getDeclaredFields()) {
                field.setAccessible(true);
                Object obj = field.get(I18nGlobalNotifiers.class);
                if(obj instanceof I18nNB) {
                    globalNotifierBeanFields.put((I18nNB) obj,  ntf + field.getName());
                    strings.add(ntf + field.getName() + title);
                    strings.add(ntf + field.getName() + content);
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        
        try {
            for (Field field : I18nGlobalMessages.class.getDeclaredFields()) {
                field.setAccessible(true);
                Object obj = field.get(I18nGlobalMessages.class);
                if(obj instanceof I18nMB) {
                    globalMessageBeanFields.put((I18nMB) obj,  msg + field.getName());
                    strings.add(msg + field.getName() + title);
                    strings.add(msg + field.getName() + content);
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        
        i18n = new I18nManager(I18nManager.class.getCanonicalName(), strings);
    }
    
    // {end fields}

    // {section constructors}
    // **********************
    /**
     * To be called after the Component has been build (mainLayout is generated) <br />
     * Genereates and translates captions form all Components, using the initial Component's caption as key <br />
     * If available, parses I18nCB and I18nNB static components for captions and notifiers <br />
     * @see I18nCB
     * @see I18nNB
     * @param component
     */
    public I18nManager(Object component) {
        
        this(component, null, false);
    }
    
    /**
     * force I18nManager to iterate until a matching superclass is found<br 7>
     * this is required for CustomComponents that inherit from a common CustomComponent and do not have any fields of their own 
     * @param component
     * @param force
     */
    public I18nManager(Object component, Class<?> force) {
        this(component, force, false);
    }
    
    public I18nManager(Object component, Class<?> force, boolean debug) {
        if(component == null) {
            throw new IllegalArgumentException("component must be specified");
        }
        
        useLocale();
        
        this.debug = debug;
        this.force = force;
        
        this.root = component;
        this.rootClass = traverseClass(root.getClass());
        this.rootName = escapeName(rootClass.getCanonicalName());
        
        iterateComponent();
        register(this);
    }
    
    /**
     * used in I18nConverter <br /> directly takes a parent string and iterates the provided Collection as identifiers
     * @param parent
     * @param captions
     */
    public I18nManager(String parent, Collection<String> captions) {
        direct(parent, captions);
    }
    
    public I18nManager(Class<?> parent, Collection<String> captions) {
        direct(parent.getCanonicalName(), captions);
    }
    
    public  I18nManager(Class<?> parent, Collection<String> captions, Collection<Class<? extends I18nEnum>> enums) {
        
        if(captions == null) {
            captions = new ArrayList<String>();
        }
        
        for (Class<? extends I18nEnum> e : enums) {
            
            if(Enum.class.isAssignableFrom(e)) {
                for (I18nEnum cnst : e.getEnumConstants()) {
                    captions.add(enm + e.getSimpleName() + "." + cnst.toString());
                }
            }
            
            for (Class<?> pen : e.getDeclaredClasses()) {
                
                if(Enum.class.isAssignableFrom(pen)) {
                    for (Object cnst : pen.getEnumConstants()) {
                        captions.add(enm + e.getSimpleName() + "." + pen.getSimpleName() + "." + cnst.toString());
                    }
                }
                
            }
            
        }
        
        direct(parent.getCanonicalName(), captions);
    }
    
    public I18nManager(String parent) {
        direct(parent, Collections.<String> emptyList());
    }
    
    private void direct(String parent, Collection<String> captions) {
        if(parent == null) {
            throw new IllegalArgumentException("parent must be specified");
        }
        if(captions == null) {
            throw new IllegalArgumentException("captions must be specified");
        }
        
        useLocale();
        
        this.rootName = escapeName(parent);
        this.captions = captions;
        
        iterateComponent();
        register(this);
    }
    
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    
    public Object getRoot() {
        return root;
    }
    
    public String getRootName() {
        return rootName;
    }
    
    @Override
    public String toString() {
        return getRootName();
    }
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    /**
     * get a caption from provided string (used by I18NConverter)
     * @param rawCaption
     * @return
     */
    public String get(String rawCaption) {
        return localizedCaptions.get(rawCaption);
    }
    
    /**
     * get a caption from a CaptionBean
     * @see I18nCB
     * @param caption
     * @return
     */
    public String get(I18nCB caption) {
        return get(captionBeanFields.get(caption));
    }
    
    public String get(I18nEnum i18nEnum) {
        
        return get(enm
            + ((i18nEnum.getClass().getEnclosingClass() != null) 
                ? i18nEnum.getClass().getEnclosingClass().getSimpleName() + "." : "")
            + i18nEnum.getClass().getSimpleName() + "." + i18nEnum.toString());
    }
    
    public HashMap<String, String> getLocalizedCaptions(){
        return this.localizedCaptions;
    }
    
    /**
     * force maintenance of all active locales in `cms_i18n_languages`
     */
    public static void updateAllLocales() {
        dbUpdateAllLocales();
    }
    
    public void setDebug(boolean debug) {
        this.debug = debug;
    }
    
    public void refresh() {
        resetCaptions();
        iterateComponent();
    }
    
    /**
     * force these components to be added with their new caption under a new identifier on refresh()
     * @param replaceComponents
     */
    public void replace(Object... replaceComponents) {
       this.replaceComponents.addAll(Arrays.asList(replaceComponents));
    }
    
    /**
     * ignore these components completely on refresh()
     * @param ignoreComponents
     */
    public void ignore(Object...ignoreComponents) {
        this.ignoreComponents.addAll(Arrays.asList(ignoreComponents));
    }
    
    /**
     * check these columns for AbstractComponents to include
     */
    public void includeTableProperties(CustomTable table, Object... properties) {
        this.tableProperties.put(table, new ArrayList<Object>(Arrays.asList(properties)));
    }
    
    /**
     * resets all component captions to their original values, <br />
     * reinitializes caption containers and applies new locale 
     * @param locale
     */
    public void changeComponentLanguage(Locale locale) {
        
        if(locale == null) {
            throw new IllegalArgumentException("locale must be specified");
        }
        
        this.locale = locale.getLanguage();
        initStatic();
        resetCaptions();
        iterateComponent();
    }
    
    public static String global(I18nCB i18nCaptionBean) {
        return i18n.get(globalCaptionBeanFields.get(i18nCaptionBean));
    }
    
    /**
     * trigger a notification
     * @see I18nManager#I18nManager(CustomComponent)
     * @see I18nNB
     * @param notifier
     */
    public static void global(I18nNB i18nGlobalNotifiers) {
        triggerNotifier(
            i18n.get(globalNotifierBeanFields.get(i18nGlobalNotifiers) + title),
            i18n.get(globalNotifierBeanFields.get(i18nGlobalNotifiers) + content),
            i18nGlobalNotifiers.type,
            i18nGlobalNotifiers.position,
            i18nGlobalNotifiers.delay);
    }
    
    /**
     * 
     * @param i18nGlobalMessages
     * @return can be null
     */
    public static MessageBox global(I18nMB i18nGlobalMessages) {
        return global(i18nGlobalMessages, null, null);
    }
    
    /**
     * 
     * @param i18nGlobalMessages
     * @param messageBoxListener
     * @return can be null
     */
    public static MessageBox global(I18nMB i18nGlobalMessages, MessageBoxListener messageBoxListener) {
        return global(i18nGlobalMessages, messageBoxListener, null);
    }
    
    /**
     * 
     * @param i18nGlobalMessages
     * @param messageBoxListener
     * @param component
     * @return can be null
     */
    public static MessageBox global(I18nMB i18nGlobalMessages, MessageBoxListener messageBoxListener, Component component) {
        
        return triggerMessage(
            i18n.get(globalMessageBeanFields.get(i18nGlobalMessages) + title),
            i18n.get(globalMessageBeanFields.get(i18nGlobalMessages) + content),
            messageBoxListener, component, i18nGlobalMessages);
        
    }
    
    /**
     * trigger a notification
     * @see I18nManager#I18nManager(CustomComponent)
     * @see I18nNB
     * @param notifier
     */
    public void notifier(I18nNB notifier) {
        
          
        if(notifierBeanFields.containsKey(notifier)) {
            
            triggerNotifier(
                get(notifierBeanFields.get(notifier) + title),
                get(notifierBeanFields.get(notifier) + content),
                notifier.type,
                notifier.position,
                notifier.delay);
            
        }else if (globalNotifierBeanFields.containsKey(notifier)){
            global(notifier); 
        }else {
            return;
        }
        
    }
    
    /**
     * 
     * @param messageBox
     * @return can be null
     */
    public MessageBox messageBox(I18nMB messageBox) {
        return messageBox(messageBox, null, null);
    }
    
    /**
     * 
     * @param messageBox
     * @param messageBoxListener
     * @return can be null
     */
    public MessageBox messageBox(I18nMB messageBox, MessageBoxListener messageBoxListener) {
        return messageBox(messageBox, messageBoxListener, null);
    }
    
    /**
     * 
     * @param messageBox
     * @param messageBoxListener
     * @param component
     * @return can be null
     */
    public MessageBox messageBox(I18nMB messageBox, MessageBoxListener messageBoxListener, Component component) {
        
        
        if(messageBeanFields.containsKey(messageBox)) {
            
            return triggerMessage(
                get(messageBeanFields.get(messageBox) + title),
                get(messageBeanFields.get(messageBox) + content),
                messageBoxListener, component, messageBox);
            
        }else if (globalMessageBeanFields.containsKey(messageBox)){
            return global(messageBox, messageBoxListener, component); 
        }else {
            return null;
        }
        
    }
    
    public static void clearManagers() {
        registeredManagers.clear();
    }
    
    public static HashSet<I18nManager> getManagers() {
        return registeredManagers;
    }
    
    public void setTableHeaders() {
        for (Entry<Object, String> entry : originalCaptions.entrySet()) {
            
            if(entry.getKey() instanceof CustomTableHeader) {
                CustomTableHeader h = (CustomTableHeader) entry.getKey();
                h.table.setColumnHeader(h.cpid, getCaption(h.cpid.toString()));
            }
            if(entry.getKey() instanceof TableHeader) {
                TableHeader h = (TableHeader) entry.getKey();
                h.table.setColumnHeader(h.cpid, getCaption(h.cpid.toString()));
            }
        }
    }
    
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    
    private void useLocale() {
        
        if(UI.getCurrent() != null && UI.getCurrent().getLocale() != null) {
            this.locale = UI.getCurrent().getLocale().getLanguage();
        }else {
            this.locale = "de"; //TASK fixme!
        }
    }
    
    private static void register(I18nManager manager) {
        registeredManagers.add(manager);
        log.info("registering: {}", manager.getRootName());
    }
    
    private void resetCaptions() {
        for (Entry<Object, String> entry : originalCaptions.entrySet()) {
            
            Object key = entry.getKey();
            String caption = entry.getValue();
            
            if(ignoreComponents.contains(key)
                || replaceComponents.contains(key)) {
                continue;
            }
            
            if(key instanceof Label) {
                ((Label) key).setValue(caption);
            }else if(key instanceof AbstractComponent) {
                ((AbstractComponent) key).setCaption(caption);
            }
            
            if(key instanceof Tab) {
                ((Tab) key).setCaption(caption);
            }
            
            if(key instanceof MenuItem) {
                ((MenuItem) key).setText(caption);
            }
            
            if(key instanceof TableHeader) {
                TableHeader header = (TableHeader) key;
                header.table.setColumnHeader(header.cpid, caption);
            }
            
            if(key instanceof CustomTableHeader) {
                CustomTableHeader header = (CustomTableHeader) key;
                header.table.setColumnHeader(header.cpid, caption);
            }
        }
    }
    
    public static void triggerNotifier(String tlt, String cnt, Type type, Position pos, int del) {
        
        String br = (tlt != null && !"".equals(tlt)) ? "<br />" : "";
        Notification n = new Notification(tlt, br + cnt,type);
        n.setPosition(pos);
        n.setDelayMsec(del);
        n.setHtmlContentAllowed(true);
        n.show(Page.getCurrent());
    }
    
    private static MessageBox triggerMessage(String tlt, String cnt, MessageBoxListener mbl, Component comp, I18nMB msgBean) {
        
        MessageBox box;
        
        if(comp != null) {
            if(msgBean.resource != null) {
                box = MessageBox.showCustomized(msgBean.resource, tlt, comp, mbl, msgBean.buttons);
            }else {
                box = MessageBox.showCustomized(msgBean.icon, tlt, comp, mbl, msgBean.buttons);
            }
        }else {
            if(msgBean.resource != null) {
                box = MessageBox.showHTML(msgBean.resource, tlt, cnt, mbl, msgBean.buttons);
            }else {
                box = MessageBox.showHTML(msgBean.icon, tlt, cnt, mbl, msgBean.buttons);
            }
        }
        
        if(box != null) {
            box.setModal(true);
            box.setAutoClose(true);
            box.getWindow().setDraggable(false);
            
            if(msgBean.height != null) {
                box.setHeight(msgBean.height);
            }
            if(msgBean.width != null) {
                box.setWidth(msgBean.width);
            }
        }
        
        return box;
            
    }
    
    private Class<?> traverseClass(Class<?> clazz) {
        
        if(force == null || clazz == force) {
            return clazz;
        }
        return traverseClass(clazz.getSuperclass());
    }
    
    private String escapeName(String name) {
        return name.replaceAll("'", "''");
    }
    
    @SuppressWarnings("unchecked")
    private void iterateComponent() {
        
        dbFillCaptionMap();
        
        //handle class based init
        if(rootClass != null) {
            try {
                
                if(debug) {
                    log.info("root: {}", rootClass);
                }
                
                // only get AbstractFields for Classes extending CustomComponent
                // TASK or Panel (quick fix for Metadata)
                if(CustomComponent.class.isAssignableFrom(rootClass) || Panel.class.isAssignableFrom(rootClass)) {
                
                    // handle field captions
                    Field[] fields = rootClass.getDeclaredFields();
                    
                    HashMap<AbstractComponent, Field> components = new HashMap<AbstractComponent, Field>(); 
        
                   
                    for (Field field : fields) {
                        
                        field.setAccessible(true);
                        Object comp = field.get(root);
                        
                        if(debug) {
                            log.info("field: {}, {}", field, field.getName());
                        }
                        
                        if(comp instanceof AbstractComponent) {
                            components.put((AbstractComponent) comp, field);
                        }
                    }    
                    
                    for (Entry<AbstractComponent, Field> entry : components.entrySet()) {
                        
                        AbstractComponent key = entry.getKey();
                        
                        if(ignoreComponents.contains(key)) {
                            continue;
                        }
                        
                        if(debug) {
                            log.debug("key: {}", key);
                        }
                        
                        //special case for menus
                        if(key instanceof MenuBar) {
                            MenuBar menu = (MenuBar) key;
                            
                            for(MenuItem item :menu.getItems()){
                                // original captions are handled during iteration
                                menuCaption(item);
                            }
                            
                        }
                        
                        // special case for tab captions/contents
                        if(key instanceof TabSheet) {
                            TabSheet tabSheet = (TabSheet) key;
                            Iterator<Component> ti = tabSheet.iterator();
                            while (ti.hasNext()) {
                              Component tabComponent = ti.next();
                              
                              Tab tab = tabSheet.getTab(tabComponent);
                              if(tab.getCaption() != null) {
                                  originalCaptions.put(tab, tab.getCaption());
                                  tab.setCaption(getCaption(tab.getCaption()));
                              }
                              
                              
                              
                            }
                        }
                        // special case for CustomTable columns
                        if(key instanceof CustomTable) {
                            CustomTable table = (CustomTable) key;
                            
                            // all propertyColumns, including nonvisible
                            for(Object cpid : table.getContainerPropertyIds()) {
                                if(cpid != null) {
                                    originalCaptions.put(new CustomTableHeader(cpid, table), cpid.toString());
                                    table.setColumnHeader(cpid, getCaption(cpid.toString()));
                                }
                            }
                            
                            // all generated columns
                            List<Object> list = new ArrayList<Object>(Arrays.asList(table.getVisibleColumns()));
                            list.removeAll(table.getContainerPropertyIds());
                            for(Object cpid : list) {
                                if(cpid != null) {
                                    originalCaptions.put(new CustomTableHeader(cpid, table), cpid.toString());
                                    table.setColumnHeader(cpid, getCaption(cpid.toString()));
                                }
                            }
                            
                            // add embedded components
                            if(tableProperties.containsKey(table)) {
                                
                                for (Object iid : table.getItemIds()) {
                                    Item item = table.getItem(iid);
                                    for (Object column : tableProperties.get(table)) {
                                        
                                        if(item.getItemProperty(column) == null) {
                                            continue;
                                        }
                                        
                                        Class<?> pc = item.getItemProperty(column).getType();
                                        if(pc != null && AbstractComponent.class.isAssignableFrom(pc)) {
                                            AbstractComponent comp = (AbstractComponent) item.getItemProperty(column).getValue();
                                            comp.setCaption(getCaption(comp.getCaption()));
                                        }
                                    }
                                }
                            }

                            
                        }
                        
                        // special case for Table columns
                        if(key instanceof Table) {
                            Table table = (Table) key;
                            
                            // all properties
                            for(Object cpid : table.getContainerPropertyIds()) {
                                if(cpid != null) {
                                    originalCaptions.put(new TableHeader(cpid, table), cpid.toString());
                                    table.setColumnHeader(cpid, getCaption(cpid.toString()));
                                }
                            }
                            
                            // all generated columns
                            List<Object> list = new ArrayList<Object>(Arrays.asList(table.getVisibleColumns()));
                            list.removeAll(table.getContainerPropertyIds());
                            for(Object cpid : list) {
                                if(cpid != null) {
                                    originalCaptions.put(new TableHeader(cpid, table), cpid.toString());
                                    table.setColumnHeader(cpid, getCaption(cpid.toString()));
                                }
                            }
                            
                        }
                        
                        AbstractComponent component = entry.getKey();
                        
                        // labels are special little snowflakes
                        if(component instanceof Label) {
                            Label label = (Label) component;
                            label.setContentMode(ContentMode.HTML);
                            originalCaptions.put(label, label.getValue());
                            if(label.getValue() != null) {
                                label.setValue(getCaption(label.getValue()));
                            }
                        //default caption behaviour
                        }else
                            originalCaptions.put(component, component.getCaption());
                            if(component.getCaption() != null) {
                            component.setCaption(getCaption(component.getCaption()));
                        }
                    }
                
                }
 
                Class<?>[] classes = rootClass.getDeclaredClasses();
                for (Class<?> dc : classes) {

                    // handle notifiers                    
                    if(I18nNB.class.isAssignableFrom(dc)) {
                        
                        Class<? extends I18nNB> parent = (Class<? extends I18nNB>) dc;
                        
                        Field[] notifiers = parent.getDeclaredFields();
                        for (Field field : notifiers) {
                            
                            field.setAccessible(true);
                            Object obj = field.get(parent);
                            
                            if(obj instanceof I18nNB) {
                                notifierBeanFields.put((I18nNB) obj, ntf + field.getName());
                                
                                getCaption(ntf + field.getName() + title);
                                getCaption(ntf + field.getName() + content);
                            }
                        }
                    }
                    
                    // messageboxes
                    if(I18nMB.class.isAssignableFrom(dc)) {
                        
                        Class<? extends I18nMB> parent = (Class<? extends I18nMB>) dc;
                        
                        Field[] boxes = parent.getDeclaredFields();
                        for (Field field : boxes) {
                            
                            field.setAccessible(true);
                            Object obj = field.get(parent);
                            
                            if(obj instanceof I18nMB) {
                                messageBeanFields.put((I18nMB) obj, msg + field.getName());
                                
                                getCaption(msg + field.getName() + title);
                                getCaption(msg + field.getName() + content);
                            }
                        }
                    }
                    
                    // & captions
                    if(I18nCB.class.isAssignableFrom(dc)) {
                        
                        Class<? extends I18nCB> captionParent = (Class<? extends I18nCB>) dc;
                        
                        Field[] captionFields = captionParent.getDeclaredFields();
                        for (Field caption : captionFields) {
                            
                            caption.setAccessible(true);
                            Object obj = caption.get(captionParent);
                            
                            if(obj instanceof I18nCB) {
                                captionBeanFields.put((I18nCB) obj, caption.getName());
                                getCaption(caption.getName());
                            }
                            
                        }
                        
                    }
                }

                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        // for converter and custom captions
        if(captions != null) {
            for (String caption : captions) {
                getCaption(caption);
            }
        }
        
        dbUpdate();
        
    }
    
    private void menuCaption(MenuItem menu) {
        if(menu.hasChildren()) {
            for (MenuItem child : menu.getChildren()) {
                menuCaption(child);
            }
        }
        originalCaptions.put(menu, menu.getText());
        menu.setText(getCaption(menu.getText()));
    }
    
    private String getCaption(String caption) {
        if(!localizedCaptions.containsKey(caption)) {
            localizedCaptions.put(caption, caption);
        }
        return localizedCaptions.get(caption);
    }
    // {end privatemethods}

    // {section database}
    // ******************
    
    private void dbFillCaptionMap() {
        
        localizedCaptions = new HashMap<String, String>();
        
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "SELECT  DISTINCT"
         + "\n " + "        COALESCE(b.IDENTIFIER, a.IDENTIFIER) IDENTIFIER,"
         + "\n " + "        COALESCE(b.CAPTION, a.CAPTION) CAPTION"
         + "\n " + "FROM cms_i18n a"
         + "\n " + "LEFT JOIN"
         + "\n " + "("
         + "\n " + "   SELECT PARENT, IDENTIFIER, LOCALE, CAPTION"
         + "\n " + "   FROM cms_i18n"
         + "\n " + "   WHERE LOCALE = '"+locale+"'"
         + "\n " + ") b ON a.PARENT = b.PARENT && a.IDENTIFIER = b.IDENTIFIER"
         + "\n " + "where a.PARENT = (select ID from cms_i18n_components where NAME = '"+rootName+"')";
//        q.db.debugNextQuery(true);
        q.setSqlString(sql);
        
        Container con = q.query();
        
        for (Object iid : con.getItemIds()) {
            Item item = con.getItem(iid);
            localizedCaptions.put(MyUtils.getValueFromItem(item, "IDENTIFIER", String.class), 
                MyUtils.getValueFromItem(item, "CAPTION", String.class));
        }
    }
    
    private void dbUpdate() {
        
        ArrayList<String> values = new ArrayList<String>();
        
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "";
        
        for (Entry<String, String> entry : localizedCaptions.entrySet()) {
            
            values.add("((select ID from cms_i18n_components where NAME = '"+rootName+"'), "
            +  "'" + entry.getKey() + "', "
            +  "'--', "
            +  "'" + entry.getKey() + "'"
            + ")" 
            );
            
        }
        
        // maintain parent component directory
        sql = "insert into cms_i18n_components (NAME)"
            + "\n " + "VALUES ('"+rootName+"') ON DUPLICATE KEY UPDATE ID = ID";
        q.setSqlString(sql);
        q.db.debugNextQuery(debug);
        q.update();
        
        if(values.size() == 0) {
            return;
        }
        
        // maintain defaults
        sql = "INSERT INTO cms_i18n (PARENT, IDENTIFIER, LOCALE, CAPTION)"
            + "\n " + "VALUES " + Joiner.on(",\n").join(values)
            + "\n " + "ON DUPLICATE KEY UPDATE CAPTION=VALUES(CAPTION)";
        q.setSqlString(sql);
        q.db.debugNextQuery(debug);
        q.update();
        
    }
    
    private static void dbUpdateAllLocales() {
        
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "";
        
        // insert missing locales
        sql = "INSERT INTO cms_i18n (PARENT, IDENTIFIER, LOCALE, CAPTION)"
                + "\n " + "SELECT PARENT, IDENTIFIER, lang.LOCALE, src.CAPTION"
                + "\n " + "FROM cms_i18n as src"
                + "\n " + "JOIN cms_i18n_languages as lang"
                + "\n " + "WHERE src.LOCALE = '--'"
         + "\n " + "ON DUPLICATE KEY UPDATE CAPTION = cms_i18n.CAPTION";
        q.setSqlString(sql);
        q.update();
    }
    
    // {end database}

    // {section classes}
    // ****************
    
    private class TableHeader{
        private Object cpid;
        private Table table;
        public TableHeader(Object cpid, Table table) {
            this.cpid = cpid;
            this.table = table;
        }
    }
    
    private class CustomTableHeader{
        private Object cpid;
        private CustomTable table;
        public CustomTableHeader(Object cpid, CustomTable table) {
            this.cpid = cpid;
            this.table = table;
        }
    }
    
    // {end layout}
}
