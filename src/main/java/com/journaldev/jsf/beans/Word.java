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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIPanel;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author root
 */
@ManagedBean(name = "word", eager = true)
@SessionScoped
@RequestScoped
public class Word implements Serializable{
    private int id_word;
    private String native_word;
    private String translatre_word;
    private UIPanel resultPanel;
    
    public int getId_word() {
        return id_word;
    }

    public void setId_word(int id_word) {
        this.id_word = id_word;
    }

    public String getNative_word() {
        return native_word;
    }

    public void setNative_word(String native_word) {
        this.native_word = native_word;
    }

    public String getTranslatre_word() {
        return translatre_word;
    }

    public void setTranslatre_word(String translatre_word) {
        this.translatre_word = translatre_word;
    }
    
    public void addWordToDataBase() throws ClassNotFoundException
    {

      //  VocabluaryDAO.addWord(native_word, translatre_word);
      
    }
    
    public List<Word> getAllWords() throws SQLException, ClassNotFoundException
    {
              int voc_id =  (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentVocabulary_id");
      //  int vocabulary_id =  (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id");
         FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("vocab_id", voc_id);

         List<Word> list = VocabluaryDAO.getWordList(voc_id);
        return list;
    }
    
    public void CountNumber()
     {
          String vocab_id =  (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("vocab_id");
        int i=0;
        List<Word> list = VocabluaryDAO.getWordList(Integer.parseInt(vocab_id));
        ExternalContext externalContext =  FacesContext.getCurrentInstance().getExternalContext();
        if(list.size() < 4)
        {
        
        try {
            externalContext.redirect("viewWords.xhtml?vocab_id="+vocab_id);
        } catch (IOException ex) {
            Logger.getLogger(Word.class.getName()).log(Level.SEVERE, null, ex);
        } 
        }
        else
        {
             try {
            externalContext.redirect("test.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(Word.class.getName()).log(Level.SEVERE, null, ex);
        } 
        }
     }
    
    public boolean deleteWord(String id_word)
     {
         VocabluaryDAO.deleteWord(id_word);
         return true;
     }
    
    public void checkResult() {
    FacesContext context = FacesContext.getCurrentInstance();
    resultPanel.setRendered(true);
    
      int voc_id =  (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("vocab_id");
  //Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
  //String vocabulkary_id = params.get("vocab_id");

    if (checkOperation(String.valueOf(voc_id))) {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correct", null));
     ExternalContext externalContext =  FacesContext.getCurrentInstance().getExternalContext();
        try {
            externalContext.redirect("viewWords.xhtml?vocab_id="+voc_id);
        } catch (IOException ex) {
            Logger.getLogger(Word.class.getName()).log(Level.SEVERE, null, ex);
        }
    } else {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Incorrect", null));

    }
   // return null;
  }
    
     private boolean checkOperation(String vocabulary_id) {
         boolean res = false;
       
        try {
            res =  VocabluaryDAO.addWord(native_word, translatre_word, vocabulary_id);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Word.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        return res;
  }
     
      public UIPanel getResultPanel() {
    return resultPanel;
  }
      public void setResultPanel(UIPanel resultPanel) {
    this.resultPanel = resultPanel;
  }
    
}
