package com.example.bot.spring.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class ConfirmDBEngine extends DBEngine {

	// functions for confirmation 
	// return all tour whose tourist number > min && not yet been confirmed; 
	/**
	 * get the confirmed tours ID
	 * @param fullfilled
	 * @return
	 */
	public List<String> getAllUnconfirmedTours(boolean fullfilled){
		List<String> unconfirmed_tours = new ArrayList<String>();
		PreparedStatement nstmt = null;
		
		this.openConnection();
		// if fullfillled, retrieve all tour which can be confirmed
		// else, retrieve all tour which should be cancelled; 
		String statement = "";
		if (fullfilled) {
			statement = "SELECT bootableid FROM booking_table "
					+ "WHERE paidnum >= mintourist AND confirmed = 'unconfirmed' ";
			// use paidnum rather than registernum; 			
		}else {
			statement = "SELECT bootableid FROM booking_table "
					+ "WHERE paidnum < mintourist AND confirmed = 'unconfirmed' ";
		}

		try {
			nstmt = connection.prepareStatement(statement);
			ResultSet rs = this.query(nstmt);
			
			while(rs.next()) {
				unconfirmed_tours.add(rs.getString(1));
			}
			nstmt.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		this.close();	
		
		return unconfirmed_tours;
	}

	/**
	 * function to get contacters information
	 * if paid == true: return all contactors who have paid (any amount of), it is for cancelers
	 * if paid == false: return all contactors, whatever they have paid or not, it is for confirmers
	 * @param booktableid
	 * @param paid
	 * @return
	 */
	public Set<String> getAllContactors(String booktableid, boolean paid){
		Set<String> customers = new HashSet<String>();
		PreparedStatement nstmt = null;	
		this.openConnection();
		
		String statement = "";
		if (paid) {
			statement = "SELECT L.userid "
					+ "FROM customer_info as C, line_user_info as L "
					+ "WHERE C.paidamount > 0 AND C.customername = L.name AND C.bootableid = ?";
		}else {
			statement = "SELECT L.userid "
					+ "FROM customer_info as C, line_user_info as L "
					+ "WHERE C.customername = L.name AND C.bootableid = ?";			
		}

		// only announce the tourist who paid the full tour fee; 
		try {
			nstmt = connection.prepareStatement(statement);			
			nstmt.setString(1,booktableid);			
			ResultSet rs = this.query(nstmt);	
			
			while(rs.next()) {
				customers.add(rs.getString(1));
			}
			nstmt.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.close();	
		
		return customers;
	}
	/**
	 * update the database information to mark it as confirmed
	 * @param booktableid
	 */
	public void updateConfirmedTours(String booktableid){	
		PreparedStatement nstmt = null;	
		this.openConnection();		
		String statement = "UPDATE booking_table "
				+ "SET confirmed = 'confirmed' "
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
		this.close();	

	}
}
