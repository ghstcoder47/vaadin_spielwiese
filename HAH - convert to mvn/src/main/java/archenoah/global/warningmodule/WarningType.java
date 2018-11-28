package archenoah.global.warningmodule;

import archenoah.lib.vaadin.Language.i18n.I18nEnum;


public interface WarningType extends I18nEnum {
    
    public enum protocols implements WarningType{
        unaccepted, // assigned protocol not accepted
        late, // late for CS
        controlled // checked by cs
    }
    
    public enum shipping implements WarningType{
        
        ordered, // TBD
        received // sent ticket not marked as received
        
      //skipped, // ticket date later than today 
      //order, // ticket date this week
      //review // open tickents in past month
    }
    
    public enum tasks implements WarningType{
//        open // TBI
    }
    
}
