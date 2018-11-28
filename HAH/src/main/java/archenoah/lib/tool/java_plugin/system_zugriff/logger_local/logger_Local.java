package archenoah.lib.tool.java_plugin.system_zugriff.logger_local;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;

import archenoah.lib.tool.java_plugin.stringmanager.classes.StringManager;





public class logger_Local {

	FileWriter writer;
	File Datei;
	
	private String Pfad;
	private String DIR_Pfad;
	private String Datei_Pfad;
	
	
	public logger_Local(String Loggnaricht)
	{
		
		 Pfad_Bestimmen();
		
		Dateierzeugen();
		
		
		
		Date d = new Date();
	
		DateFormat dfa = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
	

		String Datum_Uhrzeit = dfa.format(d);
		
		
		
	
		
		
		
		schreiben(Datum_Uhrzeit+" : "+Loggnaricht,Datei_Pfad);
		
		
		
	}
	
	
	private void schreiben(String Text,String Pfad){
	    // File anlegen
		Datei = new File(Pfad);
	     try {
	       // new FileWriter(file ,true) - falls die Datei bereits existiert
	       // werden die Bytes an das Ende der Datei geschrieben
	       
	       // new FileWriter(file) - falls die Datei bereits existiert
	       // wird diese überschrieben
	       writer = new FileWriter(Datei ,true);
	       
	       // Text wird in den Stream geschrieben
	       writer.write(Text);
	       
	       // Platformunabhängiger Zeilenumbruch wird in den Stream geschrieben
	       writer.write(System.getProperty("line.separator"));

	       // Text wird in den Stream geschrieben       
	       //writer.write("Danke mir gehts gut!");
	       
	       
	       // Schreibt den Stream in die Datei
	       // Sollte immer am Ende ausgeführt werden, sodass der Stream 
	       // leer ist und alles in der Datei steht.
	       writer.flush();
	       
	       // Schließt den Stream
	       writer.close();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	  }
	
	
	
	
	
	private void Dateierzeugen()
	{
	
	
		
		
		  File file = new File(Datei_Pfad);
		  if(!file.exists()){
	            try {
	                Formatter CHRIS = new Formatter(Datei_Pfad);
	               
	            }catch (FileNotFoundException e) {
	            	
	            	File dir = new File(DIR_Pfad);
	            	dir.mkdir();
	            	try {
						Formatter CHRISA = new Formatter(Datei_Pfad);
					} catch (FileNotFoundException e1) {
						// TODO Automatisch generierter Erfassungsblock
						e1.printStackTrace();
					}
	            }
	        }else {
	           
	        }
		
	}
	
	
	
	private void Pfad_Bestimmen()
	{
		
	
		

		
		
		URL Pfad_URL = getClass ().getProtectionDomain().getCodeSource().getLocation ();
		
		String P_temp =Pfad_URL.toString();
		StringManager sm = new StringManager();
		int sleng = P_temp.length();
		
	
		String Pad =P_temp.substring (6, P_temp.length());
		Pfad = Pad.replace("%20", " ");
		DIR_Pfad = Pfad+"Log//";
		Pfad = DIR_Pfad;
		
		Date d = new Date();
		DateFormat df = new SimpleDateFormat( "yyyy-MM-dd" );
		

		
		Datei_Pfad = Pfad +df.format(d)+".log";
		
	}
	
	
	
}
