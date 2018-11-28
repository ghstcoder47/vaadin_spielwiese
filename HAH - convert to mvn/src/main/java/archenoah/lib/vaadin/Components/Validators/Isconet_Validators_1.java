package archenoah.lib.vaadin.Components.Validators;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import org.abstractmeta.toolbox.compilation.compiler.JavaSourceCompiler;
import org.abstractmeta.toolbox.compilation.compiler.impl.JavaSourceCompilerImpl;



import archenoah.config.CMS_Config_Std;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.java_plugin.system_zugriff.pfad.classes.Pfad_lib;
import archenoah.web.setup.validator.Val_Willkommen2;

import com.vaadin.data.Container;
import com.vaadin.data.util.*;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

public class Isconet_Validators_1 { 
	private Set<Object> keys;
	private Object Classes;
	private  String Classname; 
	private String Validatorname;
	private String Erforderlich;
	private String[] Data;
	
	
	
	private String LNG_ID;
	private String Form_ID;
	private String Code;
	private String NewClassName;
	
	
	
	private Class Validator;
	private String Requiretext;
	
	
	
	public Isconet_Validators_1(String Arg_Formid,Map<Object, String> Arg_Validator_Map){
		// TODO Automatisch generierter Konstruktorstub
		
		Form_ID = Arg_Formid;
		
		if(Get_DB_Data()== false)
		{
			return;
		}
		
		if(Klasse_Erstellen()== false)
		{
			return;
		}
		
		
		
	
		
		
		
		
		
		
		
		   
		keys = Arg_Validator_Map.keySet();
		
		 
		
		 for (Object singleKey : keys) {
			 
			 Classes = 	singleKey;
			 
			 
			 
			 Data =  Arg_Validator_Map.get(singleKey).toString().split(":");
			 Validatorname =  Data[0];
			 Erforderlich = Data[1];

		
			
				//System.out.print(Classes.getClass().getName() +" \n");
					
				Classname = Classes.getClass().getName();
			 
			
			
				
				
								
			// ******************* Compenenten Auswahl *******************/
					
				//System.out.println(Classname);
						switch(Classname)
						{
						
						/*************** Combobox *****************/
						case "com.vaadin.ui.ComboBox":
						ComboBox cb = (ComboBox) singleKey;	
							
						
						break;
						/*****************************************/
						/*************** NativeSelect *****************/
						case "com.vaadin.ui.NativeSelect":
						NativeSelect ns = (NativeSelect) singleKey;	
						
						break;
						/*****************************************/
						/*************** Textfield *****************/
						case "com.vaadin.ui.TextField":
							
							
							TextField txtf = (TextField) singleKey;	
							if(Erforderlich.equals("Ja")== true)
							{
								txtf.setRequired(true);
								txtf.setRequiredError(Requiretext);
							}else
							{
								txtf.setRequired(false);
							}
								txtf.addValidator(new BeanValidator(Validator, Validatorname));
								txtf.setImmediate(true);
						
						
						
						
						break;
						/*****************************************/
						/*************** TextArea *****************/
						case "com.vaadin.ui.TextArea":
						TextArea txta = (TextArea) singleKey;	
						
						break;
						/*****************************************/
						/*************** PasswordField *****************/
						case "com.vaadin.ui.PasswordField":
						PasswordField pswf = (PasswordField) singleKey;	
						
						if(Erforderlich.equals("Ja")== true)
						{
							pswf.setRequired(true);
							pswf.setRequiredError(Requiretext);
						}else
						{
							pswf.setRequired(false);
						}
							pswf.addValidator(new BeanValidator(Validator, Validatorname));
							pswf.setImmediate(true);
						
						break;
						/*****************************************/	
						/*************** RichTextArea *****************/
						case "com.vaadin.ui.RichTextArea":
						RichTextArea rta = (RichTextArea) singleKey;	
						
						break;
						/*****************************************/	
						/*************** PopupDateField *****************/
						case "com.vaadin.ui.PopupDateField":
						PopupDateField pdf = (PopupDateField) singleKey;	
					
						break;
						/*****************************************/		
						
					
						}
			 
			 
			 
			 
			 
			 
			 
			 
		 }
		
		
		
		
		
		
		
		
		
	
		 }
		
	
	
	
	
	private boolean Klasse_Erstellen()
	{
		
		Pfad_lib pl = new Pfad_lib();
		
		
		
		JavaSourceCompiler javaSourceCompiler = new JavaSourceCompilerImpl();
	    JavaSourceCompiler.CompilationUnit compilationUnit = javaSourceCompiler.createCompilationUnit();
	  
	    compilationUnit.addClassPathEntry(pl.Pfadholen()+"validation-api-1.0.0.GA.jar");
	    compilationUnit.addClassPathEntry(pl.Pfadholen()+"jboss-logging-3.1.2.GA.jar");
	    compilationUnit.addClassPathEntry(pl.Pfadholen()+"slf4j-api-1.7.5.jar");
	    compilationUnit.addClassPathEntry(pl.Pfadholen()+"slf4j-simple-1.7.5.jar");
	    compilationUnit.addClassPathEntry(pl.Pfadholen()+"hibernate-validator-4.3.1.Final.jar");
	  
	  
	  
	  
	  

	    ClassLoader classLoader;
		try {
			compilationUnit.addJavaSource("aria.lib.vaadin.Components.Validators.Validatoren."+NewClassName, Code);
  
			classLoader = javaSourceCompiler.compile(compilationUnit);
		} catch (Exception e) {
			// TODO Automatisch generierter Erfassungsblock
			e.printStackTrace();
			return false;
		}
	   
	    
	   
	    try {
			 Validator = classLoader.loadClass("aria.lib.vaadin.Components.Validators.Validatoren."+NewClassName);
		} catch (ClassNotFoundException e1) {
			// TODO Automatisch generierter Erfassungsblock
			e1.printStackTrace();
			return false;
		}
		
		
	    return true;

		
	}
	
	
	
	private boolean Get_DB_Data()
	{
		String Formname = null;
		String LCODE = null;
		String Valname = null;
		
		LNG_ID = UI.getCurrent().getSession().getAttribute("Lng_Id").toString();
		
		CMS_Config_Std std = CMS_Config_Std.getInstance();
		DBClass db = new DBClass();
				db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("LVL_CODE");
				db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("LVL_REQUIRE_TEXT");
				db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("LVL_NAME");
				db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("CLPL_CODE");
				db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("CLFF_NAME");
				
				db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Verknüpfung("cms_language_forms_frm", "INNER JOIN", "cms_language_validator_list", "CLFF_ID", "LVL_F_ID");
				db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Verknüpfung("cms_language_pack_liste", "INNER JOIN", "cms_language_validator_list", "CLPL_ID", "LVL_LNG_ID");
				
				db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cms_language_validator_list", "LVL_LNG_ID", "=", LNG_ID, "AND");
				db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cms_language_validator_list", "LVL_F_ID", "=", Form_ID, "");
				
		
				Container con = db.DB_Data_Get.DB_SEND_AND_GET_Container();
				
				
				try {
					for (Object ItemIda : con.getItemIds()) {
      //UserId = (int) result.getItem(ItemIda).getItemProperty(this.Spalte_ID).getValue;
						
						Code = (String)con.getItem(ItemIda).getItemProperty("LVL_CODE").getValue();
						Requiretext = (String)con.getItem(ItemIda).getItemProperty("LVL_REQUIRE_TEXT").getValue();
						Valname = (String)con.getItem(ItemIda).getItemProperty("LVL_NAME").getValue();
						LCODE = (String)con.getItem(ItemIda).getItemProperty("CLPL_CODE").getValue();
						Formname = (String)con.getItem(ItemIda).getItemProperty("CLFF_NAME").getValue();
					}

					NewClassName = LCODE +"_"+Formname+"_"+Valname;
					System.out.println(NewClassName);
					System.out.println(Code);
					
					
					return true;
				} catch (Exception e) {
					// TODO Automatisch generierter Erfassungsblock
					e.printStackTrace();
					return false;
				}
		
		
	}
	
}

	
	
		
		
		
		

