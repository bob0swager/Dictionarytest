package com.journaldev.jsf.dao;

import com.journaldev.jsf.beans.SessionBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.journaldev.jsf.util.DataConnect;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.servlet.http.HttpSession;

public class LoginDAO {
static String securePassword = null;
	public static int validateLogin(String email, String password) throws ClassNotFoundException {
		Connection con = null;
		PreparedStatement ps = null;
               Connection connection;

         
            securePassword = MD5(password);
		try {
			connection = getConnection();
			ps = connection.prepareStatement("SELECT USER_ID, USER_NAME from USERS where EMAIL = ? and PASSWORD = ?");
			ps.setString(1, email);
			ps.setString(2, securePassword);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
                            int user_id = rs.getInt("USER_ID");
                            String user_name = rs.getString("USER_NAME");
                            HttpSession session = SessionBean.getSession();
                            session.setAttribute("username", user_name);
				return user_id;
			}
		} catch (SQLException ex) {
			System.out.println("Login error -->" + ex.getMessage());
                        
			return 0;
		} finally {
			DataConnect.close(con);
		}
		return 0;
	}
        public static String MD5(String md5) {
   try {
        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
        byte[] array = md.digest(md5.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
          sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
       }
        return sb.toString();
    } catch (java.security.NoSuchAlgorithmException e) {
    }
    return null;
}
         private static String getSecurePassword(String passwordToHash)
    {
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            
            //Get the hash's bytes 
            byte[] bytes = md.digest(passwordToHash.getBytes());
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        } 
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }
     
        
        private static Connection getConnection() throws SQLException, ClassNotFoundException {
        Connection con = DataConnect.getInstance().getConnection();
        return con;
    }
}