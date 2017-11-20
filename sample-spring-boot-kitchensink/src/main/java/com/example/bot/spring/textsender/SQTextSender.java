package com.example.bot.spring.textsender;

import com.example.bot.spring.database.*;
/**
 * implement the simple questions like greetings 
 * @author jsongaf
 *
 */
public class SQTextSender implements TextSender {
	private SQDBEngine sqdbengine; 
	/**
	 * class constructor
	 */
	public SQTextSender() {
		// TODO Auto-generated constructor stub
		this.sqdbengine = new SQDBEngine();
	}
	/**
	 * process the user input
	 * @param userId user id
	 * @param msg uesr input
	 * @return the result after the execution of the query
	 * @throws Exception if database connection is failed
	 */
	@Override
	public String process(String userId, String msg) throws Exception{
		// TODO Auto-generated method stub
		/* Label: greeting/ thanks/ goodbye */
		String label = null; 
		// if msg contains certain keywords, label it
			
			label = sqdbengine.search(msg);
			// label = label.replaceAll("\\s+$", "");	// trunc the whitespace at the end 
					
		switch (label) {
			case "greeting":{
				System.out.print("should be here");
				return "Hi! How can I help you?";
			}
			case "thank":{
				return "You are welcome =)";
			}
			case "bye":{
				return "have a nice day!";
			}
			default:{
				return "";
			}
		}
	}
}
