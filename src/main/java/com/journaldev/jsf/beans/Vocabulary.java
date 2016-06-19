/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journaldev.jsf.beans;

import com.journaldev.jsf.dao.VocabluaryDAO;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIPanel;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author root
 */
@ManagedBean(name = "vocabulary", eager = true)
public class Vocabulary implements Serializable{
    private int id_vocabulary;
    private String name_vocabulary;
    private String language_vocabulary;
    int currentVocabulary;

    public int getCurrentVocabulary() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("currentVocabulary_id", currentVocabulary);
        return currentVocabulary;
    }

    public void setCurrentVocabulary(int currentVocabulary) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("currentVocabulary_id", currentVocabulary);
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        try {
           // context.redirect(context.getRequestContextPath() + "viewWords.xhtml?vocab_id="+String.valueOf(currentVocabulary));
           context.redirect(context.getRequestContextPath() + "viewWords.xhtml?vocab_id="+String.valueOf(currentVocabulary));
        } catch (IOException ex) {
            Logger.getLogger(Vocabulary.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private UIPanel resultPanel;

    public int getId_vocabulary() {
        
        return id_vocabulary;
    }

    public void setId_vocabulary(int id_vocabulary) {
        this.id_vocabulary = id_vocabulary;
    }

    public String getName_vocabulary() {
        return name_vocabulary;
    }

    public void setName_vocabulary(String name_vocabulary) {
        this.name_vocabulary = name_vocabulary;
    }

    public String getLanguage_vocabulary() {
        return language_vocabulary;
    }

    public void setLanguage_vocabulary(String language_vocabulary) {
        this.language_vocabulary = language_vocabulary;
    }
    
    
    
    
    public void addVocabularyToDataBase() throws ClassNotFoundException
    {

     //   VocabluaryDAO.addVocabulary(name_vocabulary, language_vocabulary);
      
    }
    
    public List<Vocabulary> getAllVocabularies() throws SQLException, ClassNotFoundException
    {
        List<Vocabulary> list = VocabluaryDAO.getVocabularyList();
        return list;
    }
    
    public void checkResult() {
    FacesContext context = FacesContext.getCurrentInstance();
    resultPanel.setRendered(true);

    if (checkOperation()) {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correct", null));
      ExternalContext externalContext =  FacesContext.getCurrentInstance().getExternalContext();
        try {
            externalContext.redirect("viewVocabularies.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(Word.class.getName()).log(Level.SEVERE, null, ex);
        }
    } else {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Incorrect", null));
    }
   // return null;
  }
    
     private boolean checkOperation() {
         boolean res = false;
        int user_id =  (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id");
         res =  VocabluaryDAO.addVocabulary(name_vocabulary, language_vocabulary, user_id);
        return res;
  }
     
     public boolean deleteVocabulary(String id_vocabulary)
     {
         VocabluaryDAO.deleteVocabulary(id_vocabulary);
         return false;
     }
     
      public UIPanel getResultPanel() {
    return resultPanel;
  }
      public void setResultPanel(UIPanel resultPanel) {
    this.resultPanel = resultPanel;
  }
    
}
