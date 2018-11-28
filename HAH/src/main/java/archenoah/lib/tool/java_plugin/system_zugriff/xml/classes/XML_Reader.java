package archenoah.lib.tool.java_plugin.system_zugriff.xml.classes;

import java.io.File;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;


import archenoah.lib.tool.java_plugin.array_builder.classes.ArrayBuilder_Mehrfach;
import archenoah.lib.tool.java_plugin.system_zugriff.pfad.classes.Pfad;





public class XML_Reader {

	private Document doc = null;
	private String[] Data = null;
	private String temp;
	private List alleKinder;
	
	public XML_Reader(String PfadXML,String Wurzelelement)
	{
		ArrayBuilder_Mehrfach arr;
		
		Pfad PF = new Pfad(); // Pfad holen
		 	
		 File f = new File(PF.Pfadholen() + PfadXML);
		 
		 try
			{
				 // Das Dokument erstellen
	            SAXBuilder builder = new SAXBuilder();
	          
	            doc = builder.build(f);
	           
	            Element element = doc.getRootElement(); //Hole Das Wurzelelement
	         
	           
	           element = element.getChild(Wurzelelement);//Übergebe Das Kindelement

	    
	            
	            
	            

	            // Eine Liste aller direkten Kindelemente eines Elementes erstellen
	             alleKinder = (List) element.getChildren();
	            
	            
	           
	            
	            

			}	
			catch( Exception e )
			{
				//Loger log = new Loger("Fehler","Es wurde keine XML Datei Gefunden oder ein Falscher Conainer ausgewählt!","0","System","127.0.0.1","Fehlercode 0X0002");
				//TODO Logger wieder Einschalten 
				// LOGER !!!
			System.out.println( "XML-Datei nicht gef. od. sonst. Fehler");
			
			} 
		 
		 
	}
	
	
	public List XML_Holen()
	{
		
		return alleKinder;
	}
	
	
}
