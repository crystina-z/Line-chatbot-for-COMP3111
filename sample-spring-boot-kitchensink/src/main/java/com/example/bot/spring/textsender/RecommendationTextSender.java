package com.example.bot.spring.textsender;

import java.sql.ResultSet;
import java.util.ArrayList;
import com.example.bot.spring.database.*;
/**
 * handle the recommendation from the user and recommend some trips according to the features
 * @author jsongaf
 *
 */
public class RecommendationTextSender implements TextSender {
	/**
	 * class construtor 
	 */
	public RecommendationTextSender() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * process the user input and get the features of the trip
	 * @param userId user id
	 * @param msg user input
	 * @return result after the execution of the query
	 * @throws Exception if database connection is failed
	 */
	@Override
	public String process(String userId, String msg) throws Exception {
		// TODO Auto-generated method stub
		RecommendationDBEngine searchEngine = new RecommendationDBEngine();
		//assume the features are 1.hotel, 2.spring, 3.view 4. food
		ArrayList<String> featureList = new ArrayList<String>();
		
		//get the features
		featureList=searchEngine.getFeatures(msg);
		
//		if (msg.contains("hotel")){
//			featureList.add("hotel");
//		}
//		if (msg.contains("spring")){
//			featureList.add("spring");
//		}
//		if (msg.contains("view")||msg.contains("sight")){
//			featureList.add("view");
//		}
//		if (msg.contains("food")){
//			featureList.add("food");
//		}
		
		String reply = searchEngine.recommendationQuery(userId, featureList);
		
		if(reply.equals(null)||reply.equals("")) {
			throw new Exception("No matching");
			// when featureList is empty -> reply = "";
		}
		else
			return reply;
	}
	
}
