package com.example.bot.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.bot.spring.database.BookingDBEngine;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BookingTester.class, TextProcessor.class})

public class BookingTester {
	@Autowired
	private TextProcessor tp;
	
	private BookingDBEngine db = new BookingDBEngine();
	
	private String testerId="bk_tester";
	private String testerId2="bk_tester2";
	
	public BookingTester(){
		
	}
	
	@Test
	public void bookingTest() throws Exception {
		String msgs[]= {"I would like to book","I would like to book tour 2D002",
				"Yes","???","03/12","somename","-2","20","-2","9","-3","0","12345678","Yes ",
				"I would like to book Yangshan Hot Spring Tour", "Yes","dd/mm","03/12","04/12","cancel the booking",
				"I would like to book 2D001","Yes ","03/12","Yes ","2","0","0","Cancel current booking"};
		for (String msg:msgs) {
			try {
				String reply = tp.processText(testerId, msg);
				System.out.println(msg+"//////"+reply);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		db.updateLineUserInfo(testerId2, "categorization", "book");
		db.updateLineUserInfo(testerId2, "status", "double11");
		db.updateLineUserInfo(testerId2, "tourids", "2D00420171205");
		db.updateLineUserInfo(testerId2, "discount", "true");
		String msgs2[]= {"dsaf",
				"someone","Yes","2","2","2","12345678","anything"};
		for (String msg:msgs2) {
			try {
				String reply = tp.processText(testerId2, msg);
				System.out.println(msg+"//////"+reply);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
