package archenoah.web.normal.main.Menuepunkte.Individuelle.Ordermanager.OpenTickets.classes;

import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.vaadin.Language.i18n.I18nCB;
import archenoah.lib.vaadin.Language.i18n.I18nManager;
/**
 * This needs to be be replaced by a more sensible solution in the future
 * @author Developer
 */
@Deprecated
public class SetQueryStatusLng {
	
//    private final String formIdLng = "105";
	
    private final static class caption extends I18nCB {
        static final I18nCB ticket_generic = set();
        static final I18nCB nurse_edit = set();
        static final I18nCB order_placed = set();
        static final I18nCB order_prepared = set();
        static final I18nCB nurse_sent = set();
        static final I18nCB nurse_received = set();
        static final I18nCB status_unknown = set();
    }

    private I18nManager i18n;
    
	public SetQueryStatusLng() {
	    i18n = new I18nManager(this);
	}

    public I18nManager getLC() {
        return i18n;
    }


	/**
	 * adds a language conversion CASE to <code>db</code> via <code>DB_set_Spalten_Einzeln</code>
	 * @param db
	 */
    public void setStatus(DBClass db){
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("CASE"
                + " when OMMS_VALUE = 'nurse_edit' then '" + i18n.get(caption.nurse_edit) + "'"
                + " when OMMS_VALUE = 'order_placed' then '" + i18n.get(caption.order_placed) + "'"
                + " when OMMS_VALUE = 'order_prepared' then '" + i18n.get(caption.order_prepared) + "'"
                + " when OMMS_VALUE = 'nurse_sent' then '" + i18n.get(caption.nurse_sent) + "'"
                + " when OMMS_VALUE = 'nurse_received' then '" + i18n.get(caption.nurse_received) + "'"
                
                + " else '" + i18n.get(caption.status_unknown) + "' end", "'STATUS'");
        
    }
	
}
