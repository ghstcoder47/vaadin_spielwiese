package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.servicetasking.modules.converters;

public class BooleanConverter implements ModuleFieldConverter<Boolean, Integer>{

	@Override
	public String convertForDatabase(Boolean value) {

		if(value != null && value)
			return "1";
		else
			return "0";
	}

	@Override
	public Boolean convertForField(Integer databaseValue) {

		if(databaseValue != null & databaseValue == 1)
			return true;
		else
			return false;
	}




}
