package com.example.bot.spring.textsender;
/**
 * interface of all text senders
 * @author jsongaf
 *
 */
public interface TextSender {
	/**
	 * abstract function
	 * @param userId user id
	 * @param msg user input
	 * @return result of after the execution of the query
	 * @throws Exception if database connection is failed
	 */
	public String process(String userId, String msg) throws Exception;
}
