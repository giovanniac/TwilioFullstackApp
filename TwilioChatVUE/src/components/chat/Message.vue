<template>
<div class="row q-gutter-sm justify-center">
    <div class="col-5">
      <div class="q-pa-md row">
        <q-scroll-area class="col-12" style="height: 200px;">
            <div 
                v-for="message in messages" :key="message.index"
             >
              <q-chat-message
                  :name="message?.state?.author"
                  :text="[''+message?.state?.body]"
                  :sent="message?.state?.author === name"
              />
	          </div>
        </q-scroll-area>
      </div>
      <!-- Texto da nova mensagem -->
      <div class="q-gutter-sm row justify-center">
        <q-input class="col-12" outlined v-model="message"/>
      </div>
    </div>
    <!-- Botão para enviar nova mensagem -->
    <div class="col-1 self-end">
        <q-btn round color="primary" icon="send" @click="sendMessage" />
    </div>
  </div>
</template>

<script>
export default {
	props: {
    activeConversation: {
      type: Object,
      required: true
    },
    name: {
      type: String,
      required: true
    }
  },
	data() {
		return {
			messages: [],
			isSignedInUser: false,
      message: ""
		}
	},
	mounted() {
    this.activeConversation.getMessages()
      .then((newMessages) => {
        this.activeConversation.setAllMessagesRead();
        this.messages = [...this.messages, ...newMessages.items]
      })
    this.activeConversation.on("messageAdded", (message) => {
      console.log(message.index)
      this.activeConversation.updateLastReadMessageIndex(message.index)
      this.messages = [...this.messages, message];
    })
	}, 
  methods: {
    sendMessage: async function() {
			this.activeConversation.sendMessage(this.message)
				.then(() => {
					this.message = ""
				})
		}
  }
}
</script>

<style>
</style>