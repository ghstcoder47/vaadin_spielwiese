package archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.Patientenverwaltung.Form.Validatoren;

import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;

public class Custom_Validator_plz implements Validator {

	public Custom_Validator_plz() {
		// TODO Automatisch generierter Konstruktorstub
	}

	@Override
	public void validate(Object value) throws InvalidValueException {
		// TODO Automatisch generierter Methodenstub
		if (!isValid(value))
            throw new InvalidValueException("Es dürfen nur Zahlen 0-9 verwendet werden!");
		
		
	}

	
	
	
	
	public boolean isValid(Object value) {
		// TODO Automatisch generierter Methodenstub
		
	
		if(value.toString().matches("\\d*")==true){
			return true;
		    }
		else
		{
		return false;
		}
	}
}