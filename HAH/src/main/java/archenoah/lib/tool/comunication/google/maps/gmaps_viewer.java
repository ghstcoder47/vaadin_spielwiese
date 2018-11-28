package archenoah.lib.tool.comunication.google.maps;




public class gmaps_viewer {

	private String URL = "http://php.isconet-applications.de/Lizenzverwaltung_Vaadin/";
	private String PHPFILE ="gmaps.php";
	
	public String gmaps_php;
	
	public gmaps_viewer(String Von,String Nach,String Höhe,String Breite)
	{
		
		
		gmaps_php = URL + PHPFILE +"?von="+Von+"&nach="+Nach+"&hoehe="+Höhe+"&breite="+Breite;
		
		
	}
	
	
	
	
	
	
	
	
	

}
