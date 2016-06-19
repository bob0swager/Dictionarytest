/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journaldev.jsf.beans;

import com.journaldev.jsf.dao.VocabluaryDAO;
import static com.sun.faces.facelets.util.Path.context;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author root
 */



@ManagedBean(name = "test", eager=true)
@SessionScoped
@RequestScoped
public class Test implements Serializable{
    private static final long serialVersionUID = 7134492943336358840L;
    public int numberQuestion;
    String question;
    String[] answers;
  public String message;
  int voc_id;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public String correctAnswer = null;
    public String humanAnswer = null;
    public String vocabularyId = null;

    public String getVocabularyId() {
        return vocabularyId;
    }

    public void setVocabularyId(String vocabularyId) {
        this.vocabularyId = vocabularyId;
    }
  public static List<Test> listTest = null;

    @PostConstruct
    public void init() {
       this.voc_id =  (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("vocab_id");
       // if(listTest == null)
        listTest = createTest();
        
       
    }
     public int CountNumber()
     {
           
        int i=0;
        List<Word> list = VocabluaryDAO.getWordList(this.voc_id);
       return list.size();
        
     }
    
     
    public String getHumanAnswer() {
        return humanAnswer;
    }

    public void setHumanAnswer(String humanAnswer) {
        this.humanAnswer = humanAnswer;
    }
   

    public List<Test> getListTest() {
        return listTest;
    }

    public void setListTest(List<Test> listTest) {
        this.listTest = listTest;
    }
    
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String[] getAnswers() {
        return answers;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
  
    public int getNumberQuestion() {
        return numberQuestion;
    }

    public void setNumberQuestion(int numberQuestion) {
        this.numberQuestion = numberQuestion;
    }  
  public List<Test> getTest()
  {
    
      return listTest; 
  }
  
 /* public static Test getOneTest() throws SQLException, ClassNotFoundException
  {
        List<Test> createTest = createTest();
      return listTest.get(0);
  }*/
   
    public List<Test> createTest()
    {
       // String vocab_id =  (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("vocab_id");
       
        int i=0;
        List<Word> list = VocabluaryDAO.getWordList(this.voc_id);
        
        if(list.size() < 4)
        {
       FacesContext.getCurrentInstance().addMessage("mainForm:success", new FacesMessage("In vocabulary must be more than 3 word!")); // Substitute "action" with real message.
        setMessage("error");  
            return null;
        }
        else {
      setMessage("_"); 
        List<Test> test = new ArrayList<Test>();
        
   
      List<Integer> numbs1 = new ArrayList<Integer>();
      for(int j=0; j<list.size();j++)
      {
          numbs1.add(j);
      }
     List<Integer> numbs = Arrays.asList(0, 1, 2);
     
 
       for(Word w : list)
        {
            Collections.shuffle(numbs);
            Collections.shuffle(numbs1);
            Test t = new Test();
            t.correctAnswer = w.getTranslatre_word();
            t.numberQuestion = i+1;
            t.question = w.getNative_word();
            t.answers = new String[3];
           // String[] answer = new String[3];
            
                    t.answers[numbs.get(0)] = w.getTranslatre_word(); 
                    
                    if(list.get(numbs1.get(0)).getTranslatre_word().equals(w.getTranslatre_word()))
                    {
                        t.answers[numbs.get(1)] = list.get(numbs1.get(2)).getTranslatre_word();
                    }
                    else
                    {
                        t.answers[numbs.get(1)] = list.get(numbs1.get(0)).getTranslatre_word();
                    }
                    
                    if(list.get(numbs1.get(1)).getTranslatre_word().equals(w.getTranslatre_word()))
                    {
                        t.answers[numbs.get(2)] = list.get(numbs1.get(3)).getTranslatre_word();
                    }
                    else
                    {            
                        t.answers[numbs.get(2)] = list.get(numbs1.get(1)).getTranslatre_word();
                    }
                    test.add(t);
          //  answers.add(answer);
            i++;
            
        }
     // listTest = test;
       return test;
        }
        
        
       // list.get(1).getTranslatre_word();
    }
    
    public static Integer[] distinctRandoms(int nb, int max) {
    ArrayList<Integer> all = new ArrayList<Integer>(max);
    for (int i = 0; i < max; i++) {
        all.add(i);
    }
    if (max <= nb) {
        return all.toArray(new Integer[max]);
    }
    Collections.shuffle(all);
    return all.subList(0, nb).toArray(new Integer[nb]);
}
    public int getResult()
    {
        int count=0;
        for(Test item: listTest)
        {
            if(item.correctAnswer.equals(item.humanAnswer))
            {
                count++;
            }
        }
    return count;
    }
/*   public String checkRadioValue() 
   { return "success"; } */
    
   
}
