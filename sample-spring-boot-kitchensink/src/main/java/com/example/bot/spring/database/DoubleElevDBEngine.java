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
	
	// functions for confirmation 
	// return all tour whose tourist number > min && not yet been confirmed; 
	String discount_tours =  "";
    
	private Connection connection;
    
	/**
	 * class constructor
	 */
	public DoubleElevDBEngine() {
		connection = null;
	}
	
	
	// functions for confirmation 
	// return all tour whose tourist number > min && not yet been confirmed; 
	/**
	 * get the discount book ID to identify whether the users are qulified for a discount or not
	 * @return
	 */
	public String getDiscountBookid(String s){ // only one tour is allowed to be discounted at the same time
		String discount_tours =  null;
		PreparedStatement nstmt = null;
		
		this.openConnection();
		String statement = "SELECT bootableid FROM double11 "
				+ "WHERE status = ? ";
		// choose the tours that haven't been broadcasted;  
		try {
			nstmt = connection.prepareStatement(statement);
			nstmt.setString(1, s);
			ResultSet rs = this.query(nstmt);
			
			if(rs.next()) {
				discount_tours = rs.getString(1);
			}
			nstmt.close();
			rs.close();
		} catch (SQLException e) {
			this.close();
			e.printStackTrace();
		}		
		this.close();
		
		return discount_tours;
	}
	/**
	 * get the information of all line users
	 * @return
	 */
	public Set<String> getAllClient(){
		Set<String> clients = new HashSet<String>();
		PreparedStatement nstmt = null;	
		this.openConnection();
		
		String statement = "SELECT userid FROM line_user_info"
						 + " WHERE categorization <> 'book'";

		try {
			nstmt = connection.prepareStatement(statement);			
			ResultSet rs = this.query(nstmt);
			
			while(rs.next()) {
				clients.add(rs.getString(1));
			}
			nstmt.close();
			rs.close();
		} catch (SQLException e) {
			this.close();
			e.printStackTrace();
		}
		this.close();	
		
		return clients;
	}
	/**
	 * change the database table to indicate that the information has been braodcasted
	 * @param booktableid
	 */
	public void updateBroadcastedTours(String booktableid){	
		PreparedStatement nstmt = null;	
		this.openConnection();		
		String statement = "UPDATE double11 "
				+ "SET status = 'sent' "
				+ "WHERE bootableid = ?";
		try {
			nstmt = connection.prepareStatement(statement);			
			nstmt.setString(1,booktableid);		
			
			this.update(nstmt);
			
			nstmt.close();
		} catch (SQLException e) {
			this.close();
			e.printStackTrace();
		}
		
		this.close();
	}
	
	public void updateActivityStatus(){	
		PreparedStatement nstmt = null;	
		this.openConnection();		
		String statement = "UPDATE double11 "
				+ "SET status = 'outdate' "
				+ "WHERE status <> 'released'";
		try {
			nstmt = connection.prepareStatement(statement);				
			
			this.update(nstmt);
			
			nstmt.close();
		} catch (SQLException e) {
			this.close();
			e.printStackTrace();
		}
		
		this.close();
	}
	
	public boolean ifTourFull(String booktableid) {
		PreparedStatement nstm = null;
		int remaining_seat = 0; 
		
		openConnection();
		String statement = "SELECT remaining_seat FROM double11 WHERE bootableid = ? ";
		
		try {
			nstm = connection.prepareStatement(statement);
			nstm.setString(1, booktableid);
			
			ResultSet rs = this.query(nstm);			
			if(rs.next()) {
				remaining_seat = rs.getInt(1);
			}
			nstm.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		close();
		
		if(remaining_seat > 0) {return true; }
		else return false; 		
	}

	public void updateQuota(String discount_tourid){
		PreparedStatement nstmt = null;	
		this.openConnection();		
		String statement = "UPDATE double11 "
				+ "SET remaining_seat = remaining_seat - 1 "
				+ "WHERE bootableid = ?";
		try {
			nstmt = connection.prepareStatement(statement);	
			nstmt.setString(1, discount_tourid);
			this.update(nstmt);
			
			nstmt.close();
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally {
			this.close();
		}
	}
}
