package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.servicetasking.modules.converters;

import java.util.Date;

import archenoah.lib.custom.MyUtils;

public class DatetimeConverter implements ModuleFieldConverter<java.util.Date, java.sql.Date>{

    @Override
    public String convertForDatabase(Date value) {
        return MyUtils.formatSqlDate(value);
    }

    @Override
    public Date convertForField(java.sql.Date databaseValue) {
        return new java.util.Date(databaseValue.getTime());
    }

    
}
