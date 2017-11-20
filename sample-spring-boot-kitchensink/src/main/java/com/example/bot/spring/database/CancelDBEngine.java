package com.example.bot.spring.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 * this is the DBEngine that handle the cancel request due to not enough people that register the tours
 * @author jsongaf
 *
 */
public class CancelDBEngine extends DBEngine {
	private ConfirmDBEngine CDB;
	/**
	 * function constructor
	 */
	public CancelDBEngine(){
		CDB=new ConfirmDBEngine();
	}
	/**
	 * get the confirmed tours ID
	 * @return tour information list
	 */
	public List<String> getAllUnconfirmedTours(){
		return CDB.getAllUnconfirmedTours(false);
	}
	/**
	 * get the users' information for the book table
	 * @param booktableid book table id in the database
	 * @return all the users that book this tour
	 */
	public Set<String> getAllContactors(String booktableid){
		boolean paid = true;
		return CDB.getAllContactors(booktableid, paid);
	}
	/**
	 * update the database about the canceled tours
	 * @param booktableid book id in the database
	 * @throws Exception if database connection is failed
	 */
	public void updateCanceledTours(String booktableid) throws Exception{	
		PreparedStatement stmt;	
		Connection connection;
		String statement = "UPDATE booking_table "
				+ "SET confirmed = 'canceled' "
				+ "WHERE bootableid = ?";
		try {
			connection=getConnection();
			stmt = connection.prepareStatement(statement);			
			stmt.setString(1,booktableid);
			stmt.executeUpdate();
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
