package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.forsteo.Form.Validatoren;

import com.vaadin.data.Validator;

public class wert_nicht_null implements Validator {

	@Override
    public void validate(Object value) throws InvalidValueException {
        // Simply call the isValid(). It is possible to have
        // more complex logic here to also report the reason
        // of the failure in better detail.
        if (!isValid(value))
            throw new InvalidValueException("You did not greet");
    }


    public boolean isValid(Object value) {
        if (value instanceof String &&
                ((String)value).equals("hello"))
            return true;
        return false;
    }

}
