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
import com.example.bot.spring.textsender.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { UQAnswerReplierTester.class, UQAnswerReplier.class})

public class UQAnswerReplierTester {
//	@Autowired
//	private UQAnswerReplier UQReplier;
	
	public UQAnswerReplierTester(){
		
	}
	
	@Test
	public void UQReplierTester() throws Exception {
		UQAnswerReplier UQReplier = new UQAnswerReplier();
		try {
			UQReplier.broadcast();
		} catch (Exception e) {
		}
	}
	
	
}
