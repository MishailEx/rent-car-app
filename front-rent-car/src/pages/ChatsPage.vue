<template>
  <div class="chat-container">
    <div class="chat-list">
      <div v-if="chats && chats.length > 0">
        <div v-for="item in chats" :key="item.id" class="chat-item">
          <div class="chat-info">
            <p class="chat-username">{{ item.name }}</p>
          </div>
          <div class="chat-actions">
            <button-custom @click="selectChat(item)">
              Open Chat
            </button-custom>
          </div>
        </div>
      </div>
    </div>
    <div class="chat-content">
      <template v-if="selectedChat">
        <div class="messages-container">
          <div v-for="message in selectedChat.messages" :key="message.id" class="chat-message">
            <div :class="{'message-right': $store.state.user.name === message.userName,
              'message-left' : $store.state.user.name !== message.userName}">
              <p>{{ message.userName }}</p>
              <p>{{ message.message }}</p>
              <p>{{ message.timestamp }}</p>
            </div>
          </div>
        </div>
        <div class="chat-input">
          <input v-model="newMessage" placeholder="Type a message...">
          <button @click="sendMessage">Отправить</button>
        </div>
      </template>

      <template v-else-if="$route.query.recUUID">
        <div v-if="selectedChat" class="messages-container">
          <div v-for="message in selectedChat.messages" :key="message.id" class="chat-message">
            <div :class="{'message-left': $store.state.user.name === message.userName,
              'message-right': $store.state.user.name !== message.userName}">
              <p>{{ message.userName }}</p>
              <p>{{ message.message }}</p>
              <p>{{ message.timestamp }}</p>
            </div>
          </div>
        </div>
        <div class="chat-input">
          <input v-model="newMessage" placeholder="Type a message...">
          <button @click="sendMessage">Отправить</button>
        </div>
      </template>

      <div v-else>
        <p>Выберите чат для открытия переписки</p>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      socket: null,
      chats: [],
      selectedChat: null,
      newMessage: '',
      uuids: {
        uuid: this.$store.state.user.uuid,
        uuidRecipient: ''
      }
    };
  },
  mounted() {
    this.loadChats();
    this.loadPreviousMessages();

  },
  methods: {
    loadChats() {
      const userUUID = this.$store.state.user.uuid;
      this.socket = new WebSocket("ws://localhost:8632/ws?" + userUUID);
      this.socket.onmessage = this.handleMessage;

      axios.post('http://localhost:8632/getChatsByUser', {uuid: this.$store.state.user.uuid})
          .then(rsl => {
            if (rsl.data !== null) {
              this.chats = rsl.data
            }
          })
          .catch(error => {
            console.error('Error loading chats:', error);
          });
    },
    loadPreviousMessages() {
      const baseUrl = 'http://localhost:8632';
      if (this.$route.query.recUUID) {
        this.uuids.uuidRecipient = this.$route.query.recUUID;
      }
      axios.post(baseUrl + '/findChat', this.uuids).then(rsl => {
        if (rsl.data !== '') {
          this.selectedChat = rsl.data
          this.getMessagesFromChat();
        }
      })
    },
    getMessagesFromChat() {
      axios.post('http://localhost:8632/getChat', { chatId: this.selectedChat.id }).then(chat => {
        this.selectedChat.messages = this.selectedChat.messages.concat(chat.data);
      });
    },
    selectChat(chat) {
      this.selectedChat = chat;
      this.uuids.uuidRecipient = this.$store.state.user.uuid === chat.senderId ? chat.recipientId : chat.senderId;
    },
    sendMessage() {
      if (this.newMessage !== '') {
        const chatMessage = {
          uuids: this.uuids,
          message: this.newMessage,
          chatId: '',
          name: this.$store.state.user.name
        }
        if (this.selectedChat) {
          chatMessage.chatId = this.selectedChat.id;
          this.socket.send(JSON.stringify(chatMessage));
          this.newMessage = '';

          this.$nextTick(() => {
            const messagesContainer = document.querySelector('.messages-container');
            messagesContainer.scrollTop = messagesContainer.scrollHeight;

            setTimeout(() => {
              const secondLastMessage = messagesContainer.querySelector('.chat-message:nth-last-child(2)');
              if (secondLastMessage) {
                secondLastMessage.scrollIntoView({ behavior: 'smooth' });
              }
            }, 100);
          });
        } else {
          this.uuids.uuidRecipient = this.$route.query.recUUID;
          this.createChat()
              .then(() => {
                if (this.selectedChat) {
                  chatMessage.chatId = this.selectedChat.id;
                  this.socket.send(JSON.stringify(chatMessage));
                  this.newMessage = '';

                  this.$nextTick(() => {
                    const messagesContainer = document.querySelector('.messages-container');
                    messagesContainer.scrollTop = messagesContainer.scrollHeight;

                    setTimeout(() => {
                      const secondLastMessage = messagesContainer.querySelector('.chat-message:nth-last-child(2)');
                      if (secondLastMessage) {
                        secondLastMessage.scrollIntoView({ behavior: 'smooth' });
                      }
                    }, 100);
                  });
                }
              })
              .catch(error => {
                console.error('Error creating chat:', error);
              });
          this.newMessage = '';
        }
      }
    },
    createChat() {
      const data = {
        uuid: this.uuids.uuid,
        uuidRecipient: this.uuids.uuidRecipient,
        name: this.$route.query.name
      }
      return axios.post('http://localhost:8632/createChat', data)
          .then(response => {
            this.selectedChat = response.data;
            this.chats.push(response.data)
          })
          .catch(error => {
            console.error('Error creating chat:', error);
          });
    },
    handleMessage(event) {
      const message = JSON.parse(event.data);
      this.selectedChat.messages.push(message);
    },
  },
};
</script>

<style scoped>

.message-left {
  width: fit-content;
  max-width: 80%;
  text-align: left;
  margin-right: 20px;
  background-color: #e5f7fd;
  color: #000;
  padding: 10px;
  border-radius: 10px;
  margin-bottom: 10px;
}

.message-right {
  width: fit-content;
  max-width: 80%;
  text-align: right;
  margin-left: auto; /* Измененное свойство */
  margin-right: 5px; /* Измененное свойство */
  background-color: #dcf8c6;
  color: #000;
  padding: 10px;
  border-radius: 10px;
  margin-bottom: 10px;
}

.chat-input {
  margin-top: 10px;
}

html, body {
  height: 100%;
  margin: 0;
}

.chat-container {
  display: flex;
}

.chat-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 10px;
  overflow-y: auto;
}

.chat-list {
  flex: 0 0 30%;
  overflow-y: auto;
  border-right: 1px solid #ccc;
}

.chat-item {
  display: flex;
  align-items: center;
  padding: 10px;
  border-bottom: 1px solid #ccc;
}

.chat-info {
  flex: 1;
}

.chat-username {
  font-weight: bold;
}

.chat-actions {
  margin-left: 10px;
}

.messages-container {
  height: 400px;
  overflow-y: auto;
  min-height: 80vh;
}

.chat-message {
  margin-bottom: 10px;
}

.chat-input {
  display: flex;
  align-items: center;
  margin-top: 10px;
}

.chat-input input {
  flex: 1;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  margin-right: 10px;
}

.chat-input button {
  padding: 8px 16px;
  background-color: #4caf50;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

</style>