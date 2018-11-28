package archenoah.lib.tool.comunication.dbclass;

import java.util.HashMap;




public class Data_DB_Data extends Data_DB_Sammlung {

	protected  String[][]SQL_Daten= null;
	private HashMap<Integer, Boolean> wrapEntries = new HashMap<Integer, Boolean>();
	
	
	
	public Data_DB_Data() {
		// TODO Automatisch generierter Konstruktorstub
	}

	
	public void DB_Data(String Tabelle, String Spalte, String Wert) {
	    DB_Data(Tabelle, Spalte, Wert, true);
	}
	
	public void DB_Data(String Tabelle, String Spalte, String Wert, boolean wrap)
	{
		
		if(this.SQL_Daten == null)
		 {
			this.SQL_Daten = ArrayBuilderString(this.SQL_Daten, 4);
			 this.SQL_Daten[0][0] = Tabelle;
			 this.SQL_Daten[0][1] = Spalte;
			 this.SQL_Daten[0][2] = Wert;
			 this.SQL_Daten[0][3] = "";
			 
			 wrapEntries.put(0, wrap);
			 
		 }else
			 
		 {
			 this.SQL_Daten = ArrayBuilderString(this.SQL_Daten, 4);
			 this.SQL_Daten[this.SQL_Daten.length-1][0] = Tabelle; 
			 this.SQL_Daten[this.SQL_Daten.length-1][1] = Spalte;
			 this.SQL_Daten[this.SQL_Daten.length-1][2] = Wert;
			 this.SQL_Daten[this.SQL_Daten.length-1][3] = "";
			 
			 wrapEntries.put(this.SQL_Daten.length-1, wrap);
		 }
		
		
		
	}
	public void DB_Data(String Tabelle, String Spalte, String Wert,String Filepfad)
	{
	
		
		    
		
		if(this.SQL_Daten == null)
		 {
			this.SQL_Daten = ArrayBuilderString(this.SQL_Daten, 4);
			 this.SQL_Daten[0][0] = Tabelle;
			 this.SQL_Daten[0][1] = Spalte;
			 this.SQL_Daten[0][2] = "?";
			 this.SQL_Daten[0][3] = Filepfad;
			 
			 wrapEntries.put(0, false);
			 
		 }else
			 
		 {
			 this.SQL_Daten = ArrayBuilderString(this.SQL_Daten, 4);
			 this.SQL_Daten[this.SQL_Daten.length-1][0] = Tabelle; 
			 this.SQL_Daten[this.SQL_Daten.length-1][1] = Spalte;
			 this.SQL_Daten[this.SQL_Daten.length-1][2] = "?";
			 this.SQL_Daten[this.SQL_Daten.length-1][3] = Filepfad;
			 
			 wrapEntries.put(this.SQL_Daten.length-1, false);
		 }
		
		
		
	}
	
	protected String[][] get_Data() {
		// TODO Auto-generated method stub
		return SQL_Daten;
	}

	protected String func_Daten_Update()
	{
		if(SQL_Daten==null)
			
		{
			
			return "";
		}
		String SQL = "SET ";
		
		for(int i = 0;SQL_Daten.length>i;i++)
		{
			
			if(SQL_Daten[i][2]!="?")
			{
				SQL = SQL + ((i==0) ? "" : " , ") + SQL_Daten[i][0]+"."+SQL_Daten[i][1] + "=";
				SQL += (SQL_Daten[i][2] == null) ? "NULL" : wrapString(SQL_Daten[i][2], wrapEntries.get(i));
			}else
			{
				SQL = SQL + ((i==0) ? "" : " , ") + SQL_Daten[i][0]+"."+SQL_Daten[i][1] + "=" + SQL_Daten[i][2]+" ";
			}
			
			
		}
		
		
		
		return SQL;
		
	}
	
	private String wrapString(String value, boolean wrap) {
	    if(wrap) {
	        value = "'" + SanitizeMySQL.escape(value) + "'";
	    }
	    return value;
	}
	
	protected String func_Daten_INSERT()
	{
		if(SQL_Daten==null)
			
		{
			
			return "";
		}
		String SQL = " ";
	//	(column1, column2, column3,...)
	//	VALUES (value1, value2, value3,...)
		
		for(int i = 0;SQL_Daten.length>i;i++)
		{
			if(i==0)
			{
				SQL = SQL + "(" +SQL_Daten[i][0]+"."+SQL_Daten[i][1] + " ";
				
			}else
			{
				SQL = SQL +" , " +SQL_Daten[i][0]+"."+SQL_Daten[i][1] + " ";
			}
			
//			if(SQL_Daten[i][2]!="?")
//			{
//				SQL = SQL + ((i==0) ? "" : " , ") + SQL_Daten[i][0]+"."+SQL_Daten[i][1] + "=";
//				SQL += (SQL_Daten[i][2] == null) ? "NULL" : "'" + SQL_Daten[i][2]+"' ";
//			}else
//			{
//				SQL = SQL + ((i==0) ? "" : " , ") + SQL_Daten[i][0]+"."+SQL_Daten[i][1] + "=" + SQL_Daten[i][2]+" ";
//			}
			
			
		}
		
		SQL = SQL + " ) VALUES (";
		
		for(int k = 0;SQL_Daten.length>k;k++)
		{
			
			if(SQL_Daten[k][2]!="?")
			{
				SQL = SQL + ((k==0) ? "" : " , ");
				SQL += (SQL_Daten[k][2] == null) ? "NULL" : wrapString(SQL_Daten[k][2], wrapEntries.get(k));
			}else
			{
				SQL = SQL + ((k==0) ? "" : " , ") + SQL_Daten[k][2]+" ";
			}
			
			
			
//			if(k==0)
//			{
//							
//				if(SQL_Daten[k][2]!="?")
//				{
//					SQL = SQL +" '" +SQL_Daten[k][2] + "' ";
//				}else
//				{
//					SQL = SQL +" " +SQL_Daten[k][2] + " ";
//				}
//				
//				
//			}else
//			{
//				if(SQL_Daten[k][2]!="?")
//				{
//				SQL = SQL +" , '" +SQL_Daten[k][2] + "' ";
//				}else
//				{
//					SQL = SQL +" , " +SQL_Daten[k][2] + " ";
//				}
//				
//				
//			}
			
			
		}
		
		SQL = SQL + " ) ";
		
		
		
		return SQL;
		
	}
	
	
	public void DB_Data_Leeren()
	{
		SQL_Daten = null;
	}
	
	
}
