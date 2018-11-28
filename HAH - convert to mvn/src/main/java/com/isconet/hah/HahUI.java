package com.isconet.hah;

import javax.servlet.annotation.WebServlet;

import org.vaadin.jonatan.contexthelp.ContextHelp;

import archenoah.Init_CMS;

import com.github.wolfie.refresher.Refresher;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@Theme("supermetrosimple")
//@com.vaadin.annotations.Push glassfish drops requests!
public class HahUI extends UI {

    @WebServlet(value = "/*", asyncSupported = false)
    @VaadinServletConfiguration(productionMode = true, ui = HahUI.class, widgetset = "com.isconet.hah.widgetset.HahWidgetset")
    public static class Servlet extends VaadinServlet {
    }

    final ContextHelp contextHelp = new ContextHelp();
    
    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(HahUI.class);
    
    @Override
    protected void init(VaadinRequest request) {
        
        contextHelp.extend(this);
        
        UI.getCurrent().getSession().getSession().setAttribute("me", this);
        
        Init_CMS init = new Init_CMS();
        
        
    }

    public void addEx(Refresher rf) {
        addExtension(rf);
    }

    public void remEx(Refresher rf) {
        removeExtension(rf);
    }

}