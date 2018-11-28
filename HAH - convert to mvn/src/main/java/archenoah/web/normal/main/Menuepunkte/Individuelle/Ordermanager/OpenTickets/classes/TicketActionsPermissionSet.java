package archenoah.web.normal.main.Menuepunkte.Individuelle.Ordermanager.OpenTickets.classes;

import java.util.HashMap;

public class TicketActionsPermissionSet {


    // {section fields}
    // ****************
    public enum ACTION{
        CONTENTS_CREATE,
        CONTENTS_UPDATE,
        CONTENTS_MOVE,
        DETAILS,
        TYPE,
        STATUS,
        ADDRESS
    };
    private HashMap<ACTION, Boolean> permissions = new HashMap<ACTION, Boolean>();
    // {end fields}

    // {section constructors}
    // **********************
    public TicketActionsPermissionSet(Boolean bool) {
        for (ACTION action : ACTION.values()) {
            setPermission(action, bool);
        }
    }
    
    public TicketActionsPermissionSet() {
        this(false);
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    public Boolean getPermission(ACTION perm) {
        return permissions.get(perm);
    }
    public void setPermission(ACTION perm, Boolean bool) {
        permissions.put(perm, bool);
    }
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
