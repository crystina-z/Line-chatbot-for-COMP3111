package com.example.bot.spring.textsender;

import java.util.Set;
import com.example.bot.spring.database.*;
import com.linecorp.bot.client.*;
import com.linecorp.bot.model.*;
import com.linecorp.bot.model.message.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * added features that the clients could give some special discount on special date
 * this function is to inform all the users that there will be a discount
 * @author jsongaf
 *
 */
@Component
public class DoubleElevBroadcaster implements Broadcaster {
	
	DoubleElevDBEngine doubledb; 
	@Autowired
	private LineMessagingClient lineMessagingClient;	
	/**
	 * class constructor
	 */
	public DoubleElevBroadcaster() {
		
	}
	/**
	 * inform all the users that there will be a discount on some trips
	 * @throws Exception
	 */
	public void broadcast() throws Exception{
			doubledb = new DoubleElevDBEngine();
			// check each trip available in db
			// if there are any trip get to min tourist yet haven't been confirmed, confirm order; 
			String tourid = doubledb.getDiscountBookid("released");
			if(tourid.isEmpty())
				return;
			doubledb.updateActivityStatus();
			// extract all contactor info into a Set<String> to;
			// extract tourid into String tourid;
			String broadcast_content = "START COMPETE FOR TOUR " + tourid + " AT 50% DISCOUNT!! \n Wanna Grab one? "; // later if reply yes, then jump to booking, else, do nothing

			Set<String> tourist = doubledb.getAllClient();				
			Message message = new TextMessage(broadcast_content);
				
			lineMessagingClient.multicast(new Multicast(tourist, message));  // Multicast(Set<String> to, String msg)
			doubledb.updateBroadcastedTours(tourid);  // update "confirmed" col of informed tours into TRUE; 
	}
}
