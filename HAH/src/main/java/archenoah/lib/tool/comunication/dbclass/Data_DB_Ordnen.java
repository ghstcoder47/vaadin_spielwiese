package archenoah.lib.tool.comunication.dbclass;


public class Data_DB_Ordnen extends Data_DB_Sammlung{
	  private  String[][]SQL_Ordnen;
	  
	  
	public Data_DB_Ordnen() {
		// TODO Automatisch generierter Konstruktorstub
	}
	
	public void DB_Ordnen(String Spalten,String Methoden) {
		
		if(this.SQL_Ordnen == null)
		 {
			this.SQL_Ordnen = ArrayBuilderString(this.SQL_Ordnen, 2);
			 this.SQL_Ordnen[0][0] = Spalten;
			 this.SQL_Ordnen[0][1] = Methoden;
			 
			 
		 }else
			 
		 {
			 this.SQL_Ordnen = ArrayBuilderString(this.SQL_Ordnen, 2);
			 this.SQL_Ordnen[this.SQL_Ordnen.length-1][0] = Spalten; 
			 this.SQL_Ordnen[this.SQL_Ordnen.length-1][1] = Methoden;
			
		 }
	}
	
	protected String func_Ordnen()
	{
		if(SQL_Ordnen==null)
			
		{
			
			return "";
		}
		String SQL = "ORDER BY ";
		
		for(int i = 0;SQL_Ordnen.length>i;i++)
		{
			if(i==0)
			{
				SQL = SQL + SQL_Ordnen[i][0] + " " + SQL_Ordnen[i][1];
				
			}else
			{
				SQL = SQL +" , " + SQL_Ordnen[i][0] + " " + SQL_Ordnen[i][1];
			}
			
			
		}
		
		
		
		return SQL;
		
	}
	
	public void DB_Data_Leeren()
	{
		SQL_Ordnen = null;
	}
	public void Debug_Ordnen_Array()
	{
		System.out.println("**********************DEBUG Ordnen Auswahl ! ***********************************************");
		
		if(SQL_Ordnen != null)
		{
			
			
			
			for (int i = 0; i < SQL_Ordnen.length; i++)
			{
				int g = i + 1 ;
				
				System.out.println("         +++++*************   Datensatz" + g + "   ******************+++++");
				
				for (int a = 0; a < SQL_Ordnen[0].length; a++)
				
				{
					System.out.println("Spalte " + a+"   ------>     " + SQL_Ordnen[i][a]);
									
				}
			      
		     }
			
		}else{
			
			
			System.out.println("                       Kein Ordnen Ausgewählt!              ");
			
			
		}
		
		
		System.out.println("*****************************DEBUG ENDE *****************************************************");
		
		
	}
	
	

}
