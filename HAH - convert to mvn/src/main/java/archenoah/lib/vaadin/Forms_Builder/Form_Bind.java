package archenoah.lib.vaadin.Forms_Builder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.LocalDateTime;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import archenoah.config.CMS_Config_Std;
import archenoah.lib.tool.comunication.dbclass.DBClass;

import com.vaadin.data.Container;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Component;

public class Form_Bind {

	private Boolean             Zurücksetzen = false;

	private Set<Object>         keys;
	private Object              Classes;
	private String              Classname;
	private String[]            DB_Data;
	private String              Keyname;

	// Argumente

	private Container           con;
	private Map<Object, String> Fill;
	private DBClass             db;
	private AbstractField       af;

	private SQLContainer        colTypes;

	static PeriodFormatter timeFormatter = new PeriodFormatterBuilder()
    .minimumPrintedDigits(2)
    .printZeroAlways()
    .appendHours()
    .appendSuffix(":")
    .appendMinutes()
    .appendSuffix(":")
    .appendSeconds()
    .toFormatter();
	
	org.slf4j.Logger log;
	
	public Form_Bind() {
	    log = org.slf4j.LoggerFactory.getLogger(this.getClass());
	}

	public void Get_and_Fill(Container Arg_con, Map<Object, String> Arg_Fill) {
		con = Arg_con;

		Fill = Arg_Fill;
		Data_Transfer(1);
	}

	public void Update(DBClass Arg_db, Map<Object, String> Arg_Fill) {
		db = Arg_db;
		Fill = Arg_Fill;
		Data_Transfer(2);
	}

	public void Insert(DBClass Arg_db, Map<Object, String> Arg_Fill) {
		db = Arg_db;
		Fill = Arg_Fill;
		Data_Transfer(3);
	}

	/******************** --->Private Function<--- ********************/
	private void Prüfung_Read_Onyl(Component comp) {
		if (comp.isReadOnly() == true) {
			comp.setReadOnly(false);
			Zurücksetzen = true;

			return;
		}

		if (Zurücksetzen == true) {
			comp.setReadOnly(true);
			Zurücksetzen = false;
		}
	}

	private Object Get_Daten_From_Container(Container con, String Keyname) {
		String Classname = null;
		Object Wert = null;
		
		Object itemId = con.getItemIds().iterator().next();
		
		Wert = con.getItem(itemId).getItemProperty(Keyname).getValue();
		if(Wert == null){
			return null;
		}
		Classname = con.getItem(itemId).getItemProperty(Keyname).getValue().getClass().getName();
		
//		System.out.println("Classname: " + Classname + ", Keyname: " + Keyname);
		switch (Classname) {

		case "java.lang.Integer":

			return (int) Wert;

		case "java.lang.String":

			return Wert;

		case "java.lang.Boolean":

			return Wert;

		case "java.lang.Long":

			return Wert;
			
		case "java.lang.Float":
		    
		    return Wert;

		case "java.sql.Date":

			if (Wert.toString().equals("1900-01-01 00:00:00.0") == true) {

				return null;
			}
			else {
				return Wert;
			}

		case "java.sql.Timestamp":

			if (Wert.toString().equals("1900-01-01 00:00:00.0") == true) {

				return null;
			}
			else {
				return Wert;
			}

			// case "java.sql.Time":
			// return Wert.toString();
			
	     case "java.sql.Time":

	            if (Wert.toString().equals("00:00:00.0") == true) {

	                return null;
	            }
	            else {
	                
	                Duration dur = new Duration(new LocalDateTime(Wert).toDateTime(DateTimeZone.UTC).getMillis());
	                return timeFormatter.print(dur.toPeriod());
	                
	            }
	     case "java.math.BigDecimal":
	    	 
	    	 return Wert;
			
		default:

			return null;
		}

	}

	private String Get_Daten_From_Component(Object ob, String Spalte, String Tabelle) {
		String Typ;
		DateFormat dfa = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if(ob == null){
			return null;
		}
		
		
		Typ = getColumnType(Tabelle, Spalte);
		if(Typ == null) {
		    return null;
		}
		switch (Typ) {

		case "date":
				return dfa.format(ob);

		case "datetime":
				return dfa.format(ob);

		case "timestamp":
				return dfa.format(ob);

		case "tinyint":
			if ((boolean) ob == true) {
				return "1";
			}
			else {
				return "0";
			}

		default:
			return ob.toString();
		}

	}

	private void Data_Transfer(int typ) {
		keys = Fill.keySet();

		if (db != null) {
			colTypes = db.DB_Data_Advanced.DB_Database_GET_ColumnsTYP(buildTableList(Fill));
		}

		for (Object singleKey : keys) {

			Classes = singleKey;
			String temp = Fill.get(singleKey).toString();

			DB_Data = temp.split(":");

			// System.out.print(Classes.getClass().getName() +" \n");

			Classname = Classes.getClass().getName();

			if (typ == 1) {
				Prüfung_Read_Onyl((Component) singleKey);
			}

			Keyname = DB_Data[1];

			// ******************* Compenenten Auswahl *******************/

			// System.out.println(Classname);

			af = (AbstractField) singleKey;

			switch (typ) {
			case 1: // Fill Form
				
				if(af instanceof AbstractTextField){
					((AbstractTextField) af).setNullRepresentation("");
				}
				
				af.setValue(Get_Daten_From_Container(con, Keyname));
				
				break;
			case 2:
				db.DB_Data_Update.DB_Daten.DB_Data(DB_Data[0], Keyname, Get_Daten_From_Component(af.getValue(), Keyname, DB_Data[0]));
				break;
			case 3:

				db.DB_Data_Insert.DB_Daten.DB_Data(DB_Data[0], Keyname, Get_Daten_From_Component(af.getValue(), Keyname, DB_Data[0]));
				break;
			}

			if (typ == 1) {
				Prüfung_Read_Onyl((Component) singleKey);
			}

		}
	}

	private String Get_Column_TYP(String Columnname, String Tabelle) {
		String Wert = null;
		// System.out.println(Columnname);

		CMS_Config_Std std = CMS_Config_Std.getInstance();

		DBClass dba = new DBClass();

		SQLContainer cont = dba.DB_Data_Advanced.DB_Database_GET_ColumnsTYP(Tabelle, Columnname);

		for (Object ItemIda : cont.getItemIds()) {
			// UserId = (int) result.getItem(ItemIda).getItemProperty(this.Spalte_ID)
			//

			Wert = (String) cont.getItem(ItemIda).getItemProperty("DATA_TYPE").getValue();

		}
		return Wert;
	}

	private HashMap getColumTypes(String Tabelle) {

		HashMap<String, String> coltypes = new HashMap();

		CMS_Config_Std std = CMS_Config_Std.getInstance();
		DBClass dba = new DBClass();

		SQLContainer cont = dba.DB_Data_Advanced.DB_Database_GET_ColumnsTYP(Tabelle);

		for (Object Item : cont.getItemIds()) {

			coltypes.put((String) cont.getItem(Item).getItemProperty("COLUMN_NAME").getValue(),
			             (String) cont.getItem(Item).getItemProperty("DATA_TYPE").getValue());

		}

		return coltypes;
	}

	private ArrayList<String> buildTableList(Map<Object, String> map) {

		ArrayList<String> tableList = new ArrayList<String>();

		for (Map.Entry<Object, String> entry : map.entrySet()) {

			// Object key = entry.getKey();
			String value = entry.getValue();

			String[] split = value.split(":");
			String tbl = split[0];
			// String col = split[1];

			if (!tableList.contains(tbl)) {
				tableList.add(tbl);
			}

		}

		return tableList;
	}

	private String getColumnType(String table, String column) {

		String type = "";

		for (Object row : colTypes.getItemIds()) {

			String colName = colTypes.getItem(row).getItemProperty("COLUMN_NAME").getValue().toString();
			String tblName = colTypes.getItem(row).getItemProperty("TABLE_NAME").getValue().toString();

			// if (colName.compareTo(column) == 0 && tblName.compareTo(table) == 0) {
			if (colName.equals(column) && tblName.equals(table)) {

				type = colTypes.getItem(row).getItemProperty("DATA_TYPE").getValue().toString();

				break;
			}

		}

		if (type != "") {
			return type;
		}
		else {
			log.warn("table/column combination not found: {} : {}", table, column);
			return null;
		}

	}

	/***************************************************************/
}
