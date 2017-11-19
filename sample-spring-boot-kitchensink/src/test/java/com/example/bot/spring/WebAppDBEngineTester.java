package com.example.bot.spring;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;

import com.example.bot.spring.textsender.*;
import com.example.bot.spring.database.*;

@RunWith(SpringRunner.class)

@SpringBootTest(classes = { WebAppDBEngineTester.class, WebAppDBEngine.class})

public class WebAppDBEngineTester {
	@Autowired
	private WebAppDBEngine WDBE;
	
	@Test
	public void tester() throws Exception{
		//Customer cus = new Customer();
		WDBE.getAllCustomerInfo();
		WDBE.getAllTourInfo();
		//WDBE.addNewCustomer(cus);
		WDBE.getUQs();
		WDBE.getGeneralTourInfo();
		WDBE.getAllActivities();
		//WDBE.updateCustomer(cus);
		
	}

}
