package archenoah.UIS;

import com.github.wolfie.refresher.Refresher;
import com.isconet.hah.HahUI;
import com.vaadin.ui.UI;

public class UI_Functions {

	
	public UI_Functions() {
		
	}

	
	
	
	
	public void addEx(Refresher rf)
	{
		 ((HahUI) UI.getCurrent().getSession().getSession().getAttribute("me")).addEx(rf);
	}
	public void remEx(Refresher rf)
	{
	    ((HahUI) UI.getCurrent().getSession().getSession().getAttribute("me")).remEx(rf);
	}
	
	
	
	
}
