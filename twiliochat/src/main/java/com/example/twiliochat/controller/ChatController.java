package com.example.twiliochat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.twiliochat.services.base.ChatServiceInterface;
import com.example.twiliochat.vo.ConversationVO;
import com.example.twiliochat.vo.CreateConversationVO;
import com.example.twiliochat.vo.GetConversationVO;
import com.example.twiliochat.vo.MessageVO;
import com.example.twiliochat.vo.SendMessageVO;
import com.example.twiliochat.vo.TokenVO;

@RestController
public class ChatController {

	@Autowired
	ChatServiceInterface chatService;

	@GetMapping("/messages")
	List<MessageVO> all(@RequestBody SendMessageVO messageInfo) {
		return chatService.getMessages(messageInfo.getConversationId());
	}
	
	@PostMapping("/createConversation")
	void createChat(@RequestBody CreateConversationVO createConversationVO) {
		chatService.createConversation(createConversationVO);
	}
	
	@GetMapping("/conversation")
	List<ConversationVO> getConversation(@RequestBody GetConversationVO chatInfo) {
		return chatService.getConversationsByUserId(chatInfo.getCompanyId());
	}
	
	@GetMapping("/token/{identity}")
	TokenVO getToken(@PathVariable String identity) {
		TokenVO token = new TokenVO();
		token.setIdentity(identity);
		token.setToken(chatService.generateApiToken(identity));
		return token;
	}
	
	@DeleteMapping("/conversation")
	void deleteConversation(@RequestBody SendMessageVO chatInfo) {
		chatService.deleteConversation(chatInfo.getConversationId());
	}
}
