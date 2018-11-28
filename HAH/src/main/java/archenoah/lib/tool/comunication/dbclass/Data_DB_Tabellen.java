package archenoah.lib.tool.comunication.dbclass;


/**
 *                      Isconet PHP Datenbank Klasse Tabellen
 *                                  v.0.1
 *                          @author Asan Kaceri
 */
public class Data_DB_Tabellen extends Data_DB_Sammlung {

	
	
	
	
	// **************************************   Private ***************************************
	
	protected String CustomSQL= "";
	protected  String[][]SQL_Abfrage=null; 

	protected  String[][]SQL_Abfrage_Graph=null; 
	protected String HTBL[]= null;
	protected String temp_Graph[][] = null;
	
	
	private String[] Menge= null;
	
	public Data_DB_Tabellen() {
		// TODO Automatisch generierter Konstruktorstub
	}
	
	
	
	
	
	
	
	//	*******************************   Public Function     ******************************************
	
	
	public void DB_CustomSQL(String SQL)
	{
		this.CustomSQL = SQL;
	}
	
	
	public void DB_Data_Leeren()
	{
		this.SQL_Abfrage = null;
		this.SQL_Abfrage_Graph = null;
				
	}
	
	public void DB_set_Tabelle_Einzeln(String Tabellen)
	 {
		 if(this.SQL_Abfrage == null)
		 {
			 this.SQL_Abfrage = ArrayBuilderString(SQL_Abfrage, 5);
			 this.SQL_Abfrage[0][0] = Tabellen; 
			 this.SQL_Abfrage[0][1] = "Einzeln";
			 
		 }else
			 
		 {
			 
			 this.SQL_Abfrage[0][0] = Tabellen; 
			 this.SQL_Abfrage[0][1] = "Einzeln";
			 
		 }
		 
		 
		 
	 }
	
	public void DB_set_Tabelle_Verknüpfung(String Tabelle1,String Methode,String Tabelle2,String Wert1,String Wert2)
	 {
		 if(this.SQL_Abfrage == null)
		 {
			 this.SQL_Abfrage = ArrayBuilderString(SQL_Abfrage, 5);
			 this.SQL_Abfrage[0][0] = Tabelle1; 
			 this.SQL_Abfrage[0][1] = Tabelle2;
			 this.SQL_Abfrage[0][2] = Methode;
			 this.SQL_Abfrage[0][3] = Wert1;
			 this.SQL_Abfrage[0][4] = Wert2;
			 
			 
		 }else
			 
		 {
			 this.SQL_Abfrage = ArrayBuilderString(SQL_Abfrage, 5);
			 this.SQL_Abfrage[this.SQL_Abfrage.length-1][0] = Tabelle1; 
			 this.SQL_Abfrage[this.SQL_Abfrage.length-1][1] = Tabelle2;
			 this.SQL_Abfrage[this.SQL_Abfrage.length-1][2] = Methode;
			 this.SQL_Abfrage[this.SQL_Abfrage.length-1][3] = Wert1;
			 this.SQL_Abfrage[this.SQL_Abfrage.length-1][4] = Wert2;
		 }
		 
		 
		 
	 }
	public void DB_set_Tabelle_Verknüpfung_Graph(String Methode ,String Haupttabelle,String Tabelle1,String Tabelle2,String Wert1,String Wert2)
	 {
		 if(this.SQL_Abfrage_Graph == null)
		 {
			 this.SQL_Abfrage_Graph = ArrayBuilderString(SQL_Abfrage_Graph, 6);
			 this.SQL_Abfrage_Graph[0][0] = Methode; 
			 this.SQL_Abfrage_Graph[0][1] = Haupttabelle;
			 this.SQL_Abfrage_Graph[0][2] = Tabelle1;
			 this.SQL_Abfrage_Graph[0][3] = Tabelle2;
			 this.SQL_Abfrage_Graph[0][4] = Wert1;
			 this.SQL_Abfrage_Graph[0][5] = Wert2;
			 
		 }else
			 
		 {
			 this.SQL_Abfrage_Graph = ArrayBuilderString(SQL_Abfrage_Graph, 6);
			 this.SQL_Abfrage_Graph[this.SQL_Abfrage_Graph.length-1][0] = Methode; 
			 this.SQL_Abfrage_Graph[this.SQL_Abfrage_Graph.length-1][1] = Haupttabelle;
			 this.SQL_Abfrage_Graph[this.SQL_Abfrage_Graph.length-1][2] = Tabelle1;
			 this.SQL_Abfrage_Graph[this.SQL_Abfrage_Graph.length-1][3] = Tabelle2;
			 this.SQL_Abfrage_Graph[this.SQL_Abfrage_Graph.length-1][4] = Wert1;
			 this.SQL_Abfrage_Graph[this.SQL_Abfrage_Graph.length-1][5] = Wert2;
		 }
	 }
	
	// ********************************  Protected Function    *****************************************
	
	protected String func_SQL_Abfrage()
	{
		
		if(CustomSQL != ""){
			return CustomSQL;
		}
		
		String[][]SQL_Arr = this.SQL_Abfrage;
		int Count = SQL_Arr.length;
		int Count_var = 0;
		String SQL = "";
		
		if(SQL_Abfrage==null)
		{
		
			
			
			
			System.out.println("*******************************FEHLER!!!*********************");
			System.out.println("*          FEHLER 3    Keine Abfrage Vorhanden!              *");
			System.out.println("*************************************************************");
			return "";
			
		}
		
		if(Count==1 && SQL_Abfrage[0][1]=="Einzeln")
		{
			
			return SQL = SQL_Abfrage[0][0];
			
		}
		
		if(Count==1)
		{
			
			return SQL = SQL_Abfrage[0][0] + " " + SQL_Abfrage[0][2] + " " + SQL_Abfrage[0][1] + " ON " + SQL_Abfrage[0][0] + "."+ SQL_Abfrage[0][3] + " = " + SQL_Abfrage[0][1] +"."+SQL_Abfrage[0][4] + "";
			
		}
		
		
		
		
		for(int i = 0 ;Count>i;i++)
		{
			
			
		
			
		
			Count_var = SQL_Arr.length;
			if(i==0)
			{
				SQL = "( "+ SQL_Abfrage[i][0] + " " + SQL_Abfrage[i][2] + " " + SQL_Abfrage[i][1] + " ON " + SQL_Abfrage[i][0] + "." + SQL_Abfrage[i][3] + " = " + SQL_Abfrage[i][1] +"."+ SQL_Abfrage[i][4] +" )";
				Menge = ArrayBuilderString_Einzeln(Menge);
				Menge[0]= SQL_Arr[i][0];
				Menge = ArrayBuilderString_Einzeln(Menge);
				Menge[1]= SQL_Arr[i][1];
				SQL_Arr = func_Array_Leerung(SQL_Arr,i);
				
			
				
			}else{
				
				for(int k=0;Count_var>k;k++)
				
				{
					
					
					if(func_P(SQL_Arr[k][0])==true && func_P(SQL_Arr[k][1])==true )
					{
						
						
						// FEHLER
						break;
					}
					
				
					
					
					
					if(func_P(SQL_Arr[k][0])==true)
					{
						
						Menge = ArrayBuilderString_Einzeln(Menge);
						Menge[Menge.length-1]= SQL_Arr[k][1];
					
						if(SQL_Arr.length!=1)
						{
						SQL = "( "+ SQL_Arr[k][1] + " " + SQL_Arr[k][2] + " " + SQL + " ON " + SQL_Arr[k][0] + "." + SQL_Arr[k][3] + " = " + SQL_Arr[k][1] +"."+ SQL_Arr[k][4] +" )";
						}else
						{
							SQL = ""+ SQL_Arr[k][1] + " " + SQL_Arr[k][2] + " " + SQL + " ON " + SQL_Arr[k][0] + "." + SQL_Arr[k][3] + " = " + SQL_Arr[k][1] +"."+ SQL_Arr[k][4] +"";
						}
						
						SQL_Arr = func_Array_Leerung(SQL_Arr,k);
						break;
					}
					if(func_P(SQL_Arr[k][1])==true)
					{
						
						Menge = ArrayBuilderString_Einzeln(Menge);
						Menge[Menge.length-1]= SQL_Arr[k][0];
						
						if(SQL_Arr.length!=1)
						{
						SQL = "( "+ SQL_Arr[k][0] + " " + SQL_Arr[k][2] + " " + SQL + " ON " + SQL_Arr[k][0] + "." + SQL_Arr[k][3] + " = " + SQL_Arr[k][1] +"."+ SQL_Arr[k][4] +" )";
						}else
						{
							SQL = ""+ SQL_Arr[k][0] + " " + SQL_Arr[k][2] + " " + SQL + " ON " + SQL_Arr[k][0] + "." + SQL_Arr[k][3] + " = " + SQL_Arr[k][1] +"."+ SQL_Arr[k][4] +"";
						}
						
						SQL_Arr = func_Array_Leerung(SQL_Arr,k);
						break;
						
					}
						
					
				}
				
			}
			
		
		
		}
		
		
		//******SQL Graph******//
		
		
		 if(this.SQL_Abfrage_Graph != null)
		 {
			 
			 
			 HTBL_FILL();
			 
			 for(int t = 0 ;t<HTBL.length;t++)
				 
			 {
				 SQL = "(" + SQL + ") ";
				 
				 set_tempgraph(HTBL[t]);
				 
				 SQL = SQL + temp_Graph[0][0] + " " + temp_Graph[0][1] + " ON ";
				 
				 for(int n = 0;n<temp_Graph.length;n++)
				 {
					 SQL = SQL + "(" + temp_Graph[n][2]+"."+temp_Graph[n][4]+ " = "+ temp_Graph[n][3]+"."+temp_Graph[n][5] + ")";
					 
					 if(n !=temp_Graph.length-1)
					 {
						 SQL = SQL + " AND ";
					 }
					 
				 }
				 
			 }
		 }
		 
		 
		if(SQL_Arr == null)
		{
			//System.out.println(SQL);
			return SQL;
			
			
		}else
		{
			return "FEHLER";

		}
		
	}
	
	
	//	*******************************   Private Function     ******************************************
	
	private String[][] func_Array_Leerung(String[][] arr,int id)
	{
		
		int Count = arr.length ;
	   String[][]temp = null;
	    for(int i=0 ;Count>i;i++)
	    {
	    	
	    	if(Count==1)
	    	{
	    		return null;
	    		
	    	}
	    	
	    	if(i!=id)
	    	{
	    		
	    		temp = ArrayBuilderString(temp, 5);
	    		
	    		temp[temp.length-1]=arr[i];
	    		
	    		
	    		
	    	}
	    }
	    
	    
	    
		return temp;
		
	}
	
	
	private boolean func_P(String Tabelle)
	{
		int Count = this.Menge.length;
		boolean test= false;
		for(int i =0;Count>i;i++ )
		{
			if(Menge[i].equals(Tabelle)==true)
			{
				test=true;
						
			}
		}
		return test;
		
	}
	
	
	
	protected boolean func_DELETE_Abfrageprüfung()
	{
		if( this.SQL_Abfrage[0][1] != "Einzeln")
		{
			
			return true;
		}else
		{
			return false;
			
		}
		
		
	}
	
	
	
	
//	*******************************   Debugger     ******************************************
	
	
	public void DB_Debug_Abfrage_Array()
	{
		System.out.println("**********************DEBUG Where Auswahl ! ***********************************************");
		
		if(SQL_Abfrage != null)
		{
			
			
			
			for (int i = 0; i < SQL_Abfrage.length; i++)
			
			{
				int g = i + 1 ;
				
				System.out.println("         +++++*************   Datensatz" + g + "   ******************+++++");
				
				for (int a = 0; a < SQL_Abfrage[0].length; a++)
				
				{
					System.out.println("Spalte " + a+"   ------>     " + SQL_Abfrage[i][a]);
									
				}
			      
		     }
			
		}else{
			
			
			System.out.println("                       Keine Relationen Ausgewählt!              ");
			
			
		}
		
		
		System.out.println("*****************************DEBUG ENDE *****************************************************");
		
		
	}
	
	
	public void DB_Debug_SQL_Abfrage_Validation()
	{
		
		
		String[][]SQL_Arr = this.SQL_Abfrage;
		int Count = SQL_Arr.length;
		int Count_var = 0;
		
		for(int i = 0 ;Count>i;i++)
		{
		
			Count_var = SQL_Arr.length;
			if(i==0)
			{
				Menge = ArrayBuilderString_Einzeln(Menge);
				Menge[0]= SQL_Arr[i][0];
				Menge = ArrayBuilderString_Einzeln(Menge);
				Menge[1]= SQL_Arr[i][1];
				SQL_Arr = func_Array_Leerung(SQL_Arr,i);
				
			}else{
				
				for(int k=0;Count_var>k;k++)
				
				{
					
					
					if(func_P(SQL_Arr[k][0])==true && func_P(SQL_Arr[k][1])==true )
					{
						System.out.println("**********************SQL Validation FEHLER ***********************************************");
						System.out.println("Die Relation :"+SQL_Arr[k][0] + " und " +SQL_Arr[k][1]+ " sind schon Vorhanden!");
						System.out.println("**********************         ENDE         ***********************************************");
						
						// FEHLER
						break;
					}
					
					
					if(func_P(SQL_Arr[k][0])==true)
					{
						Menge = ArrayBuilderString_Einzeln(Menge);
						Menge[Menge.length-1]= SQL_Arr[k][1];
						SQL_Arr = func_Array_Leerung(SQL_Arr,k);
						break;
					}
					if(func_P(SQL_Arr[k][1])==true)
					{
						Menge = ArrayBuilderString_Einzeln(Menge);
						Menge[Menge.length-1]= SQL_Arr[k][0];
						SQL_Arr = func_Array_Leerung(SQL_Arr,k);
						break;
						
					}
						
					
				}
				
			}
			
		
			
		}
		
		if(SQL_Arr == null)
		{
			
			System.out.println("**********************SQL Validation Bestanden ***********************************************");
			System.out.println("SQL Validation Erfolgreich!");
			System.out.println("**********************         ENDE         ***********************************************");
			
			
		}else
		{
		System.out.println("**********************SQL Validation FEHLER ***********************************************");
		System.out.println("SQL Validation nicht Bestanden!");
		System.out.println("**********************         ENDE         ***********************************************");
			
	}
		
		
	}
	
	private void HTBL_FILL()
	{
		 this.temp_Graph = this.SQL_Abfrage_Graph;

		 
		while(temp_Graph != null)
		{
			
			HTBL = ArrayBuilderString_Einzeln(HTBL);
			HTBL[HTBL.length-1]= this.temp_Graph[0][1];
			HTBL_LÖSCHEN(this.temp_Graph[0][1]);
			
			
			
		}
	}
	private void HTBL_LÖSCHEN(String HTBL_NAME)
	{
		String tempa[][] = null;
		
		for(int i = 0;i<this.temp_Graph.length;i++)
		{
			if(this.temp_Graph[i][1].equals(HTBL_NAME) != true)
			{
				tempa = ArrayBuilderString(tempa, 6);
				tempa[tempa.length-1][0] = this.temp_Graph[i][0];
				tempa[tempa.length-1][1] = this.temp_Graph[i][1];
				tempa[tempa.length-1][2] = this.temp_Graph[i][2];
				tempa[tempa.length-1][3] = this.temp_Graph[i][3];
				tempa[tempa.length-1][4] = this.temp_Graph[i][4];
				tempa[tempa.length-1][5] = this.temp_Graph[i][5];
				
			}
		}
		this.temp_Graph = tempa;
		
		
	}
	
	private void set_tempgraph(String HTBL_NAME)
	{
		temp_Graph = null;
		for(int i = 0;i<this.SQL_Abfrage_Graph.length;i++)
		{
			if(this.SQL_Abfrage_Graph[i][1].equals(HTBL_NAME) == true)
			{
				temp_Graph = ArrayBuilderString(temp_Graph, 6);
				temp_Graph[temp_Graph.length-1][0] = this.SQL_Abfrage_Graph[i][0];
				temp_Graph[temp_Graph.length-1][1] = this.SQL_Abfrage_Graph[i][1];
				temp_Graph[temp_Graph.length-1][2] = this.SQL_Abfrage_Graph[i][2];
				temp_Graph[temp_Graph.length-1][3] = this.SQL_Abfrage_Graph[i][3];
				temp_Graph[temp_Graph.length-1][4] = this.SQL_Abfrage_Graph[i][4];
				temp_Graph[temp_Graph.length-1][5] = this.SQL_Abfrage_Graph[i][5];
				
			}
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
