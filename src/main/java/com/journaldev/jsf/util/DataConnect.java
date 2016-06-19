package com.journaldev.jsf.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataConnect {

/*	public static Connection getConnection() {
		try {
			Class.forName("org.apache.derby.jdbc.ClientDriver");
			Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/Users", "app", "app");
			return con;
		} catch (Exception ex) {
			System.out.println("Database.getConnection() Error -->"
					+ ex.getMessage());
			return null;
		}
	}*/

	
        
        
        private static DataConnect instance = new DataConnect();
	String url = "jdbc:derby://localhost:1527/Users";
	String user = "app";
	String password = "app";
      //The JDBC driver manager ensures that the correct driver is used to access each data source.
        //Driver: This interface handles the communications with the database server
	String driverClass = "org.apache.derby.jdbc.ClientDriver";  //Java Database Connectivity  standard Java API for database-independent connectivity
	
	//private constructor
        //Java DataBase Connectivity
	private DataConnect() {
		try {
                    //obtains a reference to the class object with the FQCN (fully qualified class name) 
                    //load a driver into memory
                    //The Class.forName statement is making sure that the class that implements the JDBC
                    //driver for sqlite3 is loaded and registered with the JDBC factory mechanism.
                    //Load the JDBC driver.
			Class.forName(driverClass);
		} 
                catch (ClassNotFoundException e) {
                    System.out.println(e.getMessage());
		}
	}
	
	public static DataConnect getInstance(){
		return instance;
	}
	
	public Connection getConnection() throws SQLException, 
	ClassNotFoundException {
		Connection connection = DriverManager.getConnection(url, user, password);
		return connection;
	}
        public static void close(Connection con) {
		try {
			con.close();
		} catch (Exception ex) {
		}
	}
}