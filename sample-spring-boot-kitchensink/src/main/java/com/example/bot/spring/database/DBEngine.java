package com.example.bot.spring.database;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
/**
 * super class for all DBEngine
 * @author jsongaf
 *
 */
public class DBEngine {
	
	private static final String CLASSIFYTABLE = "classify_table";
	
	private static final String CUSTOMER = "customer_info";
	private static final String LINEUSER = "line_user_info";
	private static final String PRICE = "tour_price";
	private static final String DESCRIPTION = "tour_description";
	private static final String TOURINFO = "tour_info";
	private static final String BOOKTABLE = "booking_table";
	protected Connection connection = null;
	
	/**
	 * class constructor
	 */
	public DBEngine() {
		
	}
	/**
	 * update the line users' information in the database
	 * @param userID user id
	 * @param entryName the categorization of the user
	 * @param value user input
	 * @throws Exception if the database connection is failed
	 */
	public void updateLineUserInfo(String userID,String entryName,String value) throws Exception{
		Connection connection= getConnection();
		PreparedStatement stmt;
		try {
			getLineUserInfo(userID,entryName);
		}catch(Exception e) {
			if(e.getMessage().equals("No such entry")) {
				stmt=connection.prepareStatement("INSERT INTO line_user_info VALUES ( ? ,'','','','','','')");
				stmt.setString(1,userID);
				stmt.executeUpdate();
				stmt.close();
			}
			else throw new Exception("Wrong Command1");
		}
		try {
			stmt = connection.prepareStatement(
					"UPDATE " + LINEUSER + " SET "+ entryName +" = ? WHERE userid = ?");
			stmt.setString(1, value);
			stmt.setString(2, userID);
			stmt.executeUpdate();
		}catch(Exception e) {
			throw new Exception("Wrong Command2");
		}
		stmt.close();
		connection.close();
	}
	/**
	 * get the line users' information from the database
	 * @param userID user id
	 * @param entryName user status
	 * @return user information
	 * @throws Exception if the database connection is failed
	 */
	public String getLineUserInfo(String userID,String entryName) throws Exception{
		Connection connection= getConnection();
		PreparedStatement stmt;
		ResultSet rs;
		try{
			stmt = connection.prepareStatement(
					"SELECT "+entryName+" FROM line_user_info WHERE userid = ?");
			stmt.setString(1, userID);
			rs=stmt.executeQuery();
		}catch(Exception e){
			System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
			System.out.println("with userid "+userID);
			e.printStackTrace();
			throw new Exception("Wrong Command1");
		}
		if(!rs.next()) { 
			System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
			System.out.println("No such entry!!!!!");
			throw new Exception("No such entry");
		}
		String tmp=rs.getString(1);
		rs.close();
		stmt.close();
		connection.close();
		return tmp;
	}
	
	/**
	 * given a pure text, check the 'classify_table' in database to determine its type
	 * @param text user input
	 * @return user text
	 */
	public String getTextType(String text) {	
		if(text == null || text.equals("")) {
			return "";
		}
		
		Connection connection = null; 
		PreparedStatement stmt  = null;
		ResultSet rs = null;		// string(1): keywords; string(2): position; string(3): label; 		
				
		// classify input according to classify_table: get returned type; 
		// if there r multiple types;
		// check: label(diff with previous label) && position(correct position)  && length of keywords (choose the label to the longer keywords)
	
		String type = "none";	 // type to return, with default value to be "none"
		String selectedKey = ""; // corresponding key of selected type; 
		
		String keywords = "";	 // keyword for cur entry
		String position = "";	 // position for cur entry
		String label = "";		 // label for cur entry
		try{
			connection = getConnection();
			String statement = "SELECT * FROM " +  CLASSIFYTABLE + " WHERE '" + text + "' LIKE concat('%', keywords, '%')";		
			stmt = connection.prepareStatement(statement);
			rs=stmt.executeQuery();

			System.err.println(statement);			
			
			while (rs.next()) {				
				label = rs.getString(3);
				System.err.println("inside rs: lable: " + label + " current type: " + type);
				if(!type.equals(label)) {	// check if rs.label euqals the selectedLabel					
					keywords = rs.getString(1);
					position = rs.getString(2);	
					System.err.println("label : " + label + " keywords: " + keywords + " position: " + position);
					if(!wrongPosition(keywords, text, position)) { // if no: check position;
						// if no: check keywords length:
						// if current keyword is longer: replce selectedKey with currentKey; 
						if(keywords.length() > selectedKey.length()) {	
							System.err.println("replacing : " + type + " with " + label + " 'cause length");
							type = label;
							selectedKey = keywords;
							
							// reset keywords,  position, label
							keywords = "";
							position = "";
							label = "";
													
						}else if(label.length() == type.length()) { // if same, shuffle		
							// no action for now; 
						}
					}
				}				
			}
		}catch(Exception e){
			System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
			System.out.println("-- inside DBENGINE: getTextType --");
			e.printStackTrace();
		}finally {
			try {
				rs.close();
				stmt.close();
				connection.close();
			}catch (Exception e2) {
				System.err.println(e2.getMessage());
			}
		}
		return type;		
	}
	/**
	 * check if the position of the string is wrong
	 * @param key user status
	 * @param message user input
	 * @param position the position of the key word
	 * @return correct or not
	 */
	private boolean wrongPosition(String key, String message, String position) {
		boolean positionWrong = true;
		
		switch (position) {
		case "front":
			if(key.equals(message.substring(0, key.length())))
				positionWrong = false;
			break;
		case "end":
	        int lenMsg = message.length();
	        int lenKey = key.length();
	        int startIndex = lenMsg - lenKey;
	        if(key.equals(message.substring(startIndex)))
	        	positionWrong = false;
	        break; 
		case "any":
			positionWrong = false;
			break;
		default: 
			// no action; 
		}
		return positionWrong;
	}
	
	/**
	 * get a connection
	 */
	public void openConnection() {
		try {
			connection = this.getConnection();
		} catch (URISyntaxException | SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * close a connection
	 */
	public void close() {
		try {
			connection.close();
			connection = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * execute the conntion statement
	 * @param nstmt statement
	 * @return result set of the statement
	 */
	protected ResultSet query(PreparedStatement nstmt) {
		ResultSet rs = null;
		try {
			rs = nstmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	/**
	 * update the database
	 * @param nstmt statement
	 */
	protected void update(PreparedStatement nstmt) {
		try {
			nstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * execute the query
	 * @param nstmt statement
	 */
	protected void execute(PreparedStatement nstmt) {
		try {
			nstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * get a new connection to the database
	 * @return new connection
	 * @throws URISyntaxException if the syntax is wrong
	 * @throws SQLException is the sql is catched an exception
	 */
	protected Connection getConnection() throws URISyntaxException, SQLException {
		Connection connection;
		URI dbUri = new URI(System.getenv("DATABASE_URL"));

		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() +  "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

		//log.info("Username: {} Password: {}", username, password);
		//log.info ("dbUrl: {}", dbUrl);
		
		connection = DriverManager.getConnection(dbUrl, username, password);

		return connection;
	}
}
