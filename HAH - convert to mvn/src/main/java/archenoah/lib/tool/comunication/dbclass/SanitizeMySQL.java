package archenoah.lib.tool.comunication.dbclass;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SanitizeMySQL {

    // {section fields}
    // ****************
    static Map<String, String> values;
    static {
        HashMap<String, String> aMap = new HashMap<String, String>();
        aMap.put("00", "\\0");
        aMap.put("08", "\\b");
        aMap.put("09", "\\t");
        aMap.put("0a", "\\n");
        aMap.put("0d", "\\r");
        aMap.put("1a", "\\Z");
        aMap.put("22", "\\\"");
        aMap.put("25", "%"); // not needed?
        aMap.put("27", "\\'");
        aMap.put("5c", "\\\\");
        aMap.put("5f", "_"); // not needed?
        values = Collections.unmodifiableMap(aMap);
    }
    // {end fields}

    // {section constructors}
    // **********************
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    public static String escape(String string) {
        StringBuilder build = new StringBuilder();
        
        for (int i = 0; i < string.length(); i++) {
            
            char c = string.charAt(i);
            
            String r = values.get(Integer.toHexString(c));
            
            if(r == null) {
                r = ((c < 256 && !(Character.isDigit(c) || Character.isLetter(c))) ? "\\" : "") + String.valueOf(c);
            }
            build.append(r);
        }
        
        return build.toString();
    }
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    // {end privatemethods}

    // {section database}
    // ******************
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
