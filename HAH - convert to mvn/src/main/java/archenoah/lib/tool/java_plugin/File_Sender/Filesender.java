package archenoah.lib.tool.java_plugin.File_Sender;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.activation.MimetypesFileTypeMap;


import com.vaadin.addon.tableexport.TemporaryFileDownloadResource;
import com.vaadin.server.DownloadStream;
import com.vaadin.server.FileResource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;

public class Filesender {

public Filesender(final File fileToExport,final String exportFileName) {
	
	

	TemporaryFileDownloadResource resource;
    try {
        resource =
                new TemporaryFileDownloadResource(UI.getCurrent().getUI(), exportFileName, Getmimetype(fileToExport), fileToExport);
        UI.getCurrent().getUI().getPage().open(resource, null, false);
    } catch (final FileNotFoundException e) {
      

    }
    
}

private String Getmimetype(File temp)
{
	String s =  new MimetypesFileTypeMap().getContentType(temp);
return s;
}




}




