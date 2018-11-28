package archenoah.web.normal.main.Menuepunkte.Individuelle.Ordermanager.classes;

import archenoah.global.UserAttributes;

public class UserAttributesCountry extends UserAttributes{

    // {section fields}
    // ****************
    // {end fields}

    // {section constructors}
    // **********************
    public UserAttributesCountry() {
        super();
        addAttribute("ordermanager_country");
    }
    
    public UserAttributesCountry(Integer userId) {
        super(userId);
        addAttribute("ordermanager_country");
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    // {end privatemethods}

    // {section database}
    // ******************
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
