package com.example.twiliochat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;

import com.twilio.Twilio;

@SpringBootApplication
public class TwiliochatApplication {

	@Value("${twilio.account.sid}")
	private String twilio_account_sid;

	@Value("${twilio.auth.token}")
	private String twilio_auth_token;

	public static void main(String[] args) {
		SpringApplication.run(TwiliochatApplication.class, args);
	}

	@EventListener
	public void onAppContextStarted(ApplicationStartedEvent e) {
		Twilio.init(twilio_account_sid, twilio_auth_token);
	}

}
