package archenoah.lib.tool.java_plugin.array_builder.classes;

public class ArrayBuilder_Einzel {
	
	private String[] arr;
	private int[] arrint;

	public ArrayBuilder_Einzel(String[] Arr)
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
		
		
		
		
		    
		
		
		
		arr =  temp;
		
		
	}
	public ArrayBuilder_Einzel(int[] Arr)
	{
		
		int[] temp ;
		
		if (Arr != null)
		{
			int Count = Arr.length;
			 temp = new int[Count + 1];
			
			 System.arraycopy(Arr, 0, temp, 0, Arr.length);	
		}else
		{
			 temp = new int[1];
		
			
		}
		
		
		
		
		    
		
		
		
		arrint =  temp;
		
		
	}
	
	
	public String[] Array_Holen()
	{
		
		return arr;
	}
	
	public int[] Array_Holen_int()
	{
		
		return arrint;
	}
	
	
	
	
	
	
}
