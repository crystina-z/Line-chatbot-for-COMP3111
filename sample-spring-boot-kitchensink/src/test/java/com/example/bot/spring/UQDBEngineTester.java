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
@SpringBootTest(classes = { UQDBEngineTester.class, UQDBEngine.class})

public class UQDBEngineTester {
	@Autowired
	private UQDBEngine UQDBE;
	
	@Test
	public void AnswerTester() throws Exception{
		UQDBE.answer();
		UQDBE.updateTable();
		UQDBE.retrieveReply();
		UQDBE.uqQuery("234567", "Stupid");
	}
}
