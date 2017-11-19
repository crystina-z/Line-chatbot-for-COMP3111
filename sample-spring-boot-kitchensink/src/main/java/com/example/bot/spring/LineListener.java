package com.example.bot.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.bot.spring.textsender.*;
/**
 * an observer to the database, inform the users if any change that will trigger an event
 * @author jsongaf
 *
 */
@Component
public class LineListener extends Thread{
	
	@Autowired
	private ConfirmBroadcaster confirmBroadcaster;
	@Autowired
	private CancelBroadcaster cancelBroadcaster;
	@Autowired
	private UQAnswerReplier uqAnswerReplier;
	@Autowired
	private DoubleElevBroadcaster double11Broadcaster;
	
	public LineListener() {
	}
	/**
	 * a loop of all executions
	 */
	public void loop() throws Exception{
		confirmBroadcaster.broadcast();
		cancelBroadcaster.broadcast();
		uqAnswerReplier.broadcast();
		double11Broadcaster.broadcast();
	}
	
	@Override
	/**
	 * a thread to listen to the database
	 */
	public void run() {
		while(true) {
			//TODO: Add what ever function need to run
			//execute 1 time per hour
			try { 				
				loop();
			}catch(Exception e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(60000);
			}catch (InterruptedException e) {
				System.err.println("<><><><>Sleep Error<><><><>");
			}
		}
	}
}
