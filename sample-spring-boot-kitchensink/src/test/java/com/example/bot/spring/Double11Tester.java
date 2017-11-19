package com.example.bot.spring;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.example.bot.spring.database.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { BookingDBEngineTest.class, BookingDBEngine.class})

public class Double11Tester {
	@Autowired
	private DoubleElevDBEngine DEDBE;
	
	@Test
	public void getDiscountBookidTester() throws Exception {
		String sentTour = DEDBE.getDiscountBookid("sent");
		//String releasedTour = DEDBE.getDiscountBookid("release");
		
		if(sentTour != null) {
			DEDBE.updateBroadcastedTours(sentTour);	
			DEDBE.updateActivityStatus();
			DEDBE.ifTourFull(sentTour);
			DEDBE.updateQuota(sentTour);
		}
	}	
}
