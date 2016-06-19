package com.journaldev.jsf.beans;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.journaldev.jsf.dao.LoginDAO;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class Login implements Serializable {

	private static final long serialVersionUID = 1094801825228386363L;
	
	private String pwd;
	private String msg;
	private String user;

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	//validate login
	public String validateUsernamePassword() throws ClassNotFoundException {
		int valid = LoginDAO.validateLogin(user, pwd);
		if (valid>0) {
			HttpSession session = SessionBean.getSession();
			//session.setAttribute("username", user);
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user_id", valid);
			return "main_page";
		} else {
			/*FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN,
							"Incorrect Username and Passowrd",
							"Please enter correct username and Password"));
			
*/
                        // invalid
            FacesContext.getCurrentInstance().addMessage("formLogin:newPassword2Error", new FacesMessage("Incorrect Username and Passowrd"));
                        return null;
		}
	}
        public String register()
        {
            return "success";
        }
       

	//logout event, invalidate session
	public void logout() {
		HttpSession session = SessionBean.getSession();
		session.invalidate();
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
}
