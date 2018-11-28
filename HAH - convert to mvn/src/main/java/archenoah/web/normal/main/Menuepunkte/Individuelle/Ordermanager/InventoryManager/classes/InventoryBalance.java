package archenoah.web.normal.main.Menuepunkte.Individuelle.Ordermanager.InventoryManager.classes;

import archenoah.config.CMS_Config_Std;
import archenoah.lib.tool.comunication.dbclass.DBClass;

import com.vaadin.data.Item;

public class InventoryBalance {
	
    protected Boolean debug = false;
    
    protected String userId;
    protected String countryCode;
    protected CMS_Config_Std std;
    
    public static final String UNKNOWN = "0";
    public static final String ACTION_INTAKE = "1";
    public static final String ACTION_TICKET = "2";
    public static final String ACTION_MANUAL = "3";
    public static final String CUSTOM_RETURN = "101";
    public static final String CUSTOM_TRANSFER = "102";
    public static final String CUSTOM_REGRESS = "103";
    public static final String CUSTOM_OTHER= "104";

    
    String table = "cust_stock_inventory_balance";
    	

    public InventoryBalance(){
        std = CMS_Config_Std.getInstance();
    }

    public void setDebug(Boolean debug) {
        this.debug = debug;
    }
    
    public String getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId.toString();
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
	
	public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Integer getBalance(Integer productId) throws IllegalStateException {
		
        if(this.countryCode == null){
            throw new IllegalStateException("countryCode is not set");
        }
        
		DBClass db = new DBClass();
		db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Einzeln(table);
		db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_All();
		
		db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein(table, "SIB_ID_PRODUCT", "=", productId.toString(), "AND ");
		db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein(table, "SIB_COUNTRY", "=", "'" + countryCode + "'", "");
		
		db.DB_Data_Get.DB_Ordnen.DB_Ordnen("SIB_CREATED", "DESC");
		db.DB_Data_Get.DB_Ordnen.DB_Ordnen("SIB_ID", "DESC");
		
		db.DB_Data_Get.DB_Limit.DB_LIMIT("1");
		
		
		db.debugNextQuery(debug);
		Item item = db.DB_Data_Get.DB_SEND_AND_GET_FIRST_ITEM();    
		
		Integer balance = (item == null) ? 0 : (Integer) item.getItemProperty("SIB_BALANCE").getValue();
		
		return balance;
		
	}
	
    /**
     * 
     * @param productId
     * @param delta (postitive Integer)
     * @return Inserted Id on success, 0 otherwise
     * @throws IllegalArgumentException
     */
    
    public Integer addBalance(Integer productId, Integer delta, String action) throws NumberFormatException {
        if(delta <= 0){
            throw new NumberFormatException ("delta cannot be negative");
        }
        
        return editBalance(productId, delta, action);
    }
    
    /**
     * 
     * @param productId
     * @param delta (postitive Integer)
     * @return Inserted Id on success, 0 otherwise
     * @throws IllegalArgumentException
     */

    public Integer removeBalance(Integer productId, Integer delta, String action) throws NumberFormatException {
        if(delta <= 0){
            throw new NumberFormatException ("delta cannot be negative");
        }
        
        return editBalance(productId, delta * -1, action);
    }
    
    public Integer editBalance(Integer productId, Integer delta, String action) throws IllegalStateException{
        return editBalance(productId, delta, action, null, null);
    };
    
    public Integer editBalance(Integer productId, Integer delta, String action, String comment) throws IllegalStateException{
        return editBalance(productId, delta, action, comment, null);
    };
    
	public Integer editBalance(Integer productId, Integer delta, String action, String comment, String dataId) throws IllegalStateException{
		
		if(this.userId == null){
			throw new IllegalStateException("userId is not set");
		}
		
        if(this.countryCode == null){
            throw new IllegalStateException("countryCode is not set");
        }
		
		Integer balance = null;
		
		try {
		    balance = getBalance(productId);
        } catch (Exception e) {
            throw new IllegalStateException("previous balance not found");
        }
		
		balance += delta;
		
		DBClass db = new DBClass();
		db.DB_Data_Insert.DB_Tabellen.DB_set_Tabelle_Einzeln(table);
		db.DB_Data_Insert.DB_Daten.DB_Data(table, "SIB_ID_USER", userId);
		db.DB_Data_Insert.DB_Daten.DB_Data(table, "SIB_COUNTRY", countryCode);
		db.DB_Data_Insert.DB_Daten.DB_Data(table, "SIB_ID_PRODUCT", Integer.toString(productId));
		db.DB_Data_Insert.DB_Daten.DB_Data(table, "SIB_DELTA", Integer.toString(delta));
		db.DB_Data_Insert.DB_Daten.DB_Data(table, "SIB_BALANCE", balance.toString());
		db.DB_Data_Insert.DB_Daten.DB_Data(table, "SIB_ACTION", action);
		db.DB_Data_Insert.DB_Daten.DB_Data(table, "SIB_DATA", dataId);
		db.DB_Data_Insert.DB_Daten.DB_Data(table, "SIB_COMMENT", comment);
		
		db.debugNextQuery(debug);
		
		return db.DB_Data_Insert.DB_Insert();
	}

}
