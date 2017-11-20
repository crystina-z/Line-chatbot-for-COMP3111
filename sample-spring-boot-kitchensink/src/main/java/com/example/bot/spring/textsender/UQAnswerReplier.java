package com.example.bot.spring.textsender;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

//import java.util.GregorianCalendar;
//import java.util.List;
//import java.util.Set;
//import java.util.concurrent.TimeUnit;
import com.example.bot.spring.database.*;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.LineMessagingClientImpl;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
/**
 * if someone answers the questions on the website, it will send the message to the user and update the table
 * @author jsongaf
 *
 */
@Component
public class UQAnswerReplier implements Broadcaster{
	
	@Autowired
	LineMessagingClient lineMessagingClient;
	/**
	 * class constructor
	 */
	public UQAnswerReplier() {
		// TODO Auto-generated constructor stub
		//lineMessagingClient = new LineMessagingClientImpl();
	}


	/**
	 * push the message to the users
	 * @throws Exception if database connection is failed
	 */
	@Override
	public void broadcast() throws Exception {
		UQDBEngine searchEngine = new UQDBEngine();
		ArrayList<String> reply=searchEngine.answer();
		for(int i=0; i<reply.size(); i++) {
			String userID;
			String question;
			String answer;
			String[] temp = reply.get(i).split(",");
			userID=temp[0];
			question=temp[1];
			answer=temp[2];
			
			// end testing			
			Message message = new TextMessage("For your question "+question+", the answer is "+answer);
			if(lineMessagingClient == null) {
				System.out.println("--------- lineMessagingClient is null ----");
			}		
			else {
			// lineMessagingClient.pushMessage(new PushMessage(userID, message));
				lineMessagingClient.pushMessage(new PushMessage(userID, message));
			// System.out.println("Sent messages: {}", apiResponse);
			}
		}
	}
}
