package com.example.bot.spring.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * this is to handle the unanswered questions, save them into the database and reply with a default answer
 * @author jsongaf
 */
//I am doing a very stupid test, please ignore this pull request
public class UQDBEngine extends DBEngine {
	/**
	 * class constructor
	 */
	public UQDBEngine() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * update the database table and save the question and change the status if someone answer the question
	 * @throws Exception if database connection fails
	 */
	public void updateTable() throws Exception{
		Connection connection = null;
		PreparedStatement stmt = null;

		connection=getConnection();
		stmt = connection.prepareStatement(
				"update unanswered_question set sent_or_not = true where answered_or_not = true"
		);
		stmt.executeUpdate();
		
		if (stmt != null) stmt.close();
		if (connection != null) connection.close();
	}
	/**
	 * get the default reply from the database
	 * @return reply to the userse
	 * @throws Exception if database connection fails
	 */
	public String retrieveReply() throws Exception {
		Connection connection = null;
		PreparedStatement stmt = null;
		String reply="";

		connection=getConnection();
		stmt = connection.prepareStatement("select * from unanswered_default_reply");
		ResultSet rs=stmt.executeQuery();
		while (rs.next()) {
			reply=rs.getString(1);
		}
		
		if (stmt != null) stmt.close();
		if (connection != null) connection.close();

		return reply;
	}
	/**
	 * get the answer from the database to send it to users
	 * @return get the answer list
	 * @throws Exception if databse connection fails
	 */
	public ArrayList<String> answer() throws Exception{
		Connection connection = null;
		PreparedStatement stmt = null;
		ArrayList<String> reply= new ArrayList<String>();

		connection=getConnection();
		stmt = connection.prepareStatement(
				"select * from unanswered_question where answered_or_not = true and sent_or_not= false"
		);
		ResultSet rs=stmt.executeQuery();
		while (rs.next()) {
			String temp="";
			temp+=rs.getString(1)+","+rs.getString(2)+","+rs.getString(4);
			reply.add(temp);
		}
		
		if (stmt != null) stmt.close();
		if (connection != null) connection.close();
		
		updateTable();

		return reply;
	}
	/**
	 * execute the query and connect to the database
	 * @param userId user id
	 * @param text user input
	 * @return result of the qurey
	 * @throws Exception if database connection is failed
	 */
	public String uqQuery(String userId, String text) throws Exception{
		//System.out.println("Success");
		Connection connection = null;
		PreparedStatement stmt = null;

		connection = getConnection();
		//insert into the unanswered question table to store the question
		stmt = connection.prepareStatement(
				"insert into unanswered_question values( '"+userId+"', '"+text+"', false, '', false)"
		);
		stmt.executeUpdate();

		if (stmt != null) stmt.close();
		if (connection != null) connection.close();

		return retrieveReply();
	}
}
