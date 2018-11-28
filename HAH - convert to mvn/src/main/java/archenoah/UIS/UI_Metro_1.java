package archenoah.UIS;

import org.vaadin.jonatan.contexthelp.ContextHelp;

import archenoah.Init_CMS;

import com.github.wolfie.refresher.Refresher;
import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
@Theme("supermetrosimple")
public class UI_Metro_1 extends UI{

	 final ContextHelp contextHelp = new ContextHelp();

	 
	public UI_Metro_1()  {
		// TODO Automatisch generierter Konstruktorstub
		
	}

	@Override
	public void init(VaadinRequest request) {
		// TODO Automatisch generierter Methodenstub
		
		contextHelp.extend(this);
		
		

		
		
		
		
		
		
		Init_CMS init = new Init_CMS();
		

		UI.getCurrent().getSession().getSession().setAttribute("me", this);
		
		
		
	}
	
	
	public void addEx(Refresher rf)
	{
		
		addExtension(rf);
	}
	public void remEx(Refresher rf)
	{
		
		removeExtension(rf);
	}
	

	
	


	
	public String ja()
	{
		return "";
	}

}
