package archenoah.lib.tool.java_plugin.system_zugriff.pfad.classes;

import java.net.URL;

import archenoah.lib.tool.java_plugin.stringmanager.classes.StringManager;






public  class Pfad_lib {
	
	
	
		private String pfad;
		
		 public Pfad_lib()
		{
			 URL Pfad_URL = getClass ().getProtectionDomain().getCodeSource().getLocation ();
				
				String P_temp =Pfad_URL.toString();
				StringManager sm = new StringManager();
				int sleng = P_temp.length();
				
			
				String Pad =P_temp.substring (6, P_temp.length()-81);
				
			
				pfad = Pad.replace("%20", " ")+ "lib/";
				
			
		}
		
		
		public String Pfadholen()
		{
		
			return pfad;
		}

		 
		 
		 
	} 
	
	
	


