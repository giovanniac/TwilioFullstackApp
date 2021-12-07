package com.example.twiliochat.vo;

import java.time.ZonedDateTime;

import lombok.Data;

@Data
public class MessageVO {
	
	private String messageBody;
	
	private String authorId;
	
	private ZonedDateTime createdAt;
	
}
