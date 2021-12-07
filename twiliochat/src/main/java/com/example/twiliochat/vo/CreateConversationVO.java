package com.example.twiliochat.vo;

import lombok.Data;

@Data
public class CreateConversationVO {
	
	private Integer userId;
	
	private Integer secondUserId;
	
	private Integer supplierId;
	
	private Integer orderId;
	
}
