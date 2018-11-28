package archenoah.lib.vaadin.validator;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.comunication.dbclass.DBClass.CustomQuery;
import archenoah.lib.vaadin.Components.Validators.MinMaxValidator;
import archenoah.lib.vaadin.Components.Validators.RegExtendedValidator;
import archenoah.lib.vaadin.Language.i18n.I18nCB;
import archenoah.lib.vaadin.Language.i18n.I18nMB;
import archenoah.lib.vaadin.Language.i18n.I18nManager;
import archenoah.lib.vaadin.Language.i18n.I18nNB;

import com.google.common.base.Joiner;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.filter.Or;
import com.vaadin.server.ErrorMessage;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import de.steinwedel.messagebox.ButtonId;
import de.steinwedel.messagebox.Icon;
import de.steinwedel.messagebox.MessageBoxListener;

public class ValidatorManager {


    // {section fields}
    // ****************
    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ValidatorManager.class);
    
    static String rParent = "PARENT";
    static String rIdent = "IDENTIFIER";
    static String rType = "CONDITION_TYPE";
    static String rVal = "CONDITION_VALUE";
    static String rReq = "REQUIRED";
    static String rOpt = "OPTIONAL";
    static String rRx = "REGEX_EXPRESSION";
    static String rRm = "REGEX_MASK";
    static String rMin = "MIN";
    static String rMax = "MAX";
    static String rHelp = "HELP";
    
    private boolean debug = false;
    
    private Object root;
    private Class<?> rootClass;
    private Class<?> force;
    private String rootName; 
    private String rootHash;
    
    
    private HashMap<AbstractField<?>, Field> components;
    private HashMap<String, AbstractField<?>> componentNames;
    private HashMap<AbstractField<?>, HashSet<Validator>> optionalValidations = new HashMap<>();
    private HashMap<AbstractField<?>, HashSet<Validator>> optionalValidationsStatic = new HashMap<>();
    private IndexedContainer rules;
    
    private HashMap<ValidatorType, Object> activeConditions = new HashMap<ValidatorType, Object>();
    private ValueChangeListener changeListener;
    
    private ArrayList<FieldState> states = new ArrayList<FieldState>();

    private I18nManager i18n;
    
    private final static class caption extends I18nCB {
        static final I18nCB regex_error = set();
        static final I18nCB invalidDescription = set();
        static final I18nCB invalidOptionalDescription = set();
    }
    
    private final static class notification extends I18nNB {
        static final I18nNB required = set(ERROR, MC, -1);
    }
    
    private final static class message extends I18nMB {
        static final I18nMB check_invalid = set(Icon.ERROR, ButtonId.OK);
        static final I18nMB check_optional = set(Icon.WARN, ButtonId.SAVE, ButtonId.ABORT);
    }
    
    // {end fields}

    // {section constructors}
    // **********************
    
    public ValidatorManager(Object component) {
        
        this(component, null, false);
    }
    
    @SuppressWarnings("serial")
    public ValidatorManager(Object component, Class<?> force, boolean debug) {
        if(component == null) {
            throw new IllegalArgumentException("component must be specified");
        }
        
        i18n = new I18nManager(this);
        
        this.debug = debug;
        this.force = force;
        
        this.root = component;
        this.rootClass = traverseClass(root.getClass());
        this.rootName = escapeName(rootClass.getCanonicalName());
        
        generateHash();
        dbGetRules();
        iterateComponent();
        
        changeListener = new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                updateValidators();
            }
        };
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    
    public void setConstraint(ValidatorType validatorType, Object value) {

        // clear active listener if present
        if(activeConditions.get(validatorType) != null && activeConditions.get(validatorType) instanceof AbstractField) {
            AbstractField<?> field = (AbstractField<?>) activeConditions.get(validatorType);
            field.removeValueChangeListener(changeListener);
            activeConditions.remove(validatorType);
        }
        
        if(value != null && value instanceof AbstractField) {
            AbstractField<?> field = (AbstractField<?>) value;
            field.addValueChangeListener(changeListener);
        }
        
        activeConditions.put(validatorType, value);
        updateValidators();
        
    }
    
    public void addOptionalValidation(AbstractField<?> field, Validator val) {
        addOptionalValidation(field, val, true);
    }
    
    public void removeOptionalValidator(AbstractField<?> field, Validator val) {
        removeOptionalValidation(field, val, true);
    }
    
    public void removeAllOptionalValidators(AbstractField<?> field) {
        
        if(field != null) {
            for (Validator val : new CopyOnWriteArrayList<Validator>(field.getValidators())) {
                removeOptionalValidation(field, val, true);
            }
        }
    }
    
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    public boolean isValid() {
        
        HashMap<AbstractField<?>, String> list = getInvalid();
        
        if(list.size() == 0) {
            return true;
        }
        
        showPanel(message.check_invalid,
            caption.invalidDescription, 
            list.values(), null);
        
        return false;
    }
    
    public boolean isValidOptional(final OptionalSaveAction action) {
        
        HashMap<AbstractField<?>, String> list = getOptional();
        
        if(list.size() == 0) {
            return true;
        }
        
        showPanel(message.check_optional,
            caption.invalidOptionalDescription, 
            list.values(),
            new MessageBoxListener() {
                
                @Override
                public void buttonClicked(ButtonId btn) {
                    
                    if(action == null) {
                        return;
                    }
                    
                    if(btn == ButtonId.SAVE) {
                        action.onSave();
                        return;
                    }else {
                        action.onCancel();
                    }
                    
                }
            });
        
        
        return false;
    }
    
    public boolean isValidSilent() {
        return getInvalid().size() == 0;
    }
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    
    private void addOptionalValidation(AbstractField<?> field, Validator val, boolean perm) {
        
        HashMap<AbstractField<?>, HashSet<Validator>> map = perm ? optionalValidationsStatic : optionalValidations;
        
        if(map.get(field) == null) {
            map.put(field, new HashSet<Validator>());
        }
        map.get(field).add(val);
        
    }
    
    private void removeOptionalValidation(AbstractField<?> field, Validator val, boolean perm) {
        
        HashMap<AbstractField<?>, HashSet<Validator>> map = perm ? optionalValidationsStatic : optionalValidations;
        
        if(field != null) {
            if(val != null) {
                map.get(field).remove(val);
            }
        }
    }
    
    
    private void showPanel(I18nMB message, I18nCB description, Collection<String> captions, MessageBoxListener listener) {
        
        Panel panel = new Panel(i18n.get(description));
        VerticalLayout vl = new VerticalLayout();
        vl.setSpacing(true);
        for (String caption : captions) {
            Label lbl = new Label(caption);
            lbl.setContentMode(ContentMode.HTML);
            vl.addComponent(lbl);
        }
        vl.setMargin(true);
        panel.setContent(vl);
        i18n.messageBox(message, listener, panel);

        
    }
    
    private String formatError(AbstractField<?> field) {
        
        ErrorMessage err = field.getErrorMessage();
        return "<b>" + field.getCaption() + (err != null ? ":" : "") + "</b> " + (err != null ? err.getFormattedHtmlMessage() : ""); 
        
    }
    
    private HashMap<AbstractField<?>, String> getInvalid() {
        
        HashMap<AbstractField<?>, String> invalid = new HashMap<AbstractField<?>, String>();
        
        for (AbstractField<?> comp : components.keySet()) {
            if(!comp.isValid()) {
                
                // if there is a possible optional validator, check both sources and only commit if at least one is not listed
                if(optionalValidations.containsKey(comp) || optionalValidationsStatic.containsKey(comp)) {
                    
                    Collection<Validator> flv = comp.getValidators();
                    Collection<Validator> opts = new ArrayList<Validator>();
                    if(optionalValidations.get(comp) != null) {
                        opts.addAll(optionalValidations.get(comp));
                    }
                    if(optionalValidationsStatic.get(comp) != null) {
                        opts.addAll(optionalValidationsStatic.get(comp));
                    }
                    
                    for (Validator validator : flv) {
                        
                        try {
                            validator.validate(comp.getValue());
                        } catch (InvalidValueException e) {
                            if(!opts.contains(validator)) {
                                invalid.put(comp, formatError(comp));
                                break;
                            }
                        }
                        
                    }
                    
                    
                }else { // default invalid handling when no optional validators registered
                    invalid.put(comp, formatError(comp));
                }
                
                
            }
        }
        
        return invalid;
    }
    
    private HashMap<AbstractField<?>, String> getOptional() {
        
        HashMap<AbstractField<?>, String> optional = new HashMap<AbstractField<?>, String>();
        
        for (AbstractField<?> comp : components.keySet()) {
            if(!comp.isValid() && (optionalValidations.containsKey(comp) || optionalValidationsStatic.containsKey(comp))) {
                optional.put(comp, formatError(comp));
            }
        }
        
        return optional;
        
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
    
    private void generateHash() {
        //generate md5 programatically instead of during query
        StringBuffer sb = new StringBuffer();
        try {
            for (byte b : MessageDigest.getInstance("MD5").digest(rootName.getBytes("UTF-8"))) {
                sb.append(String.format("%02x", b & 0xff));
            }
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            log.error("error calculating md5 hash! {}", e);
            return;
        }
        rootHash = sb.toString();
    }
    
    @SuppressWarnings("unchecked")
    private void iterateComponent() {
        
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
                    
                    components = new HashMap<AbstractField<?>, Field>(); 
                    componentNames = new HashMap<String, AbstractField<?>>();
        
                   
                    for (Field field : fields) {
                        
                        field.setAccessible(true);
                        Object comp = field.get(root);
                        
                        if(debug) {
                            log.info("field: {}, {}", field, field.getName());
                        }
                        
                        if(comp instanceof AbstractField) {
                            components.put((AbstractField<?>) comp, field);
                            componentNames.put(field.getName(), (AbstractField<?>) comp);
                            
                            ((AbstractField<?>) comp).setImmediate(true);
                        }
                        
                        if(comp instanceof ComboBox) {
                            ((ComboBox)comp).setFilteringMode(FilteringMode.CONTAINS);
                        }
                    }    
                    
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        dbUpdate();
        
    }
    
    @SuppressWarnings("serial")
    private void updateValidators() {
        
        for (FieldState state : states) {
            state.restore();
        }
        states = new ArrayList<FieldState>();
        rules.removeAllContainerFilters();
        
        ArrayList<ConditionFilter> filters = new ArrayList<ConditionFilter>();
        
        for (Entry<ValidatorType, Object> condition : activeConditions.entrySet()) {
            
            ValidatorType acType = condition.getKey();
            Object acValue = condition.getValue();

            if(acValue instanceof AbstractField) {
                acValue = ((AbstractField<?>) acValue).getValue();
            }
            
            filters.add(new ConditionFilter(acType, acValue));
            
        }
        
        rules.addContainerFilter(new Or(filters.toArray(new ConditionFilter[] {})));
        
        optionalValidations.clear();
        
        for (Object iid : rules.getItemIds()) {
            setValidator(rules.getItem(iid));
        }
    }
    
    @SuppressWarnings("serial")
    private void setValidator(Item item) {
        
        AbstractField<?> field = componentNames.get(MyUtils.getValueFromItem(item, rIdent, String.class));
        if(field == null) {
            return;
        }
        
        // setup rollback option
        FieldState state = new FieldState(field);

        // required
        field.setRequired(field.isRequired() || MyUtils.getValueFromItem(item, rReq, Boolean.class));
        
        //optional
        boolean optional = MyUtils.getValueFromItem(item, rOpt, Boolean.class);

        // regex
        if (field instanceof AbstractTextField
            && MyUtils.getValueFromItem(item, rRx, String.class) != null) {
            
            // see https://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html
            String mask = MyUtils.getValueFromItem(item, rRm, String.class);
            
            RegExtendedValidator val = new RegExtendedValidator((AbstractTextField) field,
                MyUtils.getValueFromItem(item, rRx, String.class),
                i18n.get(caption.regex_error), mask);
            if(optional) {
                addOptionalValidation(field, val, false);
            }
            
            state.addValidator(val);
        }

        // min / max
        if(MyUtils.getValueFromItem(item, rMin, BigDecimal.class) != null
            || MyUtils.getValueFromItem(item, rMax, BigDecimal.class) != null) {
            BigDecimal min = MyUtils.getValueFromItem(item, rMin, BigDecimal.class);
            BigDecimal max = MyUtils.getValueFromItem(item, rMax, BigDecimal.class);
            
            MinMaxValidator val = new MinMaxValidator(field, min, max);
            if(optional) {
                addOptionalValidation(field, val, false);
            }
            
            state.addValidator(val);
            
            
        }
        
        states.add(state);
    }
    
    // {end privatemethods}

    // {section database}
    // ******************
    
    private void dbUpdate() {
        
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "";
        
        // maintain parent component directory
        sql = "insert into cms_validator_components (`KEY`, NAME)"
            + "\n " + "VALUES ('"+rootHash+"', '"+rootName+"') ON DUPLICATE KEY UPDATE `KEY` = `KEY`";
        q.setSqlString(sql);
        q.db.debugNextQuery(debug);
        q.update();
        
        // maintain defaults
        ArrayList<String> values = new ArrayList<String>();
        for (Field field : components.values()) {
            values.add("('"+rootHash+"', '"+field.getName()+"')");
        }
        sql = "INSERT INTO cms_validator_fields (PARENT, IDENTIFIER)"
            + "\n " + "VALUES " + Joiner.on(",\n").join(values)
            + "\n " + "ON DUPLICATE KEY UPDATE PARENT=PARENT";
        q.setSqlString(sql);
        q.db.debugNextQuery(debug);
        q.update();
        
    }
    
    private void dbGetRules() {
        
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "select * from cms_validator_rules"
            + "\n " + "where  PARENT = '"+rootHash+"'";
        q.setSqlString(sql);
        
        rules = (IndexedContainer) q.query();
        
    }
    // {end database}

    // {section classes}
    // ****************
    public abstract static class OptionalSaveAction{
        public abstract void onSave();
        public void onCancel() {};
    }
    // {end layout}
}
