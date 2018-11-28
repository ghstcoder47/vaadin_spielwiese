package archenoah.lib.tool.comunication.php;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import archenoah.lib.tool.java_plugin.array_builder.classes.ArrayBuilder_Mehrfach;





public class php {

	private String URL_S;
	private String Data[][];
	private String ret = "";
	public php(String URL)
	{
		URL_S = URL;
		
		
	}
	
	
	public void data(String Key,String Key_Coding,String Value ,String Value_Coding)
	{
		
		ArrayBuilder_Mehrfach a = new ArrayBuilder_Mehrfach(Data,4);
		
		Data = a.Array_Holen();
		
		int menge = Data.length -1 ;
		Data[menge][0] = Key;
		Data[menge][1] = Key_Coding;
		Data[menge][2] = Value;
		Data[menge][3] = Value_Coding;
		
	}
	
	
	public String Send()
	{
		
		try {
		    // Construct data
		    String data = null;
		    int Count = Data.length;
		   
		    for(int i = 0 ;i < Count;i++)
	    {
		    	if(i == 0)
		    	{
		    		
		    		data = URLEncoder.encode((String)Data[i][0],(String) Data[i][1]) + "=" + URLEncoder.encode((String)Data[i][2], (String)Data[i][3]);
	    		  
			   
		    	}else {
	    		data += "&" + URLEncoder.encode((String)Data[i][0], (String)Data[i][1]) + "=" + URLEncoder.encode((String)Data[i][2], (String)Data[i][3]);
	    	}
	    	
	    }
		 
	
		    // Send data
		    URL url = new URL(URL_S);		    
		    URLConnection conn = url.openConnection();
		    conn.setDoOutput(true);
		    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		    wr.write(data);
		    wr.flush();
		    // Get the response
		    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    String line;
		    
		    while ((line = rd.readLine()) != null) {
		    	
		    	ret = ret +  line;
		        // Process line...
		    }
		    wr.close();
		    rd.close();
		} catch (Exception e) {
			
			
			
		}
			
		
		return ret;
	}
	
	
}
