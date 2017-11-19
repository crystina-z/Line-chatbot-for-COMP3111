package com.example.bot.spring.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
/**
 * this is to handle the simple questions from the users like greetings
 * @author jsongaf
 *
 */
public class SQDBEngine extends DBEngine {
	private String tname1; // name of table in charge; 
	private String column1_2;
	/**
	 * class constructor
	 */
	public SQDBEngine() {
		this.tname1 = "sq_table"; // name of table in charge; 
		this.column1_2 = "label";
	}
	/**
	 * search in the database to match the sentence and get the key words
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public String search(String text) throws Exception {
		//Write your code here
		String reply = "";
		text = text.toLowerCase();
		
		Connection connection = null;
		String statement = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			connection = super.getConnection();	
		
			statement = "SELECT " + column1_2 + " FROM "+ tname1 + " WHERE \'" + text + "\' SIMILAR TO "
					+ "concat('(', keywords, ')|(', keywords, '[^\\d\\w](%)*)|((%)*[^\\d\\w](' ,keywords, '))|((%)*[^\\d\\w](', keywords, ')[^\\d\\w](%)*)')";
			
			//statement = "SELECT " + column1_2 + " FROM "+ tname1 + " WHERE \'" + text + "\' SIMILAR TO '" + pattern + "'";
			//statement = "SELECT " + column1_2 + " FROM "+ tname1 + " WHERE \'" + text + "\' SIMILAR TO "
			//		+ "concat(keywords, '|(', keywords, '[^\\d\\w])|([^\\d\\w]' ,keywords, '|([^\\d\\w](', keywords, ')[^\\d\\w])')";
			
			//stmt.setString(1, pattern);	
			//statement = "SELECT " + column1_2 + " FROM "+ tname1 + " WHERE \'" + text + "\' LIKE keywords;";
						
			//statement = "SELECT " + column1_2 + " FROM "+ tname1 + " WHERE \'" + text + "\' LIKE concat('%', keywords, '%')";
			
			stmt = connection.prepareStatement(statement);
			//System.out.println("statement: " + statement);			
			rs = stmt.executeQuery();			
			
			if (rs.next()) {				
				reply = rs.getString(1);
			}
			
			//System.out.println(reply);
		}finally {	
			//rs.close();
			//stmt.close();
			//connection.close(); 
			 rs.close();
			 stmt.close();
			 connection.close();
		}
		System.out.print(reply);
		if(!reply.equals("") ) {
			System.out.print(reply);
			return reply;
		}else {
			return "";	
		}
		
	}
}
