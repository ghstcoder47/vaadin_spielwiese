package archenoah.web.normal.main.Menuepunkte.Individuelle.TaskSystem.TaskManager.classes;

public enum TTYPE{
    
    TASK("TYPE_TASK"),
    CAPA("TYPE_CAPA");
    
    private final String name;
    
    private TTYPE(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    public String toSqlString() {
        return "'" + name + "'";
    }
    
    public static TTYPE getTType(String value) {
        
        switch (value) {
        case "TYPE_TASK":
            return TASK;
            
        case "TYPE_CAPA":
            return CAPA;

        default:
            return TASK;
        }

    }
}
