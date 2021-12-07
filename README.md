# TwilioChatApp

- Crie uma conta gratuita em https://www.twilio.com/
- Altere os tokens em application.properties no projeto backend
- Rode o backend, que é responsável por gerar o Token
- Rode o frontend
- Inicie uma sessão com um usuário
- Crie um chat com outro usuário, inserindo um id da empresa do fornecedor e do orçamento para chave
- Envie mensagens e teste os comportamentos 

## Endpoints

- @GetMapping("/messages") recupera as mensagens de uma conversa

Requer:
	 String messageBody;
	 String author;
	 String conversationId;	
	 String participantId;
	
- @PostMapping("/createConversation") cria uma conversa
 
Requer:
	 Integer userId;
	 Integer secondUserId;
	 Integer supplierId;
	 Integer orderId;
   
- @GetMapping("/conversation") busca todas as conversas do usuário

Requer:
	 Integer userId;
	
- @GetMapping("/token/{identity}") gera o token
