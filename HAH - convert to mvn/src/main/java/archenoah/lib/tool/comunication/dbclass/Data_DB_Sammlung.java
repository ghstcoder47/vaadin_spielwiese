package archenoah.lib.tool.comunication.dbclass;

public class Data_DB_Sammlung {

	
	

	
	
	public Data_DB_Sammlung() {
		// TODO Automatisch generierter Konstruktorstub
	}

	protected String[][] ArrayBuilderString(String[][] Arr,int Spaltenanzahl)
	{
		
		String[][] temp ;
		
		if (Arr != null)
		{
			int Count = Arr.length;
			 temp = new String[Count + 1][Spaltenanzahl];
			
			 System.arraycopy(Arr, 0, temp, 0, Arr.length);	
		}else
		{
			 temp = new String[1][Spaltenanzahl];
		
			
		}
		
		return temp;
	}
	
	protected String[] ArrayBuilderString_Einzeln(String[] Arr)
	{
		
		String[] temp ;
		
		if (Arr != null)
		{
			int Count = Arr.length;
			 temp = new String[Count + 1];
			
			 System.arraycopy(Arr, 0, temp, 0, Arr.length);	
		}else
		{
			 temp = new String[1];
		
			
		}
		
		return temp;
		
	}
	
	
	
	
	
	
	
	
}
