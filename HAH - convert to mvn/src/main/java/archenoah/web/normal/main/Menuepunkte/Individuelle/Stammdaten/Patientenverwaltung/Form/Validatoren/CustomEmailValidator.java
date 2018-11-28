package archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.Patientenverwaltung.Form.Validatoren;

import com.vaadin.data.validator.RegexpValidator;

public class CustomEmailValidator extends RegexpValidator{

    public CustomEmailValidator(String errorMessage) {
        super(
            "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$",
                true, errorMessage);
    }
    
}
