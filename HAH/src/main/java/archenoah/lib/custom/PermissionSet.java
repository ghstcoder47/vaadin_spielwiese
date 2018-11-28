package archenoah.lib.custom;

import java.util.HashMap;

public class PermissionSet {

    // {section fields}
    // ****************
    public enum PERMISSION{
        CREATE,
        READ,
        UPDATE,
        DELETE
    };
    private HashMap<PermissionSet.PERMISSION, Boolean> permissions = new HashMap<PermissionSet.PERMISSION, Boolean>();
    // {end fields}

    // {section constructors}
    // **********************
    public PermissionSet(Boolean create, Boolean read, Boolean update, Boolean delete) {
        fillPermissions(create, read, update, delete);
    }
    
    public PermissionSet(Integer create, Integer read, Integer update, Integer delete) {
        fillPermissions(intToBool(create), intToBool(read), intToBool(update), intToBool(delete));
    }
    public PermissionSet() {
        this(false, false, false, false);
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    
    public Boolean getCreate() {
        return getPermission(PERMISSION.CREATE);
    }
    public Boolean getRead() {
        return getPermission(PERMISSION.READ);
    }
    public Boolean getUpdate() {
        return getPermission(PERMISSION.UPDATE);
    }
    public Boolean getDelete() {
        return getPermission(PERMISSION.DELETE);
    }
    
    public void setCreate(Boolean bool) {
        permissions.put(PERMISSION.CREATE, bool);
    }
    public void setRead(Boolean bool) {
        permissions.put(PERMISSION.READ, bool);
    }
    public void setUpdate(Boolean bool) {
        permissions.put(PERMISSION.UPDATE, bool);
    }
    public void setDelete(Boolean bool) {
        permissions.put(PERMISSION.DELETE, bool);
    }
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    public Boolean getPermission(PERMISSION perm) {
        return permissions.get(perm);
    }
    public void setPermission(PERMISSION perm, Boolean bool) {
        permissions.put(perm, bool);
    }
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    private void fillPermissions(Boolean create, Boolean read, Boolean update, Boolean delete) {
        setPermission(PERMISSION.CREATE, create);
        setPermission(PERMISSION.READ, read);
        setPermission(PERMISSION.UPDATE, update);
        setPermission(PERMISSION.DELETE, delete);
    }
    
    private Boolean intToBool(Integer i) {
        return (i == 1) ? true : false;
    }
    // {end privatemethods}

}
