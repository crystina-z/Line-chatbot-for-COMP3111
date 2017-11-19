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
@SpringBootTest(classes = { CancelDBEngineTest.class, CancelDBEngine.class})

public class CancelDBEngineTest {
	@Autowired
	private CancelDBEngine CDBE;
	
	@Test
	public void tester() throws Exception{
		CDBE.getAllUnconfirmedTours();
		CDBE.getAllContactors("11223344");
		CDBE.updateCanceledTours("11223344");
	}
}
