package archenoah.lib.tool.comunication.dbclass;


public class Data_DB_Filter extends Data_DB_Sammlung{
	protected String[][]SQL_Where= null;
	
	
	public Data_DB_Filter() {
		// TODO Automatisch generierter Konstruktorstub
	}
	
	public void DB_WHERE_Allgemein(String Tabelle,String Spalten,String Methode,String Wert,String Verknuepfung)
	 {
		 if(this.SQL_Where == null)
		 {
			 
			 this.SQL_Where = ArrayBuilderString(SQL_Where, 6);
			 this.SQL_Where[0][0] = Tabelle; 
			 this.SQL_Where[0][1] = Spalten; 
			 this.SQL_Where[0][2] = Methode;
			 this.SQL_Where[0][3] = Wert;
			 this.SQL_Where[0][4] = Verknuepfung;
			 this.SQL_Where[0][5] = "Allgemein";
			 
			 
			 
			 
		 }else
			 
		 {
			 this.SQL_Where = ArrayBuilderString(SQL_Where, 6);
			 this.SQL_Where[this.SQL_Where.length-1][0] = Tabelle;
			 this.SQL_Where[this.SQL_Where.length-1][1] = Spalten; 
			 this.SQL_Where[this.SQL_Where.length-1][2] = Methode;
			 this.SQL_Where[this.SQL_Where.length-1][3] = Wert;
			 this.SQL_Where[this.SQL_Where.length-1][4] = Verknuepfung;
			 this.SQL_Where[this.SQL_Where.length-1][5] ="Allgemein";
			 
			 
			
		 }
	 
		 
	 }	 	

	public void DB_WHERE_In(String Tabelle,String Spalten,String Wert,String Verknuepfung)
	 {
		 if(this.SQL_Where == null)
		 {
			 this.SQL_Where = ArrayBuilderString(SQL_Where, 6);
			 
			 this.SQL_Where[0][0] = Tabelle; 
			 this.SQL_Where[0][1] = Spalten; 
			 this.SQL_Where[0][2] = Wert;
			 this.SQL_Where[0][3] = Verknuepfung;
			 this.SQL_Where[0][4] = "LEER";
			 this.SQL_Where[0][5] = "IN";
			 
			 
		 }else
			 
		 {
			 this.SQL_Where = ArrayBuilderString(SQL_Where, 6);
			 this.SQL_Where[this.SQL_Where.length-1][0] = Tabelle; 
			 this.SQL_Where[this.SQL_Where.length-1][1] = Spalten; 
			 this.SQL_Where[this.SQL_Where.length-1][2] = Wert;
			 this.SQL_Where[this.SQL_Where.length-1][3] = Verknuepfung;
			 this.SQL_Where[this.SQL_Where.length-1][4] = "LEER";
			 this.SQL_Where[this.SQL_Where.length-1][5] = "IN";
		 }
	 
		 
	 }	
	
	public void DB_WHERE_LIKE(String Tabelle,String Spalten,String Wert,String Verknuepfung)
	 {
		 if(this.SQL_Where == null)
		 {
			 this.SQL_Where = ArrayBuilderString(SQL_Where, 6);
			 
			 this.SQL_Where[0][0] = Tabelle;
			 this.SQL_Where[0][1] = Spalten; 
			 this.SQL_Where[0][2] = Wert;
			 this.SQL_Where[0][3] = Verknuepfung;
			 this.SQL_Where[0][4] = "LEER";
			 this.SQL_Where[0][5] = "LIKE";
			 
			 
		 }else
			 
		 {
			 this.SQL_Where = ArrayBuilderString(SQL_Where, 6);
			 this.SQL_Where[this.SQL_Where.length-1][0] = Tabelle; 
			 this.SQL_Where[this.SQL_Where.length-1][1] = Spalten; 
			 this.SQL_Where[this.SQL_Where.length-1][2] = Wert;
			 this.SQL_Where[this.SQL_Where.length-1][3] = Verknuepfung;
			 this.SQL_Where[this.SQL_Where.length-1][4] = "LEER";
			 this.SQL_Where[this.SQL_Where.length-1][5] = "LIKE";
			
		 }
	 
		 
	 }
	
	public void DB_WHERE_BETWEEN(String Tabelle,String Spalten,String Wert1,String Wert2 ,String Verknuepfung)
	 {
		 if(this.SQL_Where == null)
		 {
			 this.SQL_Where = ArrayBuilderString(SQL_Where, 6);
			 this.SQL_Where[0][0] = Tabelle; 
			 this.SQL_Where[0][1] = Spalten; 
			 this.SQL_Where[0][2] = Wert1;
			 this.SQL_Where[0][3] = Wert2;
			 this.SQL_Where[0][4] = Verknuepfung;
			 this.SQL_Where[0][5] = "BETWEEN";
			 
			 
		 }else
			 
		 {
			 this.SQL_Where = ArrayBuilderString(SQL_Where, 6);
			 this.SQL_Where[this.SQL_Where.length-1][0] = Tabelle;
			 this.SQL_Where[this.SQL_Where.length-1][1] = Spalten; 
			 this.SQL_Where[this.SQL_Where.length-1][2] = Wert1;
			 this.SQL_Where[this.SQL_Where.length-1][3] = Wert2;
			 this.SQL_Where[this.SQL_Where.length-1][4] = Verknuepfung;
			 this.SQL_Where[this.SQL_Where.length-1][5] = "BETWEEN";
			
		 }
	 
		 
	 }
	
	public void DB_WHERE_CUSTOM(String Custom ,String Verknuepfung)
	 {
		 if(this.SQL_Where == null)
		 {
			 this.SQL_Where = ArrayBuilderString(SQL_Where, 6);
			 this.SQL_Where[0][0] = Custom; 
			 this.SQL_Where[0][1] = "";
			 this.SQL_Where[0][2] = "";
			 this.SQL_Where[0][3] = Verknuepfung;
			 this.SQL_Where[0][5] = "CUSTOM";
			 
			 
		 }else
			 
		 {
			 this.SQL_Where = ArrayBuilderString(SQL_Where, 6);
			 this.SQL_Where[this.SQL_Where.length-1][0] = Custom; 
			 this.SQL_Where[this.SQL_Where.length-1][1] = "";
			 this.SQL_Where[this.SQL_Where.length-1][2] = "";
			 this.SQL_Where[this.SQL_Where.length-1][3] = Verknuepfung;
			 this.SQL_Where[this.SQL_Where.length-1][5] = "CUSTOM";
			
		 }
	 
		 
	 }
	
	
	protected String func_SQL_Where()
	{

		if(SQL_Where==null)
		{
		
			
			
			
			
			return "";
			
		}else
			
		{
		
		int Count = SQL_Where.length;
		
		
		
		String SQL = null;
		
		SQL = "WHERE " ;
		
		for(int i=0;Count>i;i++)
		{
			
			
				
				switch(this.SQL_Where[i][5])
					
				{
				
				
				
				case "Allgemein":
					
					SQL = SQL + this.SQL_Where[i][0] + "."  + this.SQL_Where[i][1]+" "+this.SQL_Where[i][2]+" "+this.SQL_Where[i][3]+ " "+this.SQL_Where[i][4]+ " ";
					break;
				
				case "BETWEEN":
					SQL = SQL + this.SQL_Where[i][0]+"."+ this.SQL_Where[i][1] + " BETWEEN " +this.SQL_Where[i][2]+" AND "+this.SQL_Where[i][3] + " "+this.SQL_Where[i][4] + " ";
					break;
				case "IN":	
				
					SQL = SQL + this.SQL_Where[i][0]+"."+ this.SQL_Where[i][1]+ " IN (" + this.SQL_Where[i][2]+ ") " +this.SQL_Where[i][3] + " ";
					break;
				case "LIKE":
					
					SQL = SQL + this.SQL_Where[i][0]+"."+ this.SQL_Where[i][1]+ " LIKE " +  this.SQL_Where[i][2] + " " +  this.SQL_Where[i][3] + " ";
					break;
				case "CUSTOM":
				
					SQL = "";
					
					SQL = SQL + this.SQL_Where[i][0] + " " + this.SQL_Where[i][3] + " ";
				
					break;
				}
				
				
				
				
					
				
				
				
					
			
		}
		
		


	return SQL;
		}
	}
	
	
	
	public void Debug_Where_Array()
	{
		System.out.println("**********************DEBUG Where Auswahl ! ***********************************************");
		
		if(SQL_Where != null)
		{
			
			
			
			for (int i = 0; i < SQL_Where.length; i++)
			{
				int g = i + 1 ;
				
				System.out.println("         +++++*************   Datensatz" + g + "   ******************+++++");
				
				for (int a = 0; a < SQL_Where[0].length; a++)
				
				{
					System.out.println("Spalte " + a+"   ------>     " + SQL_Where[i][a]);
									
				}
			      
		     }
			
		}else{
			
			
			System.out.println("                       Keine Filter Ausgewählt!              ");
			
			
		}
		
		
		System.out.println("*****************************DEBUG ENDE *****************************************************");
		
		
	}
	
	public void DB_Data_Leeren()
	{
	this.SQL_Where = null;
		
	}
	
	
	

}
