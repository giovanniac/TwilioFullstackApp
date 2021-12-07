package com.example.twiliochat.vo;

import lombok.Data;

@Data
public class SendMessageVO {
	
	private String messageBody;
	
	private String author;
	
	private String conversationId;
	
	private String participantId;
	
}
