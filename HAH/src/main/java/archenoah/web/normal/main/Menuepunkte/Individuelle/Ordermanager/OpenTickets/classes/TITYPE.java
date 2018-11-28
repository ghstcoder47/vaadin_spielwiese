package archenoah.web.normal.main.Menuepunkte.Individuelle.Ordermanager.OpenTickets.classes;

import java.util.HashMap;
import java.util.Map;

public enum TITYPE{
    NURSE,
    PATIENT,
    GENERIC;
    
    private static final Map<String, TITYPE> lookup = new HashMap<String, TITYPE>();
    
    static {
        for (TITYPE t : TITYPE.values()) {
            lookup.put(t.name(), t);
        }
    }
    
    public static TITYPE get(String name) {
        return lookup.get(name);
    }
}
