/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journaldev.jsf.beans;

import com.journaldev.jsf.util.DataConnect;
import java.io.IOException;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.sql.Connection;
import javax.activation.DataSource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.NoneScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.http.HttpSession;

/**
 *
 * @author root
 */
@ManagedBean(name = "user")
@ViewScoped
public class User implements Serializable {

    private String name;
    private String email;
    private String password;
    private String dbPassword;
    private String dbName;
    public String resultAdding;

    public String getResultAdding() {
        return resultAdding;
    }

    public void setResultAdding(String resultAdding) {
        this.resultAdding = resultAdding;
    }
    //  DataSource ds;
    Connection connection;

    public User() {
        /* try {
            Context ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("java:comp/env/jdbc/database");
        } catch (NamingException e) {
            e.printStackTrace();
        }*/
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public String getDbName() {
        return dbName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void validatePassword(ComponentSystemEvent event) {

	  FacesContext fc = FacesContext.getCurrentInstance();

	  UIComponent components = event.getComponent();

	  // get password
	  UIInput uiInputPassword = (UIInput) components.findComponent("psw");
	  String password = uiInputPassword.getLocalValue() == null ? ""
		: uiInputPassword.getLocalValue().toString();
	  String passwordId = uiInputPassword.getClientId();

	  // get confirm password
	  UIInput uiInputConfirmPassword = (UIInput) components.findComponent("confirmPassword");
	  String confirmPassword = uiInputConfirmPassword.getLocalValue() == null ? ""
		: uiInputConfirmPassword.getLocalValue().toString();

	  // Let required="true" do its job.
	  if (password.isEmpty() || confirmPassword.isEmpty()) {
		return;
	  }

	  if (!password.equals(confirmPassword)) {

		FacesMessage msg = new FacesMessage("Password must match confirm password");
		msg.setSeverity(FacesMessage.SEVERITY_ERROR);
		fc.addMessage(passwordId, msg);
		fc.renderResponse();
			
	  }

	}
    
    /*public String add() {
        int i = 0;
        if (name != null) {
            PreparedStatement ps = null;
            Connection con = null;
            try {
                
                    con = getConnection();
                    if (con != null) {
                        String sql = "INSERT INTO USERDB(NAME, PASSWORD) VALUES(?,?)";
                        ps = con.prepareStatement(sql);
                        ps.setString(1, name);
                        ps.setString(2, password);
                        
                        i = ps.executeUpdate();
                        System.out.println("Data Added Successfully");
                    
                }
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                try {
                    con.close();
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (i > 0) {
            return "success";
        } else
            return "unsuccess";
    }*/
    public String add() {
        int i = 0;
        if (name != null) {
            PreparedStatement ps = null;
            Connection con = null;
            boolean isEmailValid = checkValidEmail(email);
            if (isEmailValid) {

                String securePassword = MD5(password);

                try {

                    con = getConnection();
                    if (con != null) {
                        String sql = "INSERT INTO USERS(USER_NAME, EMAIL, PASSWORD) VALUES(?,?,?)";
                        ps = con.prepareStatement(sql);
                        ps.setString(1, name);
                        ps.setString(2, email);
                        ps.setString(3, securePassword);

                        i = ps.executeUpdate();
                        System.out.println("Data Added Successfully");

                    }
                } catch (Exception e) {
                    System.out.println("----------------------------------------------------" + e);
                } finally {
                    try {
                        con.close();
                        ps.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (i > 0) {
                /*  setResultAdding("yes");
            return resultAdding;*/
                FacesContext.getCurrentInstance().addMessage("registerForm:success", new FacesMessage("Successful registration! Thanks.")); // Substitute "action" with real message.
                FacesContext fContext = FacesContext.getCurrentInstance();
                ExternalContext extContext = fContext.getExternalContext();

                try {
                    extContext.redirect(extContext.getRequestContextPath() + "/login.xhtml#contact");
                } catch (IOException ex) {
                    Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
                }
                return "success";
            } else {
                /*setResultAdding("no");
        return resultAdding;*/
                FacesContext.getCurrentInstance().addMessage("registerForm:unsuccess", new FacesMessage("Repeat Please")); // Substitute "action" with real message.
                return null;

            }
        } else { 
            return null;
        }
    }

    public String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    private boolean checkValidEmail(String email) {
        Connection connection = null;

        int user_id = 0;
        PreparedStatement ps = null;
        try {

            connection = getConnection();
            ps = connection.prepareStatement("SELECT USER_ID from USERS where EMAIL = ?");
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user_id = rs.getInt("USER_ID");


            }
        } catch (SQLException ex) {
            System.out.println("Login error -->" + ex.getMessage());

            return false;
        } finally {
            DataConnect.close(connection);
        }
        if (user_id == 0) {
            return true;
        } else {
            return false;
        }
    }

    private static String getSecurePassword(String passwordToHash) {
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            //Get the hash's bytes 
            byte[] bytes = md.digest(passwordToHash.getBytes());
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    //Add salt
    private static byte[] getSalt() {
        byte[] salt = null;
        try {
            //Always use a SecureRandom generator
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
            //Create array for salt
            salt = new byte[16];
            //Get a random salt
            sr.nextBytes(salt);
            //return salt

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return salt;
    }

    private static Connection getConnection() throws SQLException {
        Connection con = null;
        try {
            con = DataConnect.getInstance().getConnection();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return con;
    }

    public void dbData(String email) {
        if (email != null) {
            PreparedStatement ps = null;
            Connection con = null;
            ResultSet rs = null;

            try {
                con = getConnection();
                if (con != null) {
                    String sql = "select EMAIL, PASSWORD from USERS where EMAIL = '"
                            + email + "'";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    rs.next();
                    dbName = rs.getString("EMAIL");
                    dbPassword = rs.getString("PASSWORD");
                }
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }

        }
    }

    public String login() {
        dbData(name);
        if (name.equals(dbName) && password.equals(dbPassword)) {
            return "main_page";
        } else {
            return "invalid";
        }
    }

    public void logout() {
        FacesContext.getCurrentInstance().getExternalContext()
                .invalidateSession();
        FacesContext.getCurrentInstance()
                .getApplication().getNavigationHandler()
                .handleNavigation(FacesContext.getCurrentInstance(), null, "/login.xhtml");
    }
}
