package com.example.bot.spring.textsender;
/**
 * interface of all text senders
 * @author jsongaf
 *
 */
public interface TextSender {

	public String process(String userId, String msg) throws Exception;
}
