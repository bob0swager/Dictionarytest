/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journaldev.jsf.dao;

import com.journaldev.jsf.beans.Vocabulary;
import com.journaldev.jsf.beans.Word;
import com.journaldev.jsf.util.DataConnect;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.swing.JOptionPane;

/**
 *
 * @author root
 */
public class VocabluaryDAO {
    
      public static boolean addVocabulary(String name, String language, int id){
		Connection con = null;
		PreparedStatement ps = null;
               Connection connection;
               boolean res = false;

		try {
                    
                    try {
                        connection = getConnection();
                    
			ps = connection.prepareStatement("INSERT INTO VOCABULARIES(VOCABULARY_NAME, VOCABlUARY_LANGUAGE, USER_ID) VALUES(?,?,?)");
			ps.setString(1, name);
			ps.setString(2, language);
                        ps.setInt(3, id);

			ps.executeUpdate();
} catch (ClassNotFoundException ex) {
                        Logger.getLogger(VocabluaryDAO.class.getName()).log(Level.SEVERE, null, ex);
                    }
			//JOptionPane.showMessageDialog(null,"Inserted Successfully!");
		} catch (SQLException ex) {
			System.out.println("error -->" + ex.getMessage());           
			
		} finally {
			DataConnect.close(con);
		}
                res = true;
                return res;
		
	}
            
    public static boolean addWord(String word, String translate, String vocabulary_id) throws ClassNotFoundException {
		Connection con = null;
		PreparedStatement ps = null;
               Connection connection;
               boolean res = false;

		try {
                    
			connection = getConnection();
			ps = connection.prepareStatement("INSERT INTO WORDS(NATIVE_WORD, TRANSLATE_WORD) VALUES(?,?)");
			ps.setString(1, word);
			ps.setString(2, translate);
			ps.executeUpdate();
                        
                        ps = null;
                        connection = getConnection();
                        ps = connection.prepareStatement("SELECT MAX(WORD_ID) FROM WORDS");                     
                        ResultSet result = null;
                        int newWordId=0;
                        result = ps.executeQuery();
                        if(result.next())      
                        newWordId  = result.getInt(1);
                        
                        ps = null;
                        connection = getConnection();
                        ps = connection.prepareStatement("INSERT INTO VOCABULARY_AND_WORD(ID_VOCABULARY, ID_WORD) VALUES(?,?)");
                        ps.setInt(1, Integer.parseInt(vocabulary_id));
			ps.setInt(2, newWordId);
                        ps.executeUpdate();
			//JOptionPane.showMessageDialog(null,"Inserted Successfully!");
		} catch (SQLException ex) {
			System.out.println("error -->" + ex.getMessage());           
			
		} finally {
			DataConnect.close(con);
		}
                res = true;
                return res;
		
	}
     public void displayMessage(ActionEvent actionEvent)
    {
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("it worked!"));
    }
     
    public static List<Word> getWordList(int vocabulary_id) {
		
		
		//get database connection
		Connection con = null;
        try {
            con = getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(VocabluaryDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VocabluaryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
		
		if(con==null)
                {}
			
		
		PreparedStatement ps = null;
        try {
            ps = con.prepareStatement("SELECT WORD_ID, NATIVE_WORD, TRANSLATE_WORD FROM WORDS w JOIN VOCABULARY_AND_WORD v ON w.WORD_ID = v.ID_WORD WHERE v.ID_VOCABULARY = ?");
            ps.setInt(1, vocabulary_id);
        } catch (SQLException ex) {
            Logger.getLogger(VocabluaryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
		
		//get customer data from database
		ResultSet result = null;
        try {
            result = ps.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(VocabluaryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
		
		List<Word> list = new ArrayList<Word>();
		int i=0;
        try {
            while(result.next()){
                Word cust = new Word();
                cust.setId_word(result.getInt("WORD_ID"));
                cust.setNative_word(result.getString("NATIVE_WORD"));
                cust.setTranslatre_word(result.getString("TRANSLATE_WORD"));
                i++;
                //store all data into a List
                list.add(cust);	
            }
        } catch (SQLException ex) {
            Logger.getLogger(VocabluaryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
		return list;
	}
    
    static public void deleteVocabulary(String id_vocabulary)
    {
        Connection connection = null;
          try {
              connection = getConnection();
          } catch (SQLException ex) {
              Logger.getLogger(VocabluaryDAO.class.getName()).log(Level.SEVERE, null, ex);
          } catch (ClassNotFoundException ex) {
              Logger.getLogger(VocabluaryDAO.class.getName()).log(Level.SEVERE, null, ex);
          }
        
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("DELETE FROM VOCABULARIES WHERE VOCABULARY_ID = ?");
            ps.setInt(1, Integer.parseInt(id_vocabulary));
            ps.executeUpdate();
            
            ps = null;
            try {
                connection = getConnection();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(VocabluaryDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
                        ps = connection.prepareStatement("DELETE FROM VOCABULARY_AND_WORD WHERE ID_VOCABULARY = ?");
                        ps.setInt(1, Integer.parseInt(id_vocabulary));
                        ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(VocabluaryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       
    }
    
     static public void deleteWord(String id_word)
    {
        Connection connection = null;
          try {
              connection = getConnection();
          } catch (SQLException ex) {
              Logger.getLogger(VocabluaryDAO.class.getName()).log(Level.SEVERE, null, ex);
          } catch (ClassNotFoundException ex) {
              Logger.getLogger(VocabluaryDAO.class.getName()).log(Level.SEVERE, null, ex);
          }
        
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("DELETE FROM WORDS WHERE WORD_ID = ?");
            ps.setInt(1, Integer.parseInt(id_word));
            ps.executeUpdate();
            
            ps = null;
            try {
                connection = getConnection();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(VocabluaryDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
                        ps = connection.prepareStatement("DELETE FROM VOCABULARY_AND_WORD WHERE ID_WORD = ?");
                        ps.setInt(1, Integer.parseInt(id_word));
                        ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(VocabluaryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       
    }
    
    public static List<Vocabulary> getVocabularyList() {
		
		
		//get database connection
		Connection con = null;
        try {
            con = getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(VocabluaryDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VocabluaryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
		
		if(con==null)
                {}
			
		
		PreparedStatement ps = null;
        try {
            ps = con.prepareStatement("select VOCABULARY_ID, VOCABULARY_NAME, VOCABLUARY_LANGUAGE from VOCABULARIES where USER_ID = ?");
            int user_id =  (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id");
            ps.setInt(1, user_id);
        } catch (SQLException ex) {
            Logger.getLogger(VocabluaryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
		
		//get customer data from database
		ResultSet result = null;
        try {
            result = ps.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(VocabluaryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
		
		List<Vocabulary> list = new ArrayList<Vocabulary>();
		
        try {
            while(result.next()){
                Vocabulary cust = new Vocabulary();
                cust.setId_vocabulary(result.getInt("VOCABULARY_ID"));
                cust.setName_vocabulary(result.getString("VOCABULARY_NAME"));
                cust.setLanguage_vocabulary(result.getString("VOCABLUARY_LANGUAGE"));
              
                //store all data into a List
                list.add(cust);	
            }
        } catch (SQLException ex) {
            Logger.getLogger(VocabluaryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
		return list;
	}
        
        private static Connection getConnection() throws SQLException, ClassNotFoundException {
        Connection con = DataConnect.getInstance().getConnection();
        return con;
    }
}
