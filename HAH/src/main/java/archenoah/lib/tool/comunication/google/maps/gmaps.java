package archenoah.lib.tool.comunication.google.maps;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import archenoah.config.MapsConfig;

public class gmaps {
	
	
	private String Start_Country;
	private String End_Country;
	private String Start_Strasse;
	private String End_Strasse;
	private String Start_PLZ;
	private String End_PLZ;
	private String Start_Ort;
	private String End_Ort;
	
	private String URL_GEN;
	
	private String ret = "";
	
	
	
	
	
	//public
	
	public String Status = "";
	public String Zeit_string = "";
	public String Strecke_string = "";
	public int Zeit_int; 
	public int Strecke_int; 	
	
	private static MapsConfig mpc = new MapsConfig(); 
	
	/******** LNG ************/
	public String LNG_H_VON;
	public String LNG_H_BIS;
	public String LNG_W_VON;
	public String LNG_W_BIS;
	/*************************/
	public gmaps() {
		// TODO Automatisch generierter Konstruktorstub
	}
	
	
	//*******************Start****************************//
	
	public void Start(String Land,String Strasse,String PLZ,String Ort)
	{
		this.Start_Country = Land;
		this.Start_Strasse = Strasse;
		this.Start_PLZ = PLZ;
		this.Start_Ort = Ort;
	}
	public void Ende(String Land,String Strasse,String PLZ,String Ort)
	{
		this.End_Country = Land;
		this.End_Strasse = Strasse;
		this.End_PLZ = PLZ;
		this.End_Ort = Ort;
	}
	
	private void URL_AUFBAU()
	{
		URL_GEN = "https://maps.googleapis.com/maps/api/directions/json?&key="+mpc.getEntry("ApiKey", "apiKey")+"&";
		URL_GEN	= URL_GEN +	"origin="+URL_Start()+"&";//Start
		URL_GEN	= URL_GEN + "destination=" + URL_Ende()+"&";//ENDE
		URL_GEN	= URL_GEN + "sensor=false";
		

	}
	
	private String URL_Start()
	{
		String Wert_URL = Start_Strasse+"+"+Start_PLZ+"+"+Start_Ort+","+Start_Country;
		
		try {
			Wert_URL = URLEncoder.encode(Wert_URL, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Wert_URL;
	}
	private String URL_Ende()
	{
		String Wert_URL = End_Strasse+"+"+End_PLZ+"+"+End_Ort+","+End_Country;
		
		try {
			Wert_URL = URLEncoder.encode(Wert_URL, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Wert_URL;
	}
	public void Datenholen()
	{
		URL_AUFBAU();
		
		try {
		    // Construct data
		  
		   
	
		
	
		    // Send data
		    URL url = new URL(URL_GEN);	
//		    System.out.println(URL_GEN);
		    URLConnection conn = url.openConnection();
		 //   conn.setDoOutput(true);
		  //  OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		    //wr.write(data);
		    //wr.flush();
		    // Get the response
		    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    String line;
		    
		    while ((line = rd.readLine()) != null) {
		    	
		    	ret = ret +  line;
		        // Process line...
		    }
		  //  wr.close();
		    rd.close();
		    
		 
		    Fill_GMAPS_Data();
		} catch (Exception e) {
			
			
			
		}
		
	}
	
	
	private void Fill_GMAPS_Data()
	{
		JSONParser parser = new JSONParser();
		
		
			Object obj;
			try {
				//System.out.println(ret);
				obj = parser.parse(ret);
				JSONObject jsonObject = (JSONObject) obj;
				
				
				Status = (String) jsonObject.get("status");
				
				if(Status.equals("OK") != true)
				{
					return;
				}
				
				
				
				JSONArray temp_arr = (JSONArray) jsonObject.get("routes");
				
				String temp = "";
				 for (Object c : temp_arr)
				    {
					 temp = temp +c;
				    }
				
				 obj = parser.parse(temp);
				 jsonObject = (JSONObject) obj;
			
				
			//****************************LEGS*****************************//
				temp_arr = (JSONArray) jsonObject.get("legs");
				temp = "";
				 for (Object c : temp_arr)
				    {
					 temp = temp +c;
				    }
				 
				 String Leg = temp;
				 
				
	
			//*************************************************************//	
		
			obj = parser.parse(Leg);
			jsonObject = (JSONObject) obj;
			obj = parser.parse(jsonObject.get("distance").toString());
			jsonObject = (JSONObject) obj;
			Strecke_string = jsonObject.get("text").toString(); 
		
			obj = parser.parse(Leg);
			jsonObject = (JSONObject) obj;
			obj = parser.parse(jsonObject.get("duration").toString());
			jsonObject = (JSONObject) obj;
			Zeit_string = jsonObject.get("text").toString(); 
				
			obj = parser.parse(Leg);
			jsonObject = (JSONObject) obj;
			obj = parser.parse(jsonObject.get("start_location").toString());
			jsonObject = (JSONObject) obj;
			LNG_H_VON = jsonObject.get("lat").toString(); 	
			LNG_W_VON = jsonObject.get("lng").toString(); 	
			
			obj = parser.parse(Leg);
			jsonObject = (JSONObject) obj;
			obj = parser.parse(jsonObject.get("end_location").toString());
			jsonObject = (JSONObject) obj;
			LNG_H_BIS = jsonObject.get("lat").toString(); 	
			LNG_W_BIS = jsonObject.get("lng").toString(); 		
			
//			String dd_temp = Strecke_string;
//			dd_temp = dd_temp.replaceAll(" km", "");
//			Strecke_int = Integer.valueOf(dd_temp);
//			dd_temp = Zeit_string;
//			dd_temp = dd_temp.replaceAll(" mins", "");
//			Zeit_int = Integer.valueOf(dd_temp);
			
			obj = parser.parse(Leg);
			jsonObject = (JSONObject) obj;
			obj = parser.parse(jsonObject.get("distance").toString());
			jsonObject = (JSONObject) obj;
			Strecke_int = Integer.parseInt(jsonObject.get("value").toString()); 
			
			float Strecke_float = Strecke_int;
			Strecke_float = Strecke_float/1000;
			Strecke_int = Math.round(Strecke_float);
		
			obj = parser.parse(Leg);
			jsonObject = (JSONObject) obj;
			obj = parser.parse(jsonObject.get("duration").toString());
			jsonObject = (JSONObject) obj;
			Zeit_int = Integer.parseInt(jsonObject.get("value").toString()); 
				 
			//********************************************************************//	 
				 
				 
			} catch (ParseException e) {
				// TODO Automatisch generierter Erfassungsblock
				e.printStackTrace();
			}
		
			
			
			
	

		
	}
	
	
	

}
