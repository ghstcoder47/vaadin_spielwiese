package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.pop.classes;

import archenoah.lib.vaadin.Language.i18n.I18nConverter;

public class I18nPoPProcessConverter extends I18nConverter{

    public I18nPoPProcessConverter() {
        super("cust_schulung_status where SS_CODE = 'POP'", "SS_PROZESS", "SS_NAME");
    }

}
