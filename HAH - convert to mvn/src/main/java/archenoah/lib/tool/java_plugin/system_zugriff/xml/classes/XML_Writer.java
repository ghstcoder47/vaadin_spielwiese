package archenoah.lib.tool.java_plugin.system_zugriff.xml.classes;


	

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import archenoah.lib.tool.java_plugin.array_builder.classes.ArrayBuilder_Mehrfach;



public class XML_Writer {

	String XML_Pfad ="";
	String Wurzel;
	protected  String[][]XML_data=null;
	
	
	public XML_Writer(String Arg_XML_Pfad,String Wurzelelement) {
		
		XML_Pfad = Arg_XML_Pfad;
		Wurzel = Wurzelelement;
		
		// TODO Automatisch generierter Konstruktorstub
	}
	
	
	
	
	
	
	public void Set_XML(String Elementename, String Wert)
	{
		ArrayBuilder_Mehrfach arr = new ArrayBuilder_Mehrfach(XML_data, 2);
		 this.XML_data = arr.Array_Holen();
		 
	
		if(XML_data.length >0)
		{
			this.XML_data[XML_data.length-1][0] = Elementename; 
			 this.XML_data[XML_data.length-1][1] = Wert;	
		}else
		{
			this.XML_data[0][0] = Elementename; 
			this.XML_data[0][1] = Wert;
		}
	
		 	
	}
	
	
	
	
	
	public void XML_Write()
	{
		 try {
			 
				SAXBuilder builder = new SAXBuilder();
				File xmlFile = new File(XML_Pfad);
		 
				Document doc = (Document) builder.build(xmlFile);
				Element rootNode = doc.getRootElement();
		 
				// update staff id attribute
				Element staff = rootNode.getChild(Wurzel);
		//		staff.getAttribute("id").setValue("2");
		 
				// add new age element
			//	Element age = new Element("age").setText("28");
			//	staff.addContent(age);
		 
				// update salary value
				
			
				
				
				for(int i = 0;i<XML_data.length;i++)
				{
					staff.getChild(XML_data[i][0]).setText(XML_data[i][1]);
				
				}
				
		 
				// remove firstname element
			//	staff.removeChild("firstname");
		 
				XMLOutputter xmlOutput = new XMLOutputter();
		 
				// display nice nice
				xmlOutput.setFormat(Format.getPrettyFormat());
				xmlOutput.output(doc, new FileWriter(XML_Pfad));
		 
				// xmlOutput.output(doc, System.out);
		 
				
			  } catch (IOException io) {
				io.printStackTrace();
			  } catch (JDOMException e) {
				e.printStackTrace();
			  }
		
	
	
	}
	

}
