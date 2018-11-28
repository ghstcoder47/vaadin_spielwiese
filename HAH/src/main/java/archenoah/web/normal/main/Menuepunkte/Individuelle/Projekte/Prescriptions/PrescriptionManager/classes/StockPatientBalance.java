package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Prescriptions.PrescriptionManager.classes;

import archenoah.lib.tool.comunication.dbclass.DBClass;

import com.vaadin.data.Item;

public class StockPatientBalance extends BalanceBase{
	
	String table = "cust_stock_patient_balance";

	@Override
	public Integer getBalance(Integer productId) throws IllegalStateException {
		
		if(this.patientId == null){
			throw new IllegalStateException("patientId is not set");
		}
		
		DBClass db = new DBClass();
		db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Einzeln(table);
		db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_All();
		
		db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein(table, "SPB_ID_PATIENT", "=", patientId, "AND");
		db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein(table, "SPB_ID_PRODUCT", "=", productId.toString(), "");
		
		db.DB_Data_Get.DB_Ordnen.DB_Ordnen("SPB_CREATED", "DESC");
		
		
		db.debugNextQuery(debug);
		
		Item item = db.DB_Data_Get.DB_SEND_AND_GET_FIRST_ITEM();
		
		Integer balance = (item == null) ? 0 : (Integer) item.getItemProperty("SPB_BALANCE").getValue();
		
		return balance;
		
	}
	
	@Override
	public Integer editBalance(Integer productId, Integer delta, String action) throws IllegalStateException{
		
		if(this.patientId == null){
			throw new IllegalStateException("patientId is not set");
		}
		
		Integer balance = getBalance(productId);
		balance += delta;
		
		DBClass db = new DBClass();
		db.DB_Data_Insert.DB_Tabellen.DB_set_Tabelle_Einzeln(table);
		db.DB_Data_Insert.DB_Daten.DB_Data(table, "SPB_ID_PATIENT", patientId);
		db.DB_Data_Insert.DB_Daten.DB_Data(table, "SPB_ID_PRODUCT", Integer.toString(productId));
		db.DB_Data_Insert.DB_Daten.DB_Data(table, "SPB_DELTA", Integer.toString(delta));
		db.DB_Data_Insert.DB_Daten.DB_Data(table, "SPB_BALANCE", balance.toString());
		db.DB_Data_Insert.DB_Daten.DB_Data(table, "SPB_ACTION", action);
		
		return db.DB_Data_Insert.DB_Insert();
	}

}
