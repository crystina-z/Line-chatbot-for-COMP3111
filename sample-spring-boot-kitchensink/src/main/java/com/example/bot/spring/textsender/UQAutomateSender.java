package com.example.bot.spring.textsender;

import com.example.bot.spring.database.UQDBEngine;
/**
 * handle the unanswered questions
 * @author jsongaf
 *
 */
public class UQAutomateSender implements TextSender {
	/**
	 * class constructor
	 */
	public UQAutomateSender() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * process the user input and save it in the database if it is an unanswered question
	 * @param userId user id
	 * @param msg user input
	 * @return the result after the execution of the query
	 * @throws Exception if database connection is failed
	 */
	@Override
	public String process(String userId, String msg) throws Exception {
		// TODO Auto-generated method stub
		UQDBEngine searchEngine = new UQDBEngine();
		return searchEngine.uqQuery(userId, msg);
	}
}