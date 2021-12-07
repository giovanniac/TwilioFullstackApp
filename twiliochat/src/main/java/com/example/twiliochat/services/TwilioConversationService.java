package com.example.twiliochat.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.twiliochat.services.base.ChatServiceInterface;
import com.example.twiliochat.vo.ConversationVO;
import com.example.twiliochat.vo.CreateConversationVO;
import com.example.twiliochat.vo.MessageVO;
import com.example.twiliochat.vo.SendMessageVO;
import com.twilio.base.ResourceSet;
import com.twilio.jwt.accesstoken.AccessToken;
import com.twilio.jwt.accesstoken.ChatGrant;
import com.twilio.rest.conversations.v1.Conversation;
import com.twilio.rest.conversations.v1.ParticipantConversation;
import com.twilio.rest.conversations.v1.conversation.Message;
import com.twilio.rest.conversations.v1.conversation.Participant;
import com.twilio.rest.conversations.v1.user.UserConversation;

@Service
public class TwilioConversationService implements ChatServiceInterface {

	@Value("${twilio.account.sid}")
	private String twilioAccountSid;
	
	@Value("${twilio.auth.token}")
	private String twilioAuthToken;
	
	@Value("${twilio.chat.service.sid}")
	private String twilioServiceSid;
	
	@Value("${twilio.api.key}")
	private String twilioApiKey;
	
	@Value("${twilio.api.secret}")
	private String twilioApiSecret;
	
	
	@Override
	public List<MessageVO> getMessages(String conversationId) {
		ResourceSet<Message> messages = Message.reader(conversationId).read();
		List<MessageVO> messageVOList = new ArrayList<MessageVO>();

		for (Message record : messages) {
			MessageVO messageVO = new MessageVO();
			messageVO.setAuthorId(record.getAuthor());
			messageVO.setMessageBody(record.getBody());
			messageVO.setCreatedAt(record.getDateCreated());
			messageVOList.add(messageVO);
		}

		return messageVOList;
	}

	@Override
	public void createConversation(CreateConversationVO createConversationVO) {
		Integer orderId = createConversationVO.getOrderId();
		Integer userId = createConversationVO.getUserId();
		Integer secondUserId = createConversationVO.getSecondUserId();
		Integer supplierId = createConversationVO.getSupplierId();
		String uniqueId = createUniqueConversationId(orderId, supplierId);
		if (userId == supplierId) {
			return;
		}
		if (verifyConversation(uniqueId.toString())) {
			return;
		}
		Conversation conversation = Conversation.creator().setUniqueName(uniqueId.toString())
				.setFriendlyName("Exemplo de chat").create();
		Participant.creator(conversation.getSid()).setIdentity(userId.toString()).create();
		Participant.creator(conversation.getSid()).setIdentity(secondUserId.toString()).create();
	}

	@Override
	public List<ConversationVO> getConversationsByUserId(Integer userId) {
		ResourceSet<ParticipantConversation> participantConversations = ParticipantConversation.reader()
				.setIdentity(userId.toString()).read();
		List<ConversationVO> conversationVOList = new ArrayList<ConversationVO>();

		for (ParticipantConversation record : participantConversations) {
			UserConversation userConversation = UserConversation.fetcher(userId.toString(), record.getConversationSid()).fetch();
			
			ConversationVO conversation = new ConversationVO();
			conversation.setConversationId(record.getConversationSid());
			conversation.setUniqueId(record.getConversationUniqueName());
			conversation.setName(record.getConversationFriendlyName());
			conversation.setUnreadMessages(userConversation.getUnreadMessagesCount());
			conversation.setLastReadedMessageIndex(userConversation.getLastReadMessageIndex());
			conversationVOList.add(conversation);
		}

		return conversationVOList;
	}
	
	public String generateApiToken(String identity) {
	    ChatGrant grant = new ChatGrant();
	    grant.setServiceSid(twilioServiceSid);

	    AccessToken token = new AccessToken.Builder(twilioAccountSid, twilioApiKey, twilioApiSecret)
	        .identity(identity).grant(grant).build();

	    return token.toJwt();
	}

	@Override
	public void sendMessage(SendMessageVO messageVO) {
		Message.creator(messageVO.getConversationId()).setAuthor(messageVO.getAuthor())
				.setBody(messageVO.getMessageBody()).create();
	}

	@Override
	public void deleteConversation(String conversationId) {
		Conversation.deleter(conversationId).delete();
	}

	private String createUniqueConversationId(Integer orderId, Integer supplierId) {
		return new StringBuilder().append(orderId).append("_").append("_").append(supplierId)
				.toString();
	}
	

	private boolean verifyConversation(String uniqueId) {
		try {
			Conversation.fetcher(uniqueId).fetch();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
