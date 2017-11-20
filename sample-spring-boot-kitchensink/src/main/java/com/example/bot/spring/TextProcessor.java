package com.example.bot.spring;

import com.example.bot.spring.database.DBEngine;
import com.example.bot.spring.database.DoubleElevDBEngine;
import com.example.bot.spring.textsender.*;
/**
 * process all the user input and categorize the information
 * @author jsongaf
 *
 */
public class TextProcessor {

	private DBEngine DBE;
	private DoubleElevDBEngine DEDBE; 
	
	public TextProcessor() {
		// TODO Auto-generated constructor stub
		DBE=new DBEngine();
		DEDBE = new DoubleElevDBEngine();
	}
	
	/**
	 * classify the user input and see what kind of request the user is asking
	 * @param userId user id
	 * @param text user input
	 * @return the category of the user input
	 * @throws Exception if database connection is failed
	 */
	private String classifyText(String userId, String text) throws Exception{
		String reply="";		
		
		try {			
			String tag = DBE.getLineUserInfo(userId,"categorization"); // from database; 
			String label = DBE.getTextType(text);					   // by analysis input 
						
			SQTextSender sqsender = new SQTextSender();
			reply = sqsender.process(userId, text);
			if(!reply.isEmpty())
				reply += "\n";
			
			if(tag.equals("book")) {
				BookingTextSender bsender = new BookingTextSender();
				reply += bsender.process(userId, text);	
				return reply;
			}
			
			System.out.println(text.charAt(0));
			System.out.println(text.charAt(text.length()-1));
			System.out.println(text);
			if (text.toLowerCase().equals("yes")|| text.toLowerCase().equals("no")) {
				System.out.println("We should handle double 11");
				return double_elev_handler(userId, text);
			}
			
			// after decide the label:
			// - update user tag;
			// - pass the info to corresponding text processor;			
			switch (label) {
			case "reco": 
				RecommendationTextSender rsender = new RecommendationTextSender();
				DBE.updateLineUserInfo(userId,"categorization","reco");	
				reply += rsender.process(userId, text);			
				return reply;
				
			case "gq":
				GQTextSender gqsender = new GQTextSender();
				DBE.updateLineUserInfo(userId,"categorization", "gq");	
				reply += gqsender.process(userId, text);			
				return reply;
				
			case "book":
				BookingTextSender bsender = new BookingTextSender();
				DBE.updateLineUserInfo(userId,"categorization","book");	
				reply += bsender.process(userId, text);			
				return reply;
				
			default:
				// no action, continue to next checking state; 				
			}
			
			switch (tag) {
			case "reco":
				RecommendationTextSender rsender = new RecommendationTextSender();
				reply += rsender.process(userId, text);	
				return reply;
			case "gq":
				GQTextSender gqsender = new GQTextSender();
				reply += gqsender.process(userId, text);	
				return reply;
			default:
				// no action, continue to unanswered question processor; 
			}
			
			reply += "You can send your request by specifying: recommendation/ general question/booking a trip";
			UQAutomateSender uqSender = new UQAutomateSender();
			uqSender.process(userId, text);	
			if(reply.equals("")) throw new Exception();
			return reply;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				DBE.updateLineUserInfo(userId,"categorization", "default");
			}catch(Exception e2) {
				// no action; 
			}	
			
			UQAutomateSender uqSender = new UQAutomateSender();			
			reply += uqSender.process(userId, text);			
			
			return reply; //return "exception here";
		}

	}
	/**
	 * process the text and extract the key words from it
	 * @param userId user id
	 * @param text user input
	 * @return the result after process the user input
	 * @throws Exception if database connection is failed
	 */
	public String processText(String userId, String text) throws Exception{
		if (text == null) {// yet text won't be null?
			throw new Exception("no input");
		}
		String reply = "";		
		
		text = formatMsg(text);	// format the text before classification 
		DBE.updateLineUserInfo(userId,"lastq",text); // insert the formatted text into database; 
		
		if(text.equals("")) {
			throw new Exception("no input");
		}else {
			reply = classifyText(userId,text);
			DBE.updateLineUserInfo(userId,"lasta",reply);
			return reply;
		}
	}
	
	/** Store the new subscriber information in the database
	 * 
	 * @param id: Userid
	 * @return
	 * @throws Exception 
	 */
	public String newSubscriber(String id) throws Exception {
		DBE.updateLineUserInfo(id,"lastq","");
		String reply = "Thank you for subscribing our travel agency chatbot.\n"
				+ "You can send your request by specifying: recommendation/ general question/booking a trip";
		return reply;
	}
	


	/**
	 * truncate all non-digit and non-char elements from user input (decimal point is reserved)
	 * and transform the input into lower case; 
	 * it's possible for formatMsg() to return a empty text, which need to be handled in its calling function
	 * @param message user input
	 * @return the formatted reply
	 */
	private String formatMsg(String message) {
		char preChar = 'S';
		String outputMsg = "";
		
		for (char c: message.toCharArray()) {
			if(isDigit(c) || isChar(c) || isAllowedPunc(c)){
				outputMsg += c;
				preChar = c;
			}else if (preChar != ' '){
				outputMsg += ' ';
				preChar = ' ';
			}
		}
		outputMsg = outputMsg.toLowerCase();	
		return outputMsg;
	}

	/**
	 * helper function for formatMsg()
	 * check if next character is 'char'
	 * @param c input
	 * @return if it is char
	 */
	private boolean isChar (char c) {
		if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
			return true;
		else
			return false; 
	}
	
	/**
	 * helper function for formatMsg()
	 * check if next character is 0-9 or decimal point
	 * @param c input
	 * @return if it is decimal
	 */
	private boolean isDigit (char c) {
		if ((c >= '0' && c <= '9') || c == '.')
			return true;
		else
			return false; 
	}
	
	/**
	 * helper function for formatMsg()
	 * check if next character is legal puncuation
	 * @param c input
	 * @return if the puncuation is legal
	 */
	private boolean isAllowedPunc(char c) {
		if ( c == '/')
			return true;
		else
			return false; 
	}

	/** special handler for double 11 event; 
	 * after a double11 message is broadcast, check user reply;
	 * if reply yes:
	 * @param userId user id
	 * @param message user input
	 * @return special discount of double eleven
	 * @throws Exception if database connection is failed
	 */
	private String double_elev_handler(String userId, String message) throws Exception {
		assert (message.equals("yes")|| message.equals("no"));
		String reply = "";
		if (message.equals("yes")) {
			// get current discount tour
			String discount_tourid = DEDBE.getDiscountBookid("sent"); 					// check double11 table, get available tour's id; id =  DEDBE.getDiscountBookid()	
			System.out.println(discount_tourid);
			// check if there are still ticket:
			if(DEDBE.ifTourFull(discount_tourid)) {
				DBE.updateLineUserInfo(userId,"categorization", "book"); 			// update line_user_info.categorization into "booking"	
				DBE.updateLineUserInfo(userId,"status", "double11"); 			// update line_user_info.categorization into "booking"	
				DBE.updateLineUserInfo(userId,"tourids", discount_tourid); 	// update line_user_info.discount_tour_id = id;
				DBE.updateLineUserInfo(userId,"discount", "true"); 	// update line_user_info.discount_tour_id = id;
				DEDBE.updateQuota(discount_tourid);
				reply = "Congratulations! You Got A Discount Ticket! Now you can continue booking!";
			}else {
				reply = "Sorry ticket sold out";
			}
		}
		else {
			reply = "Sure =) Have a nice day."; 
		}
		
		return reply;
	}
}
