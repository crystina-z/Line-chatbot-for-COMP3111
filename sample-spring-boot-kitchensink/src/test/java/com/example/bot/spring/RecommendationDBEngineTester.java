package com.example.bot.spring;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.bot.spring.textsender.*;
import com.example.bot.spring.database.*;

@RunWith(SpringRunner.class)

@SpringBootTest(classes = { RecommendationDBEngineTester.class, RecommendationTextSender.class})
		
public class RecommendationDBEngineTester {
	@Autowired
	private RecommendationTextSender Rsender;
	private String testerId="234567";
	@Test
	public void RecommendationTester() throws Exception {
		System.out.println("-------- inside RecommendationTester --------------");
		boolean thrown = false;
		String result = null;
		//ArrayList<String> temp = new ArrayList<String>();
		Rsender = new RecommendationTextSender();
		//temp.add("food");
		//temp.add("spring");
		//System.err.println(temp.get(0) + " " + temp.get(1));
		//System.err.println("it is good here");
		try {
			//System.err.println("it is good here");
			result = this.Rsender.process(testerId,"food spring");
		} catch (Exception e) {
			//System.err.println(e.getMessage());
			e.printStackTrace();
			thrown = true;
		}
		assertThat(!thrown).isEqualTo(true);
		assertThat(result).isEqualTo("Tours with good spring : 2D001: Shimen National Forest Tour\n2D002: Yangshan Hot Spring Tour\n2D003: Heyuan Hotspring Tour\n\nNo tours with good food.");
		try {
			//System.err.println("it is good here");
			result = this.Rsender.process(testerId,"I want nothing");
		} catch (Exception e) {
			//System.err.println(e.getMessage());
			e.printStackTrace();
			thrown = true;
		}
		assertThat(thrown).isEqualTo(true);
		//assertThat(result).isEqualTo("No matching");
		try {
			//System.err.println("it is good here");
			result = this.Rsender.process(testerId,"hotel view");
		} catch (Exception e) {
			//System.err.println(e.getMessage());
			e.printStackTrace();
			thrown = true;
		}
		//assertThat(!thrown).isEqualTo(true);
		assertThat(result).isEqualTo("Tours with good hotel : 2D001: Shimen National Forest Tour\n2D004: National Park Tour\n\n"
				+ "Tours with good view : 2D001: Shimen National Forest Tour\n2D002: Yangshan Hot Spring Tour\n2D003: Heyuan Hotspring Tour\n2D004: National Park Tour\n2D005: Yummy Sight seeing Tour..");
	}
}
