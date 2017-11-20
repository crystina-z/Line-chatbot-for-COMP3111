package com.example.bot.spring.textsender;

import com.example.bot.spring.database.GQDBEngine;
/**
 * general questions handler which could ask questions like how long is the trip and when the trip will begin
 * @author jsongaf
 *
 */
public class GQTextSender implements TextSender {
	
	private GQDBEngine DBE;
	/**
	 * class constructor
	 */
	public GQTextSender() {
		DBE= new GQDBEngine();
	}
	/**
	 * process the input from the user and then answer the questions accordingly
	 * @param userID user id
	 * @param msg user input
	 * @return the result after executing the query
	 * @throws Exception if database conenction is failed
	 */
	@Override
	public String process(String userID, String msg) throws Exception{
			String TourID=DBE.getTourID(userID,msg);
			DBE.update(userID,TourID);
			String reply =DBE.query(userID,msg,TourID);
			if(reply.equals("")) {
				throw new Exception("No reply");
			}
			return reply;
	}
}
