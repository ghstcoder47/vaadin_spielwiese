package archenoah.web.normal.main.Menuepunkte.Standard.Administration.Benutzerverwaltung.Validatoren;

import com.vaadin.data.Validator;
import com.vaadin.ui.PasswordField;

public class password_matching implements Validator {

    private PasswordField match;

    public password_matching(PasswordField arg_match) {
        match = arg_match;
    }

    @Override
    public void validate(Object value) throws InvalidValueException {
        if (isValid(value) == false) {
            throw new InvalidValueException("Die Passwörter stimmen nicht überein!");
        }
    }

    public boolean isValid(Object value) {

        if (value.equals(match.getValue()) == true) {
            return true;
        } else {
            return false;
        }

    }
}
