package archenoah.lib.custom;

import org.apache.commons.lang3.StringUtils;

public class CustomStyles {

    // {section fields}
    // ****************
    
    public enum HIGHLIGHT{
        
        PRIMARY("primary"),
        RED("red"),
        ORANGE("orange"),
        GREEN("green"),
        BLUE("blue");
        
        String value;
        
        HIGHLIGHT(String value){
            this.value = value;
        }
        
        @Override
        public String toString(){
            return "custom-styles-highlight-" + this.value;
        }
        
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
    
    public static String get(HIGHLIGHT... highlights) {
        
        return StringUtils.join(highlights, " ");
        
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
