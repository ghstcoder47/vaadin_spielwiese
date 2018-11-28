package archenoah.lib.tool.comunication.dbclass;


/**
 *                      Isconet PHP Datenbank Klasse Spalte
 *                                  v.0.1
 *                          @author Asan Kaceri
 */
public class Data_DB_Spalten extends Data_DB_Sammlung{

	protected String[][]SQL_Spalten= null;  
	
	// **************************************   Public Object  ***************************************
	
	public C_Standard Standard;
	public C_Advanced Advanced;
	 
	
	// **************************************   Private ***************************************
	
		
		
		
	
	public Data_DB_Spalten() {
		// TODO Automatisch generierter Konstruktorstub
		Standard = new C_Standard();
		Advanced = new C_Advanced();
	}

	
	// **************************************   Public  ***************************************
	
	
	public void DB_Data_Leeren()
	{
		SQL_Spalten = null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	// **************************************   Protected  ***************************************
	
	protected String func_SQL_Spalten()
	{

		// Array zusammen führung
		
		
		if(Standard.SQL_Spalten == null && Advanced.SQL_Spalten == null)
		{
			System.out.println("*******************************FEHLER!!!*********************");
			System.out.println("*          FEHLER 1    Keine Spalten Vorhanden!             *");
			System.out.println("*************************************************************");
			return "";
		}
		
		if(Standard.SQL_Spalten != null && Advanced.SQL_Spalten != null)
		{
			// Länge Standard
			
			
			
			int Std_Count_D_Anzahl = Standard.SQL_Spalten.length;
			int Std_count_S_Anzahl = Standard.SQL_Spalten[0].length;
			int Adv_Count_D_Anzahl = Advanced.SQL_Spalten.length;
			int Adv_count_S_Anzahl = Advanced.SQL_Spalten[0].length;
			
			this.SQL_Spalten = new String[Adv_Count_D_Anzahl+Std_Count_D_Anzahl][Adv_count_S_Anzahl+Std_count_S_Anzahl];
			
			
			System.arraycopy(Standard.SQL_Spalten, 0, this.SQL_Spalten, 0,Std_Count_D_Anzahl);	
		
			
			
			System.arraycopy(Advanced.SQL_Spalten, 0, this.SQL_Spalten, Std_Count_D_Anzahl,Adv_Count_D_Anzahl);
			
		}
		
		
		
		if(Standard.SQL_Spalten == null && Advanced.SQL_Spalten != null)
		{
			
			int Adv_Count_D_Anzahl = Advanced.SQL_Spalten.length;
			int Adv_count_S_Anzahl = Advanced.SQL_Spalten[0].length;
			
			this.SQL_Spalten = new String[Adv_Count_D_Anzahl][Adv_count_S_Anzahl];
			
			
			System.arraycopy(Advanced.SQL_Spalten, 0, this.SQL_Spalten, 0,Adv_Count_D_Anzahl);
			
			
		}
		if(Standard.SQL_Spalten != null && Advanced.SQL_Spalten == null)
		{
			
			int Std_Count_D_Anzahl = Standard.SQL_Spalten.length;
			int Std_count_S_Anzahl = Standard.SQL_Spalten[0].length;
			this.SQL_Spalten = new String[Std_Count_D_Anzahl][Std_count_S_Anzahl];
			
			System.arraycopy(Standard.SQL_Spalten, 0, this.SQL_Spalten, 0,Std_Count_D_Anzahl);	
			
		}
		
		
		
		
		
		
		
		if(SQL_Spalten==null)
		{
		
			
			
			
			System.out.println("*******************************FEHLER!!!*********************");
			System.out.println("*          FEHLER 1    Keine Spalten Vorhanden!             *");
			System.out.println("*************************************************************");
			return "";
			
		}else
			
		{
		
		int Count = SQL_Spalten.length;
		
		
		
		String SQL = "";
		
		
			if(SQL_Spalten[0][1]!= "Custom")
			{
				if(SQL_Spalten[0][1]!= "ALLE")
				{
					for(int i=0;Count>i;i++)
					{
						
						if(SQL != "")
						{
							SQL = SQL + " , ";
						}
						
						
							switch(SQL_Spalten[i][1])
							{
							
							case "DISTINCT":
								
								SQL = SQL + " DISTINCT " + SQL_Spalten[i][0];
								break;
								
							case "Einzeln":
								
								SQL = SQL + SQL_Spalten[i][0]; 
								break;
								
						
							case "First":
								
								SQL = SQL + SQL_Spalten[i][0];
								break;
							case "Last":
								
								SQL = SQL + SQL_Spalten[i][0];
								break;
							case "Max":
								
								SQL = SQL + SQL_Spalten[i][0];
								break;
								
							case "Min":
								
								SQL = SQL + SQL_Spalten[i][0];
								break;
							case "Ucase":
								
								SQL = SQL + SQL_Spalten[i][0];
								break;
							case "Lcase":
								
								SQL = SQL + SQL_Spalten[i][0];
								break;
							case "MID":
								
								SQL = SQL + SQL_Spalten[i][0];	
								break;
							case "Len":
								
								SQL = SQL + SQL_Spalten[i][0];
								break;
							case "Round":
								
								SQL = SQL + SQL_Spalten[i][0];	
								break;
							case "Now":
								
								SQL = SQL + SQL_Spalten[i][0];
								break;
							case "Format":
								
								SQL = SQL + SQL_Spalten[i][0];
								break;
							case "Count":
								
								SQL = SQL + SQL_Spalten[i][0];
								break;
							case "AVG":
								
								SQL = SQL + SQL_Spalten[i][0];
								break;
							case "SUM":
								
								SQL = SQL + SQL_Spalten[i][0];	
								break;
							}
						
						
						
						
					}
				
				}else
				{
					SQL = SQL + " * ";
					
				}
	
	
		
			}else{
				
				SQL = SQL + SQL_Spalten[0][0];
			}
		
		
			
			
		return SQL;
		}
	}
	
	
	
	
	
	/*
	 * 
	 * 
	 * 								Class Einzelauswahl
	 * 
	 * 
	 * 
	 * */
	public class C_Standard extends Data_DB_Sammlung
	{
		protected String[][]SQL_Spalten = null;
		
		
		
		
		public void DB_set_Spalten_All()
		 {
			if (this.SQL_Spalten ==  null )
			 {
				this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
				this.SQL_Spalten[0][0] ="*"; 
				this.SQL_Spalten[0][1] = "ALLE";
			 }else
			 {
				 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
				 this.SQL_Spalten[0][0] ="*"; 
				 this.SQL_Spalten[0][1] = "ALLE";
				 
			 }
			 
		 }
		
		public void DB_set_Spalten_Einzeln(String Spalten)
		 {
			 if (this.SQL_Spalten==  null )
			 {
				 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
				 this.SQL_Spalten[0][0] = Spalten; 
				 this.SQL_Spalten[0][1] = "Einzeln";
				 
			 }else
			 {
				 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
				 
				 this.SQL_Spalten[this.SQL_Spalten.length-1][0] = Spalten; 
				 this.SQL_Spalten[this.SQL_Spalten.length-1][1] = "Einzeln"; 
			 }
					 
			 
		 }
		
		
		public void DB_set_Spalten_Einzeln(String Spalten,String NeuerSpaltenname)
		 {
			 if (this.SQL_Spalten==  null )
			 {
				 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
				 this.SQL_Spalten[0][0] = Spalten +" AS " +NeuerSpaltenname; 
				 this.SQL_Spalten[0][1] = "Einzeln";
				 
			 }else
			 {
				 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
				 
				 this.SQL_Spalten[this.SQL_Spalten.length-1][0] = Spalten +" AS " +NeuerSpaltenname;  
				 this.SQL_Spalten[this.SQL_Spalten.length-1][1] = "Einzeln"; 
			 }
					 
			 
		 }
		
		
		public void DB_set_Spalten_Leeren()
		{
		this.SQL_Spalten = null;
			
		}
		
		
		
		
		
		
	}
	
	/*
	 * 
	 * 
	 * 								Class Mehrfachauswahl
	 * 
	 * 
	 * 
	 * */
	public class C_Advanced extends Data_DB_Sammlung
	{
		
		protected String[][]SQL_Spalten = null;
		
		
		
		
	
		/*
		 * 
		 * 					Distinct
		 * 
		 * */
		public void DB_Dinstinct(String Spalten)
		 {
			 if (this.SQL_Spalten ==  null )
			 {
				 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
				 this.SQL_Spalten[0][0] = Spalten ; 
				 this.SQL_Spalten[0][1] = "DISTINCT"; 
			 
			 }else
			 {
				 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
				 this.SQL_Spalten[this.SQL_Spalten.length-1][0] = Spalten; 
				 this.SQL_Spalten[this.SQL_Spalten.length-1][1] = "DISTINCT"; 
			 }
			 
		 }
	
		public void DB_Dinstinct(String Spalten,String Neuer_Spaltenname)
		 {
			 if (this.SQL_Spalten ==  null )
			 {
				 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
				 this.SQL_Spalten[0][0] = Spalten+" AS " + Neuer_Spaltenname; 
				 this.SQL_Spalten[0][1] = "DISTINCT"; 
			 
			 }else
			 {
				 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
				 this.SQL_Spalten[this.SQL_Spalten.length-1][0] = Spalten+" AS " + Neuer_Spaltenname; 
				 this.SQL_Spalten[this.SQL_Spalten.length-1][1] = "DISTINCT"; 
			 }
			 
		 }
		
		/*
		 * 
		 * 					Custom
		 * 
		 * */
	public void DB_Custom(String Spalten)
	 {
		 if (this.SQL_Spalten==  null )
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[0][0] = Spalten; 
			 this.SQL_Spalten[0][1] = "Custom"; 
		 
		 }else
		 {
			 
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[this.SQL_Spalten.length-1][0] = Spalten; 
			 this.SQL_Spalten[this.SQL_Spalten.length-1][1] = "Custom"; 
		 }
		 
	 }
	
	/*
	 * 
	 * 					First
	 * 
	 * */
	public void DB_First(String Spalten)
	 {
		 if (this.SQL_Spalten ==  null )
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[0][0] = " FIRST("+Spalten+")" ; 
			 this.SQL_Spalten[0][1] = "First"; 
		 
		 }else
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[this.SQL_Spalten.length-1][0] =" FIRST("+Spalten+")"; 
			 this.SQL_Spalten[this.SQL_Spalten.length-1][1] = "First"; 
		 }
		 
	 }
	public void DB_First(String Spalten,String Neuer_Spaltenname)
	 {
		 if (this.SQL_Spalten ==  null )
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[0][0] = " FIRST("+Spalten+")" +" AS " + Neuer_Spaltenname; ; 
			 this.SQL_Spalten[0][1] = "First"; 
		 
		 }else
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[this.SQL_Spalten.length-1][0] = " FIRST("+Spalten+")" +" AS " + Neuer_Spaltenname;; 
			 this.SQL_Spalten[this.SQL_Spalten.length-1][1] = "First"; 
		 }
		 
	 }
	/*
	 * 
	 * 					Last
	 * 
	 * */
	public void DB_Last(String Spalten)
	 {
		 if (this.SQL_Spalten ==  null )
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[0][0] = " LAST("+Spalten+")" ; 
			 this.SQL_Spalten[0][1] = "Last"; 
		 
		 }else
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[this.SQL_Spalten.length-1][0] = " LAST("+Spalten+")"; 
			 this.SQL_Spalten[this.SQL_Spalten.length-1][1] = "Last"; 
		 }
		 
	 }
	public void DB_Last(String Spalten,String Neuer_Spaltenname)
	 {
		 if (this.SQL_Spalten ==  null )
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[0][0] = " LAST("+Spalten+")" +" AS " + Neuer_Spaltenname; ; 
			 this.SQL_Spalten[0][1] = "Last"; 
		 
		 }else
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[this.SQL_Spalten.length-1][0] = " LAST("+Spalten+")" +" AS " + Neuer_Spaltenname;; 
			 this.SQL_Spalten[this.SQL_Spalten.length-1][1] = "Last"; 
		 }
		 
	 }
	/*
	 * 
	 * 					Max
	 * 
	 * */
	public void DB_Max(String Spalten)
	 {
		 if (this.SQL_Spalten ==  null )
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[0][0] = " MAX("+Spalten+")" ; 
			 this.SQL_Spalten[0][1] = "Max"; 
		 
		 }else
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[this.SQL_Spalten.length-1][0] = " MAX("+Spalten+")"; 
			 this.SQL_Spalten[this.SQL_Spalten.length-1][1] = "Max"; 
		 }
		 
	 }
	public void DB_Max(String Spalten,String Neuer_Spaltenname)
	 {
		 if (this.SQL_Spalten ==  null )
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[0][0] = " MAX("+Spalten+")" +" AS " + Neuer_Spaltenname; ; 
			 this.SQL_Spalten[0][1] = "Max"; 
		 
		 }else
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[this.SQL_Spalten.length-1][0] = " MAX("+Spalten+")" +" AS " + Neuer_Spaltenname;; 
			 this.SQL_Spalten[this.SQL_Spalten.length-1][1] = "Max"; 
		 }
		 
	 }
	/*
	 * 
	 * 					Min
	 * 
	 * */
	public void DB_Min(String Spalten)
	 {
		 if (this.SQL_Spalten ==  null )
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[0][0] = " MIN("+Spalten+")" ; 
			 this.SQL_Spalten[0][1] = "Min"; 
		 
		 }else
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[this.SQL_Spalten.length-1][0] = " MIN("+Spalten+")" ; 
			 this.SQL_Spalten[this.SQL_Spalten.length-1][1] = "Min"; 
		 }
		 
	 }
	public void DB_Min(String Spalten,String Neuer_Spaltenname)
	 {
		 if (this.SQL_Spalten ==  null )
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[0][0] = " MIN("+Spalten+")"  +" AS " + Neuer_Spaltenname; ; 
			 this.SQL_Spalten[0][1] = "Min"; 
		 
		 }else
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[this.SQL_Spalten.length-1][0] = " MIN("+Spalten+")"  +" AS " + Neuer_Spaltenname;; 
			 this.SQL_Spalten[this.SQL_Spalten.length-1][1] = "Min"; 
		 }
		 
	 }

	/*
	 * 
	 * 					Ucase
	 * 
	 * */
	public void DB_Ucase(String Spalten)
	 {
		 if (this.SQL_Spalten ==  null )
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[0][0] = " UCASE("+Spalten+")"  ; 
			 this.SQL_Spalten[0][1] = "Ucase"; 
		 
		 }else
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[this.SQL_Spalten.length-1][0] = " UCASE("+Spalten+")"; 
			 this.SQL_Spalten[this.SQL_Spalten.length-1][1] = "Ucase"; 
		 }
		 
	 }
	public void DB_Ucase(String Spalten,String Neuer_Spaltenname)
	 {
		 if (this.SQL_Spalten ==  null )
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[0][0] = " UCASE("+Spalten+")" +" AS " + Neuer_Spaltenname; ; 
			 this.SQL_Spalten[0][1] = "Ucase"; 
		 
		 }else
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[this.SQL_Spalten.length-1][0] = " UCASE("+Spalten+")" +" AS " + Neuer_Spaltenname;; 
			 this.SQL_Spalten[this.SQL_Spalten.length-1][1] = "Ucase"; 
		 }
		 
	 }
	/*
	 * 
	 * 					Lcase
	 * 
	 * */
	public void DB_Lcase(String Spalten)
	 {
		 if (this.SQL_Spalten ==  null )
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[0][0] = " LCASE("+Spalten+")" ; 
			 this.SQL_Spalten[0][1] = "Lcase"; 
		 
		 }else
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[this.SQL_Spalten.length-1][0] = " LCASE("+Spalten+")"; 
			 this.SQL_Spalten[this.SQL_Spalten.length-1][1] = "Lcase"; 
		 }
		 
	 }
	public void DB_Lcase(String Spalten,String Neuer_Spaltenname)
	 {
		 if (this.SQL_Spalten ==  null )
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[0][0] = " LCASE("+Spalten+")" +" AS " + Neuer_Spaltenname; ; 
			 this.SQL_Spalten[0][1] = "Lcase"; 
		 
		 }else
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[this.SQL_Spalten.length-1][0] = " LCASE("+Spalten+")" +" AS " + Neuer_Spaltenname;; 
			 this.SQL_Spalten[this.SQL_Spalten.length-1][1] = "Lcase"; 
		 }
		 
	 }
	
	/*
	 * 
	 * 					MID
	 * 
	 * */
	public void DB_MID(String Spalten,String Start,String Laenge)
	 {
		 if (this.SQL_Spalten ==  null )
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[0][0] = " MID("+Spalten+","+Start+","+Laenge+")" ; 
			 this.SQL_Spalten[0][1] = "MID"; 
			
			 
			 
		 
		 }else
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[this.SQL_Spalten.length-1][0] =  " MID("+Spalten+","+Start+","+Laenge+")" ;; 
			 this.SQL_Spalten[this.SQL_Spalten.length-1][1] = "MID";
			 
		 }
		 
	 }
	public void DB_MID(String Spalten,String Start,String Laenge,String Neuer_Spaltenname)
	 {
		 if (this.SQL_Spalten ==  null )
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[0][0] =  " MID("+Spalten+","+Start+","+Laenge+")"  +" AS " + Neuer_Spaltenname; ; 
			 this.SQL_Spalten[0][1] = "MID"; 
			 
		 }else
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[this.SQL_Spalten.length-1][0] =  " MID("+Spalten+","+Start+","+Laenge+")"  +" AS " + Neuer_Spaltenname;; 
			 this.SQL_Spalten[this.SQL_Spalten.length-1][1] = "MID";
		 }
		 
	 }
	/*
	 * 
	 * 					Len
	 * 
	 * */
	public void DB_Len(String Spalten)
	 {
		 if (this.SQL_Spalten ==  null )
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[0][0] = " LEN("+Spalten+")" ; 
			 this.SQL_Spalten[0][1] = "Len"; 
		 
		 }else
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[this.SQL_Spalten.length-1][0] = " LEN("+Spalten+")"; 
			 this.SQL_Spalten[this.SQL_Spalten.length-1][1] = "Len"; 
		 }
		 
	 }
	public void DB_Len(String Spalten,String Neuer_Spaltenname)
	 {
		 if (this.SQL_Spalten ==  null )
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[0][0] = " LEN("+Spalten+")" +" AS " + Neuer_Spaltenname; ; 
			 this.SQL_Spalten[0][1] = "Len"; 
		 
		 }else
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[this.SQL_Spalten.length-1][0] = " LEN("+Spalten+")" +" AS " + Neuer_Spaltenname;; 
			 this.SQL_Spalten[this.SQL_Spalten.length-1][1] = "Len"; 
		 }
		 
	 }
	
	
	/*
	 * 
	 * 					Round
	 * 
	 * */
	public void DB_Round(String Spalten,String Dezimalstellen)
	 {
		 if (this.SQL_Spalten ==  null )
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[0][0] = " ROUND("+Spalten+","+Dezimalstellen+")" ; 
			 this.SQL_Spalten[0][1] = "Round"; 
			 this.SQL_Spalten[0][2] = Dezimalstellen; 
		 
		 }else
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[this.SQL_Spalten.length-1][0] = " ROUND("+Spalten+","+Dezimalstellen+")"; 
			 this.SQL_Spalten[this.SQL_Spalten.length-1][1] = "Round";
			 this.SQL_Spalten[this.SQL_Spalten.length-1][2] = Dezimalstellen;
			 
		 }
		 
	 }
	public void DB_Round(String Spalten,String Dezimalstellen,String Neuer_Spaltenname)
	 {
		 if (this.SQL_Spalten ==  null )
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[0][0] = " ROUND("+Spalten+","+Dezimalstellen+")" +" AS " + Neuer_Spaltenname; ; 
			 this.SQL_Spalten[0][1] = "Round"; 
			 this.SQL_Spalten[0][2] = Dezimalstellen; 
		 
		 }else
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[this.SQL_Spalten.length-1][0] = " ROUND("+Spalten+","+Dezimalstellen+")" +" AS " + Neuer_Spaltenname;; 
			 this.SQL_Spalten[this.SQL_Spalten.length-1][1] = "Round"; 
			 this.SQL_Spalten[this.SQL_Spalten.length-1][2] = Dezimalstellen;
		 }
		 
	 }
	
	
	
	/*
	 * 
	 * 					Now
	 * 
	 * */
	public void DB_Now()
	 {
		 if (this.SQL_Spalten ==  null )
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[0][0] = " NOW() " ; 
			 this.SQL_Spalten[0][1] = "Now"; 
		 
		 }else
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[this.SQL_Spalten.length-1][0] = " NOW() "; 
			 this.SQL_Spalten[this.SQL_Spalten.length-1][1] = "Now"; 
		 }
		 
	 }
	public void DB_Now(String Spalten,String Neuer_Spaltenname)
	 {
		 if (this.SQL_Spalten ==  null )
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[0][0] = " NOW() " +" AS " + Neuer_Spaltenname;
			 this.SQL_Spalten[0][1] = "Now"; 
		 
		 }else
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[this.SQL_Spalten.length-1][0] = " NOW() " +" AS " + Neuer_Spaltenname;
			 this.SQL_Spalten[this.SQL_Spalten.length-1][1] = "Now"; 
		 }
		 
	 }
	
	/*
	 * 
	 * 					Format
	 * 
	 * */
	public void DB_Format(String Spalten,String Format)
	 {
		 if (this.SQL_Spalten ==  null )
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[0][0] = " FORMAT("+Spalten+","+Format+")" ; 
			 this.SQL_Spalten[0][1] = "Format"; 
			 this.SQL_Spalten[0][2] = Format; 
		 
		 }else
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 3);
			 this.SQL_Spalten[this.SQL_Spalten.length-1][0] = " FORMAT("+Spalten+","+Format+")"; 
			 this.SQL_Spalten[this.SQL_Spalten.length-1][1] = "Format"; 
			 this.SQL_Spalten[this.SQL_Spalten.length-1][2] =  Format; 
		 }
		 
	 }
	public void DB_Format(String Spalten,String Format,String Neuer_Spaltenname)
	 {
		 if (this.SQL_Spalten ==  null )
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[0][0] = " FORMAT("+Spalten+","+Format+")" +" AS " + Neuer_Spaltenname; ; 
			 this.SQL_Spalten[0][1] = "Format"; 
			 this.SQL_Spalten[0][2] = Format; 
		 
		 }else
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[this.SQL_Spalten.length-1][0] = " FORMAT("+Spalten+","+Format+")" +" AS " + Neuer_Spaltenname;; 
			 this.SQL_Spalten[this.SQL_Spalten.length-1][1] = "Format"; 
			 this.SQL_Spalten[this.SQL_Spalten.length-1][2] =  Format; 
		 }
		 
	 }
	/*
	 * 
	 * 					Count
	 * 
	 * */
	public void DB_Count(String Spalten)
	 {
		 if (this.SQL_Spalten ==  null )
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[0][0] = " COUNT("+Spalten+")" ; 
			 this.SQL_Spalten[0][1] = "Count"; 
		 
		 }else
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[this.SQL_Spalten.length-1][0] = " COUNT("+Spalten+")" ; 
			 this.SQL_Spalten[this.SQL_Spalten.length-1][1] = "Count"; 
		 }
		 
	 }
	public void DB_Count(String Spalten,String Neuer_Spaltenname)
	 {
		 if (this.SQL_Spalten ==  null )
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[0][0] = " COUNT("+Spalten+")"  +" AS " + Neuer_Spaltenname; ; 
			 this.SQL_Spalten[0][1] = "Count"; 
		 
		 }else
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[this.SQL_Spalten.length-1][0] = " COUNT("+Spalten+")"  +" AS " + Neuer_Spaltenname;; 
			 this.SQL_Spalten[this.SQL_Spalten.length-1][1] = "Count"; 
		 }
		 
	 }
	/*
	 * 
	 * 					Durchschnitt
	 * 
	 * */
	public void DB_Durchschnitt(String Spalten)
	 {
		 if (this.SQL_Spalten ==  null )
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[0][0] = " AVG("+Spalten+")"  ; 
			 this.SQL_Spalten[0][1] = "AVG"; 
			
		 
		 }else
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 3);
			 this.SQL_Spalten[this.SQL_Spalten.length-1][0] = " AVG("+Spalten+")" ; 
			 this.SQL_Spalten[this.SQL_Spalten.length-1][1] = "AVG"; 
			 
		 }
		 
	 }
	public void DB_Durchschnitt(String Spalten,String Neuer_Spaltenname)
	 {
		 if (this.SQL_Spalten ==  null )
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[0][0] = " AVG("+Spalten+")"  +" AS " + Neuer_Spaltenname; ; 
			 this.SQL_Spalten[0][1] = "AVG"; 
		 }else
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[this.SQL_Spalten.length-1][0] = " AVG("+Spalten+")"  +" AS " + Neuer_Spaltenname;; 
			 this.SQL_Spalten[this.SQL_Spalten.length-1][1] = "AVG";  
		 }
		 
	 }
	/*
	 * 
	 * 					Sum
	 * 
	 * */
	public void DB_Sum(String Spalten)
	 {
		 if (this.SQL_Spalten ==  null )
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[0][0] = " SUM("+Spalten+")"  ; 
			 this.SQL_Spalten[0][1] = "SUM"; 
			
		 
		 }else
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 3);
			 this.SQL_Spalten[this.SQL_Spalten.length-1][0] = " SUM("+Spalten+")"; 
			 this.SQL_Spalten[this.SQL_Spalten.length-1][1] = "SUM"; 
			 
		 }
		 
	 }
	public void DB_Sum(String Spalten,String Neuer_Spaltenname)
	 {
		 if (this.SQL_Spalten ==  null )
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[0][0] = " SUM("+Spalten+")" +" AS " + Neuer_Spaltenname; ; 
			 this.SQL_Spalten[0][1] = "SUM"; 
		 }else
		 {
			 this.SQL_Spalten = ArrayBuilderString(SQL_Spalten, 2);
			 this.SQL_Spalten[this.SQL_Spalten.length-1][0] = " SUM("+Spalten+")" +" AS " + Neuer_Spaltenname;; 
			 this.SQL_Spalten[this.SQL_Spalten.length-1][1] = "SUM";  
		 }
		 
	 }
	
		

	
	
	
	
	
	
	
	
	
	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
