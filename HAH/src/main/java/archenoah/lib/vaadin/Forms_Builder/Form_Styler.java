package archenoah.lib.vaadin.Forms_Builder;


import com.vaadin.server.ThemeResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupView.Content;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.Window;

public class Form_Styler {

	private AbsoluteLayout mainlayout;
	private Component comp;
	
	private float Width  = 1024;
	private float Highth = 786;
	
	public Form_Styler(AbsoluteLayout Arg_mainlayout,Component Arg_comp)
	{
		mainlayout = Arg_mainlayout;
		comp = Arg_comp;
	}
	
	public Window Window_Builder(Window main,String Titel)
	{
		float H_Width = mainlayout.getWidth();
		float H_Heigth = mainlayout.getHeight();
	
		
		
		Window winmain = new Window(Titel);
		winmain.setClosable(true);
		winmain.setDraggable(false);
		winmain.setResizable(false);
		
		winmain.setWidth(H_Width,winmain.UNITS_PIXELS);
		winmain.setHeight(H_Heigth,winmain.UNITS_PIXELS);
		
				
		winmain.setContent(comp);
		winmain.center();
		winmain.setModal(true);
		
		
		
		return winmain;
	}
	
	public TabSheet Tab_Builder(TabSheet main ,String Titel,String Resource)
	{
		HorizontalLayout hl = new HorizontalLayout();
		HorizontalLayout hl2 = new HorizontalLayout();
		hl.setSizeFull();
		hl.setMargin(false);
		hl2.addComponent(comp);
		hl.addComponent(hl2);
		hl.setExpandRatio(hl2,5);
		hl.setComponentAlignment(hl2, Alignment.TOP_CENTER);
		
		final Tab tab_Benutzerverwaltung = main.addTab(hl,Titel,new ThemeResource(Resource));
		tab_Benutzerverwaltung.setClosable(true);
		main.setSelectedTab(tab_Benutzerverwaltung);
		return main;
	}
	
	
	
	
	
	
	
	
	
}
