package archenoah.lib.vaadin.Customclass;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import archenoah.lib.tool.java_plugin.stringmanager.classes.StringManager;



public class CustomClassLoader {

	public CustomClassLoader() {
		// TODO Automatisch generierter Konstruktorstub

		
		
		
		
		 URL Pfad_URL = getClass ().getProtectionDomain().getCodeSource().getLocation ();
			
			String P_temp =Pfad_URL.toString();
			StringManager sm = new StringManager();
			int sleng = P_temp.length();
			
		
			String Pad =P_temp.substring (6, P_temp.length()-56);
			
		
			
			
			
        String url =Pad.replace("%20", " ");
        
       
        

	    try {
			newInstance( url, "aria.config.test" );
		} catch (Exception e) {
			// TODO Automatisch generierter Erfassungsblock
			e.printStackTrace();
		}
		
	}
	
	
	
	
	static Object newInstance( String path, String classname ) throws Exception
	  {
	    URL url = new File( path ).toURI().toURL();

	    URLClassLoader cl = new URLClassLoader( new URL[]{ url } );
	   
	  
	    Class<?> c = cl.loadClass( classname );
	    
	    Constructor ctor = c.getDeclaredConstructor(String.class);
            
	    return  ctor.newInstance("Test"); 
	  }


}
