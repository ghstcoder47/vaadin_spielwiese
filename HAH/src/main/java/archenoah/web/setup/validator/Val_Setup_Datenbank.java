package archenoah.web.setup.validator;

import javax.validation.constraints.Pattern;

public class Val_Setup_Datenbank {

	String Keine_Validierung;
	
	@Pattern(regexp="[0-9]*", message="Sie dürfen nur Zahlen 0-9 verwenden")
	String Port;

}
