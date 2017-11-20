package com.example.bot.spring.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

//import com.linecorp.bot.model.event.message.TextMessageContent;
/**
 * this class is to recommend some trips with specific features to the users
 * @author jsongaf
 *
 */
public class RecommendationDBEngine extends DBEngine {
	/**
	 * class constructor
	 */
	public RecommendationDBEngine() {
		
	}
	/**
	 * get the features of the tours
	 * @param msg user input
	 * @return return the features list
	 */
	public ArrayList<String> getFeatures(String msg) {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs=null;
		ArrayList<String> result=new ArrayList<String>();
		try {
			connection=getConnection();
			stmt=connection.prepareStatement(
					"select * from key_features"
			);
			rs=stmt.executeQuery();
			
			while(rs.next()) {
				if (msg.contains(rs.getString(1))) {
					result.add(rs.getString(1));
				}
			}
			
			stmt.close();
			connection.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	/**
	 * execute the query and update the line user information
	 * @param tourids tour id
	 * @param userid user id
	 */
	public void updateQuery(String tourids, String userid) {
		Connection connection = null;
		PreparedStatement stmt = null;

		try {
			connection=getConnection();
			stmt=connection.prepareStatement(
					"update line_user_info set tourids ='"+tourids+"' where userid='"+userid+"'"
			);
			stmt.executeUpdate();
			stmt.close();
			connection.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * get the recommendation tours and sentence from the database
	 * @param userId user id
	 * @param text user input
	 * @return recommendation of the tours
	 */
	public String recommendationQuery(String userId, ArrayList<String> text) {

		String response="";
		String idList="";
		
		Connection connection = null;
		PreparedStatement stmt = null;
		String temp;
		ResultSet rs = null;

		try {
			connection = getConnection();
			
			for(int i=0; i<text.size(); i++) {
				//System.out.println(text.get(i));
				stmt = connection.prepareStatement(
					"select * from tour_features join tour_info on tour_features.tourid=tour_info.tourid where tour_features."+text.get(i)+"=1", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			
				rs=stmt.executeQuery();
				
				if (!rs.next()) {
					response+="No tours with good "+text.get(i)+"\n";
				}
				else {
					rs.beforeFirst();
					temp="Tours with good "+text.get(i)+" : ";
					while (rs.next()) {
						String tourid = rs.getString(1);
						temp+=tourid+": ";
						temp+=rs.getString(7)+"\n";
						idList+=tourid+", ";
					} 
					temp=temp.replaceAll(", $", "");
					response+=temp+"\n";
					rs.close();
				}

				//stmt.close();
			}
			
			if (!response.equals("")){
				response=response.replaceAll("\n$", ".");
			}
			
			if (idList!="") {
				idList=idList.replaceAll(", $", "");
			}
			
			stmt.close();
			connection.close();

			updateQuery(idList, userId);
			
		}catch(Exception e) {
			e.printStackTrace();
		}

		return response;
	}
}
