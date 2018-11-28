package archenoah.lib.tool.comunication.dbclass;


public class Data_DB_Limit extends Data_DB_Sammlung {
	private  String[]SQL_LIMIT;
	
	
	public Data_DB_Limit() {
		// TODO Automatisch generierter Konstruktorstub
	}
	
	/*
	 * 
	 * 							Public 
	 * 
	 * 
	 * */
	
	public void DB_LIMIT(String Limit) {
		if(this.SQL_LIMIT == null)
		 {
			this.SQL_LIMIT = ArrayBuilderString_Einzeln(this.SQL_LIMIT);
			 this.SQL_LIMIT[0] = Limit;
			
			 
			 
		 }else
			 
		 {
			 this.SQL_LIMIT = ArrayBuilderString_Einzeln(this.SQL_LIMIT);
			 this.SQL_LIMIT[this.SQL_LIMIT.length-1] = Limit; 
			
		 }
	}
	
	
	protected String func_LIMIT()
	{
		if(SQL_LIMIT==null)
			
		{
			
			return "";
		}
		String SQL = "LIMIT ";
		
		for(int i = 0;SQL_LIMIT.length>i;i++)
		{
			if(i==0)
			{
				SQL = SQL + SQL_LIMIT[i];
				
			}else
			{
				SQL = SQL +" , " + SQL_LIMIT[i];
			}
			
			
		}
		
		
		
		return SQL;
		
	}
	
	public void DB_Data_Leeren()
	{
		SQL_LIMIT = null;
	}
	
	public void Debug_LIMIT_Array()
	{
		System.out.println("**********************DEBUG Ordnen Auswahl ! ***********************************************");
		
		if(SQL_LIMIT != null)
		{
			
			
			
			for (int i = 0; i < SQL_LIMIT.length; i++)
			{
				int g = i + 1 ;
				
				System.out.println("         +++++*************   Datensatz" + g + "   ******************+++++");
				
				
				
				
					System.out.println("Spalte1 "  +"   ------>     " + SQL_LIMIT[i]);
									
				
			      
		     }
			
		}else{
			
			
			System.out.println("                       Kein Ordnen Ausgewählt!              ");
			
			
		}
		
		
		System.out.println("*****************************DEBUG ENDE *****************************************************");
		
		
	}
	
	
}
