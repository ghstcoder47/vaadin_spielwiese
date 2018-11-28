package archenoah.lib.tool.comunication.dbclass;


public class Data_DB_Gruppieren extends Data_DB_Sammlung {
	private  String[]SQL_Grupieren;
	
	public Data_DB_Gruppieren() {
		// TODO Automatisch generierter Konstruktorstub
	}
	
	public void DB_Gruppieren(String Spalten) {
		if(this.SQL_Grupieren == null)
		 {
			this.SQL_Grupieren = ArrayBuilderString_Einzeln(this.SQL_Grupieren);
			this.SQL_Grupieren[0] = Spalten; 
			 
		 }else
			 
		 {
			 this.SQL_Grupieren = ArrayBuilderString_Einzeln(this.SQL_Grupieren);
			 this.SQL_Grupieren[this.SQL_Grupieren.length-1] = Spalten; 
			
		 }
	}
	public void DB_Data_Leeren()
	{
		SQL_Grupieren = null;
	}
	
	protected String func_Gruppieren()
	{
	if(SQL_Grupieren==null)
		
	{
		
		return "";
	}

		String SQL = "GROUP BY ";
		
		for(int i = 0;SQL_Grupieren.length>i;i++)
		{
			if(i==0)
			{
				SQL = SQL + SQL_Grupieren[i];
				
			}else
			{
				SQL = SQL +" , " + SQL_Grupieren[i];
			}
			
			
		}
		
		
		
		return SQL;
		
	}	
	
	
	public void Debug_Gruppieren_Array()
	{
		System.out.println("**********************DEBUG Ordnen Auswahl ! ***********************************************");
		
		if(SQL_Grupieren != null)
		{
			
			
			
			for (int i = 0; i < SQL_Grupieren.length; i++)
			{
				int g = i + 1 ;
				
				System.out.println("         +++++*************   Datensatz" + g + "   ******************+++++");
				
				
					System.out.println("Spalte 1 " +"   ------>     " + SQL_Grupieren[i]);
									
				
			      
		     }
			
		}else{
			
			
			System.out.println("                       Kein Ordnen Ausgewählt!              ");
			
			
		}
		
		
		System.out.println("*****************************DEBUG ENDE *****************************************************");
		
		
	}
	
	
	
	
}
