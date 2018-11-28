package archenoah.lib.vaadin.reg_generieren;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;





public class Generieren_Registry {

	
	
	private String Pfad;
	
	public Generieren_Registry(String Arg_Pfad) {
		// TODO Automatisch generierter Konstruktorstub
		
		Pfad = Arg_Pfad;
	}
	

	
	
	public void schreiben(String Text){
	    // File anlegen
	    FileWriter writer;
		File Datei = new File(Pfad);
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
	
	
	
	
	
	
	
	
	public void Dateierzeugen()
	{
	

		
		
	
		
		  File file = new File(Pfad);
		  file.delete();
		
		  if(!file.exists()){
	            try {
	                Formatter CHRIS = new Formatter(Pfad);
	               
	            }catch (FileNotFoundException e) {
	            	
	            	//File dir = new File(Dir_Pfad);
	            	//dir.mkdir();
	            	try {
						Formatter CHRISA = new Formatter(Pfad);
						CHRISA.close();
					} catch (FileNotFoundException e1) {
						// TODO Automatisch generierter Erfassungsblock
						e1.printStackTrace();
					}
	            }
	        }else {
	           
	        }
		
	}
	
	
	
	
	
	

}
