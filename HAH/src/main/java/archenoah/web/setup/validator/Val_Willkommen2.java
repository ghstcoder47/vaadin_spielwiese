package archenoah.web.setup.validator;


import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Length.List;

  


public class Val_Willkommen2{

	
	String Keine_Validierung;
	
	@List({
	    @Length(min = 3, message = "Die Postleitzahl muss mindestens 3 Zahlen erhalten"),
	    @Length(max = 10, message = "Die Postleitzahl darf höchstens 10 Zahlen erhalten"),
	})
	@Pattern(regexp="[0-9]*", message="Sie dürfen nur Zahlen 0-9 verwenden")
	
	String PLZ;
	
	

	String Ort;
	
	
	@List({
	    @Length(min = 2, message = "Die Telefonnummer muss mindestens 2 Zahlen erhalten"),
	    @Length(max = 15, message = "Die Telefonnummer darf höchstens 15 Zahlen erhalten"),
	})
	@Pattern(regexp="[0-9]*", message="Sie dürfen nur Zahlen 0-9 verwenden")
	
	String Tel;
	
	
	@Size(max = 50 ,message = "Ihre Zeichenkette ist zu lang!")
	@Email
	String Email;
	
	
	
	

	

}
