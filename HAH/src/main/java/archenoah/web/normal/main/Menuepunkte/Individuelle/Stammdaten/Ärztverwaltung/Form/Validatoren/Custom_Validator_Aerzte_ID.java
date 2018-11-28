package archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.Ärztverwaltung.Form.Validatoren;

import archenoah.config.CMS_Config_Std;
import archenoah.lib.tool.comunication.dbclass.DBClass;

import com.vaadin.data.Container;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;

public class Custom_Validator_Aerzte_ID implements Validator {

	public Custom_Validator_Aerzte_ID() {
		// TODO Automatisch generierter Konstruktorstub
	}

	@Override
	public void validate(Object value) throws InvalidValueException {
		// TODO Automatisch generierter Methodenstub
		if (!isValid(value))
            throw new InvalidValueException("Achtung die ID ist schon vergeben!");
		
		
	}

	
	
	
	
	public boolean isValid(Object value) {
		// TODO Automatisch generierter Methodenstub
		
		CMS_Config_Std std = CMS_Config_Std.getInstance();
		DBClass db = new DBClass();
		
		db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_All();
		db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Einzeln("cust_arzt_stammdaten_arzt");
		db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cust_arzt_stammdaten_arzt", "ASA_C_ID", "=", "'"+value.toString()+"'", "");
		
		Container con = db.DB_Data_Get.DB_SEND_AND_GET_Container();
		
		
		if(con.size() > 0)
		{
			return false;
		}else
		{
			return true;
		}
		
	}
}