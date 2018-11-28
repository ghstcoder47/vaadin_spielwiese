package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.vpriv.classes;

import archenoah.lib.vaadin.Language.i18n.I18nConverter;

public class I18nVPRIVStatusConverter extends I18nConverter {
    public I18nVPRIVStatusConverter() {
        super("cust_schulung_status where SS_CODE = 'VPRIV'", "SS_PROZESS", "SS_NAME");
    }
}
