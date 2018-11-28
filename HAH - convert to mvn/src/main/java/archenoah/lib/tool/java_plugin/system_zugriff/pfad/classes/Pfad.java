package archenoah.lib.tool.java_plugin.system_zugriff.pfad.classes;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;






public  class Pfad {
	
	
	
		private String pfad;
		
		 public Pfad()
		{
		     
			 URL Pfad_URL = getClass ().getProtectionDomain().getCodeSource().getLocation ();
				
			 
			 
			 Pattern pattern = Pattern.compile("^file:\\/(.+\\/WEB-INF\\/classes\\/)");
			 Matcher matcher = pattern.matcher(Pfad_URL.toString());
			 if(matcher.find()){
			     if(matcher.group(1) != null){
			         pfad = matcher.group(1); 
			     }
			 }
			 
//			 
//			 
//				String P_temp =Pfad_URL.toString();
//				StringManager sm = new StringManager();
//				int sleng = P_temp.length();
//				
//			
//				String Pad =P_temp.substring (6, P_temp.length());
//			
//				pfad = Pad.replace("%20", " ");
				
			
		}
		
		
		public String Pfadholen()
		{
		
			return pfad;
		}

		 
		 
		 
	} 
	
	
	


