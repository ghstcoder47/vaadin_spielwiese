package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.servicetasking.modules.converters;


public interface ModuleFieldConverter<T, D> {
    /**
     * 
     * @param value
     * @return String representation of the value compatible with sql insert<br />
     * String quotes are included automatically in ModuleCOmponent
     */
    abstract String convertForDatabase(T value);
    /**
     * Convert DB value to special presentation form if needed
     * @param databaseValue
     * @return
     */
    abstract T convertForField(D databaseValue);
}
