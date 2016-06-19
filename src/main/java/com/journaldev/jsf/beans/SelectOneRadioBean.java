/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journaldev.jsf.beans;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author root
 */
@ManagedBean
@SessionScoped
public class SelectOneRadioBean {
    private List<Test> tutorials = new ArrayList<Test>();

	private String selectedTutorial = new String();

	public SelectOneRadioBean() throws SQLException, ClassNotFoundException {
		//this.tutorials= Test.createTest();
	}

	public String register() {
		return "result";
	}

	public List<Test> getTests() {
		return tutorials;
	}

	public void setTutorials(List<Test> tutorials) {
		this.tutorials = tutorials;
	}

	public String getSelectedTutorial() {
		return selectedTutorial;
	}

	public void setSelectedTutorial(String selectedTutorial) {
		this.selectedTutorial = selectedTutorial;
	}
}
