package com.example.bot.spring;
/*
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
@SpringBootTest(classes = { DBEngineTester.class, DBEngine.class})
public class DBEngineTester {
	@Autowired
	private DBEngine DBE;
	
	@Test
	public void getTextTypeTester() throws Exception {
		int textSize = 6;
		String[] input = {
				"how to apply?",
				"is there any other thing we need to take care",
				"what if the tour is cancelled?",
				"can i book this trip?",
				"can you recommend some trip to shenzhen",
				"some trip with good spring please",
				"i d like to book this one",
				"can you recommend a book to me"
		};		
		String[] expectedLabel = {"gq", "gq", "gq", "book", "reco", "reco", "book", "reco"};		
		String[] type = new String[textSize];
		for (int i = 0; i < textSize; i++) {
			type[i] = DBE.getTextType(input[i]);
		}
		for (int i = 0; i < textSize; i++) {
			assertThat(expectedLabel[i].equals(type[i])).isEqualTo(true);
		}
	}
}
*/