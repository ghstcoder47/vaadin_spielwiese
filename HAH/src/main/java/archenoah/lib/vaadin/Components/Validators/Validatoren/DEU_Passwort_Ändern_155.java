package archenoah.lib.vaadin.Components.Validators.Validatoren;


import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Length.List;



public class DEU_Passwort_Ändern_155 {

	@List({
	    @Length(min = 5, message = "Das Passwort muss mindestens 5 Zeichen erhalten"),
	    @Length(max = 15, message = "Das Passwort darf höchstens 20 Zeichen erhalten"),
	})
	
	String Passwort;
	
	
	@List({
	    @Length(min = 5, message = "Das Passwort muss mindestens 5 Zeichen erhalten"),
	    @Length(max = 15, message = "Das Passwort darf höchstens 20 Zeichen erhalten"),
	})
	
	
	@Pattern(regexp = ".*\\.jpg|.*\\.jpeg|.*\\.gif",
    message="Only images of type JPEG or GIF are supported.")
	
	String Passwort2;
	
	

	  
	
	
}
