package com.example.bot.spring.database;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
/**
 * added features that the agency could give special discount on special events with limited quota
 * @author jsongaf
 *
 */
public class DoubleElevDBEngine extends DBEngine {
	private Connection connection;
	/**
	 * class constructor
	 */
	public DoubleElevDBEngine() {
		connection = null;
	}
	/**
	 * get a new connection to the database
	 */
	public void openConnection() {
		try {
			connection = this.getConnection();
		} catch (URISyntaxException | SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * close the existing database
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
	 * execute the query statement
	 * @param nstmt
	 * @return
	 */
	private ResultSet query(PreparedStatement nstmt) {
		ResultSet rs = null;
		try {
			rs = nstmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	// functions for confirmation 
	// return all tour whose tourist number > min && not yet been confirmed; 
	/**
	 * get the discount book ID to identify whether the users are qulified for a discount or not
	 * @return
	 */
	public String getDiscountBookid(){ // only one tour is allowed to be discounted at the same time
		String discount_tours =  null;
		PreparedStatement nstmt = null;
		
		openConnection();
		String statement = "SELECT bootableid FROM double11 "
				+ "WHERE status = 'released' ";
		// choose the tours that haven't been broadcasted;  
		try {
			nstmt = connection.prepareStatement(statement);
			ResultSet rs = this.query(nstmt);
			
			if(rs.next()) {
				discount_tours = rs.getString(1);
			}
			nstmt.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		close();
		
		return discount_tours;
	}
	/**
	 * get the information of all line users
	 * @return
	 */
	public Set<String> getAllClient(){
		Set<String> clients = new HashSet<String>();
		PreparedStatement nstmt = null;	
		openConnection();
		
		String statement = "SELECT userid FROM line_user_info"
						 + " WHERE categorization = 'book'";

		try {
			nstmt = connection.prepareStatement(statement);			
			ResultSet rs = this.query(nstmt);
			
			while(rs.next()) {
				clients.add(rs.getString(1));
			}
			nstmt.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		close();	
		
		return clients;
	}
	/**
	 * change the database table to indicate that the information has been braodcasted
	 * @param booktableid
	 */
	public void updateBroadcastedTours(String booktableid){	
		PreparedStatement nstmt = null;	
		openConnection();		
		String statement = "UPDATE TABLE double11 "
				+ "SET status = 'sent' "
				+ "WHERE bootableid = ?";
		try {
			nstmt = connection.prepareStatement(statement);			
			nstmt.setString(1,booktableid);		
			
			ResultSet rs = this.query(nstmt);
			
			nstmt.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		close();
	}
}
