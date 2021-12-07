package com.example.twiliochat.services.base;

import java.util.List;

import com.example.twiliochat.vo.ConversationVO;
import com.example.twiliochat.vo.CreateConversationVO;
import com.example.twiliochat.vo.MessageVO;
import com.example.twiliochat.vo.SendMessageVO;

public interface ChatServiceInterface {

	List<MessageVO> getMessages(String conversationId);
	
	void createConversation(CreateConversationVO createConversationVO);
	
	public List<ConversationVO> getConversationsByUserId(Integer companyId);
	
	void sendMessage(SendMessageVO messageVO);
	
	void deleteConversation(String conversationId);
	
	String generateApiToken(String identity);
}
