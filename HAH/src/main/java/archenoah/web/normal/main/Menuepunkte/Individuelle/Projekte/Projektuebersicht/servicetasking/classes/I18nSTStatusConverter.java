package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.servicetasking.classes;

import archenoah.lib.vaadin.Language.i18n.I18nConverter;


public class I18nSTStatusConverter extends I18nConverter {
    
    public I18nSTStatusConverter() {
        super("cust_schulung_status where SS_CODE = 'SA'", "SS_PROZESS", "SS_NAME");
    }
}
