package com.example.twiliochat.vo;

import lombok.Data;

@Data
public class ConversationVO {

	private String conversationId;
	
	private String uniqueId;
	
	private String name;

	private Integer unreadMessages;
	
	private Integer lastReadedMessageIndex;
}
