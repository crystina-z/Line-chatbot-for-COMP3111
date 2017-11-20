package com.example.bot.spring.textsender;

import java.util.List;
import java.util.Set;
import com.example.bot.spring.database.*;
import com.linecorp.bot.client.*;
import com.linecorp.bot.model.*;
import com.linecorp.bot.model.message.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * once the trip is confirmed to go, notify all the users that subscribe to the trip
 * @author jsongaf
 *
 */
@Component
public class ConfirmBroadcaster implements Broadcaster {
	
	ConfirmDBEngine confirmdb; 
	@Autowired
	private LineMessagingClient lineMessagingClient;
	/**
	 * class constructor
	 */
	public ConfirmBroadcaster() {
		// TODO Auto-generated constructor stub
		//this.confirmdb = new ConfirmDBEngine();
	}
	
	/**
	 * inform all the users that the trip is confirmed
	 * @throws Exception if database connection is failed
	 */
	public void broadcast() throws Exception{
		try {
			this.confirmdb = new ConfirmDBEngine();
			// check each trip available in db
			// if there are any trip get to min tourist yet haven't been confirmed, confirm order; 
			List<String> tourids = confirmdb.getAllUnconfirmedTours(true);
			
			// extract all contactor info into a Set<String> to;
			// extract tourid into String tourid;
			for (String tourid: tourids) {
				boolean paid = false; 
				String broadcast_content = "Your tour " + tourid + " has been confirmed! Looking forward to see you on the depart day!";
				Set<String> tourists = confirmdb.getAllContactors(tourid, paid);
				
				Message message = new TextMessage(broadcast_content);			
				lineMessagingClient.multicast(new Multicast(tourists, message));  // Multicast(Set<String> to, String msg)
				confirmdb.updateConfirmedTours(tourid);  // update "confirmed" col of informed tours into TRUE; 
			}
		}catch (Exception e) {
			System.err.println(e);
		}
	}
	
}
