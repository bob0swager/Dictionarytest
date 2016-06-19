/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journaldev.jsf.beans;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;

/**
 *
 * @author root
 */
@ManagedBean
public class ForwardBean {
    private String action;
    private String actionParam;

    public void navigate(PhaseEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String outcome = action; // Do your thing?
        facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, outcome);
    }
    public String redirectToLogin() {
        try { 
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
           Thread.currentThread().interrupt();
        }
        return "login";
    }
}
