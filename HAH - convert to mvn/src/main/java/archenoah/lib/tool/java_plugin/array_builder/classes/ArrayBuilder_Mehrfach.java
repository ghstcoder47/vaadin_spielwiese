package archenoah.lib.tool.java_plugin.array_builder.classes;

public class ArrayBuilder_Mehrfach {

	
	private String[][] tempa;
	private int[][] tempb;
	private Object[][] tempc;
	
	public  ArrayBuilder_Mehrfach(String[][] Arr,int Spaltenanzahl)
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
		

		
		tempa = temp;
		
	}
	
	public  ArrayBuilder_Mehrfach(int[][] Arr,int Spaltenanzahl)
	{
		
		int[][] temp ;
		
		if (Arr != null)
		{
			int Count = Arr.length;
			 temp = new int[Count + 1][Spaltenanzahl];
			
			 System.arraycopy(Arr, 0, temp, 0, Arr.length);	
		}else
		{
			 temp = new int[1][Spaltenanzahl];
		
			
		}
		

		
		tempb = temp;
		
	}
	
	public  ArrayBuilder_Mehrfach(Object[][] Arr,int Spaltenanzahl)
	{
		
		Object[][] temp ;
		
		if (Arr != null)
		{
			int Count = Arr.length;
			 temp = new Object[Count + 1][Spaltenanzahl];
			
			 System.arraycopy(Arr, 0, temp, 0, Arr.length);	
		}else
		{
			 temp = new Object[1][Spaltenanzahl];
		
			
		}
		

		
		tempc = temp;
		
	}
	
	
	public String[][] Array_Holen()
	{
		
		return tempa;
	}
	
	public int[][] Array_Holen_int()
	{
		
		return tempb;
	}
	public Object[][] Array_Holen_Object()
	{
		
		return tempc;
	}
	
	
	
	
	
	
	
}
