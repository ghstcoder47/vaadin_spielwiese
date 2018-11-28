package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.expenses.classes;

import archenoah.lib.vaadin.Language.i18n.I18nConverter;


public class I18nCEDStatusConverter extends I18nConverter {
    
    public I18nCEDStatusConverter() {
        super("cust_schulung_status where SS_CODE = 'EXP'", "SS_PROZESS", "SS_NAME");
    }
}
