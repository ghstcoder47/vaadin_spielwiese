package archenoah.lib.tool.java_plugin.array_builder.example;

import archenoah.lib.tool.java_plugin.array_builder.classes.ArrayBuilder_Einzel;




public class Beispiel_Array_Builder_Einzeln {

	public Beispiel_Array_Builder_Einzeln()
	{
		String[] Data = null;
		
		ArrayBuilder_Einzel 	arr = new ArrayBuilder_Einzel(Data);
    	Data = arr.Array_Holen();
    	
    	Data[0]= "asan";
    	
    	System.out.print(Data);
	}
	
}
