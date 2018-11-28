package archenoah.lib.vaadin.Components.Twinlist;

import archenoah.lib.tool.java_plugin.stringmanager.classes.StringManager;



public class Twinlist_Getarray {

	private String[] arr;
	
	
	public Twinlist_Getarray(Object Result) {
		
		
		StringManager sm = new StringManager();
		String Wert = Result.toString();
		
		Wert = sm.Mid(Wert, 1, Wert.length()-1);
		Wert = Wert.replace(" ", "");
		
		
		arr = Wert.split(",");
		
		
		
		
		
		
		// TODO Automatisch generierter Konstruktorstub
	}
	
	
	public String[] Arrayholen()
	{
		return arr;
		
	}
	

}
