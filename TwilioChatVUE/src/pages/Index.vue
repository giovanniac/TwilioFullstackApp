<template>
<div>
  <!-- Card com informações do Chat -->
  <q-card dark bordered class="bg-grey-9 my-card">
      <q-card-section>
        <div class="text-h6">
          Status da Conexão com Twilio
          <div class="row">
            <q-input bg-color="white" class="col-3" outlined v-model="userId" label="UserID" />
            <q-btn icon="login" @click="initConversationsClient"/>
          </div>
        </div>
        <div class="text-subtitle2">{{statusString}}</div>
      </q-card-section>
  </q-card>
  <q-separator />
  <!-- Inputs que contém as informações para iniciar o chat -->
  <div class="row q-gutter-sm justify-center">
    <div class="q-pa-sm col-2">
        <q-input class="row q-mt-md" outlined v-model="secondUserId" label="SecondUserID" />
        <q-input class="row q-mt-md" outlined v-model="supplierId" label="SupplierID" />
        <q-input class="row q-mt-md" outlined v-model="orderId" label="OrderID"/>
        <q-btn class="row q-mt-md" color="primary" label="Iniciar Chat" @click="createConversation"/>
    </div>
    <!-- Mensagens -->
    <div class="q-pa-md col-9 justify-center">
      <message v-if="activeConversation" :active-conversation="activeConversation" :name="userId" />
    </div>
  </div>
  <q-separator />
  <div v-if="activeConversation">
    <q-btn class="row q-mt-md" color="primary" label="Marcar todas como Unread" @click="unreadAllMessages"/>
    <q-btn class="row q-mt-md" color="primary" label="Marcar todas como Read" @click="readAllMessages"/>
  </div>
</div>
</template>

<script>
const { Client } = require('@twilio/conversations');
import { defineComponent } from 'vue';
import Message from 'components/chat/Message.vue';

export default defineComponent({
  name: 'PageIndex',
  components: {
    Message
  },
  data: function () {
    return {
			statusString: "",
			activeConversation: null,
			nameRegistered: false,
			isConnected: false,
      message: "",
      userId: "",
      supplierId: "",
      orderId: "",
      secondUserId: ""
    }
  }, 
  methods: {
		initConversationsClient: async function() {
			window.conversationsClient = Client
			const token = await this.getToken(this.userId)
			this.conversationsClient = new Client(token);
			this.statusString = "Conectando ao Twilio…"
			this.conversationsClient.on("connectionStateChanged", (state) => {
				switch (state) {
          case "connected":
            this.statusString = "Conectado ao Twilio!"
            this.isConnected = true
            this.getAllConversationsFromUser()
            break
          case "disconnecting":
            this.statusString = "Desconectando do Twilio..."
            break
          case "disconnected":
            this.statusString = "Desconectado!"
            break
          case "denied":
            this.statusString = "Erro ao conectar!"
            break
				}
			})
		},

		getToken: async function(identity) {
			const response = await fetch(`http://localhost:8080/token/${identity}`)
			const responseJson = await response.json()
			return responseJson.token
		},

		createConversation: async function() {
			try {
        if (this.secondUserId == this.userId || this.secondUserId == "") {
          console.log("É necessário um segundo usuário participante da conversa")
          return
        }
        await this.conversationsClient.getUser(this.userId)
				await this.conversationsClient.getUser(this.secondUserId)
			} catch {
				console.error("Waiting for User1 and User2 client sessions")
				return
			}

			try {
				const newConversation = await this.conversationsClient.createConversation({uniqueName: this.orderId+ "_" + this.supplierId})
				const joinedConversation = await newConversation.join().catch(err => console.log(err))
				await joinedConversation.add(this.secondUserId).catch(err => console.log("error: ", err))
				this.activeConversation = joinedConversation
        console.log(this.activeConversation)
			} catch {
				this.activeConversation = await (this.conversationsClient.getConversationByUniqueName(this.orderId+ "_" + this.supplierId))
        console.log(this.activeConversation)
			}
		},

    unreadAllMessages: async function() {
      await this.activeConversation.setAllMessagesUnread();
      let info = await this.activeConversation.getUnreadMessagesCount().then((count) => {return count});
      console.log(info)
		},

    getAllConversationsFromUser: async function()  {
      let userConversations = []
      await this.conversationsClient.getSubscribedConversations().then((channels) => userConversations.push(channels['items']))
      console.log('Todas as conversas do Usuário', userConversations)
    }
  }
})
</script>
