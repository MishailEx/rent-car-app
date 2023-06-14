<!--<template>-->
<!--  <div>-->
<!--    <div v-for="message in messages" :key="message.id">-->
<!--      <div :class="{'message-left': $store.state.user.name === message.userName,-->
<!--       'message-right': $store.state.user.name !== message.userName}" style="border: 3px solid #ccc">-->
<!--        <p>{{ message.userName }}</p>-->
<!--        <p>{{ message.message }}</p>-->
<!--        <p>{{ message.timestamp }}</p>-->
<!--      </div>-->
<!--    </div>-->

<!--    <input v-model="newMessage" placeholder="Type a message...">-->
<!--    <button @click="sendMessage">Send</button>-->
<!--  </div>-->
<!--</template>-->

<!--<script>-->
<!--import axios from 'axios';-->

<!--export default {-->
<!--  data() {-->
<!--    return {-->
<!--      socket: null,-->
<!--      messages: [],-->
<!--      newMessage: '',-->
<!--      chatId: null,-->
<!--      uuids: {-->
<!--        uuid: this.$store.state.user.uuid,-->
<!--        uuidRecipient: this.$route.params.recUUID-->
<!--      }-->
<!--    };-->
<!--  },-->
<!--  mounted() {-->
<!--    const userUUID = this.$store.state.user.uuid;-->
<!--    this.socket = new WebSocket("ws://localhost:8632/ws?" + userUUID);-->
<!--    this.socket.onmessage = this.handleMessage;-->
<!--    this.loadPreviousMessages();-->
<!--  },-->
<!--  methods: {-->
<!--    sendMessage() {-->
<!--      if (this.newMessage !== '') {-->
<!--        const chatMessage = {-->
<!--          uuids: this.uuids,-->
<!--          message: this.newMessage,-->
<!--          chatId: this.chatId,-->
<!--          name: this.$store.state.user.name-->
<!--        }-->
<!--        if (this.chatId) {-->
<!--          this.socket.send(JSON.stringify(chatMessage));-->
<!--        } else {-->
<!--          this.createChat(this.uuids);-->
<!--          this.socket.send(JSON.stringify(chatMessage));-->
<!--        }-->
<!--      }-->
<!--    },-->
<!--    createChat(uuids) {-->
<!--      axios.post('http://localhost:8632/createChat', uuids)-->
<!--          .then(response => {-->
<!--            this.chatId = response.data;-->
<!--          })-->
<!--          .catch(error => {-->
<!--            console.error('Error creating chat:', error);-->
<!--          });-->
<!--    },-->
<!--    handleMessage(event) {-->
<!--      const message = JSON.parse(event.data);-->
<!--      this.messages.push(message);-->
<!--    },-->
<!--    getMessagesFromChat() {-->
<!--      axios.post('http://localhost:8632/getChat', { chatId: this.chatId }).then(chat => {-->
<!--        console.log(chat.data);-->
<!--        this.messages = this.messages.concat(chat.data);-->
<!--      })-->
<!--    },-->
<!--    loadPreviousMessages() {-->

<!--      const baseUrl = 'http://localhost:8632';-->

<!--      // const uuids = {-->
<!--      //   uuid: this.$store.state.user.uuid,-->
<!--      //   uuidRecipient: this.recUUID-->
<!--      // }-->

<!--      axios.post(baseUrl + '/findChat', this.uuids).then(rsl => {-->
<!--        if (rsl.data !== '') {-->
<!--          this.chatId = rsl.data-->
<!--          this.getMessagesFromChat();-->
<!--        }-->
<!--      })-->
<!--    }-->
<!--  },-->
<!--};-->
<!--//   created() {-->
<!--//     this.initWebSocketConnection();-->
<!--//   },-->
<!--//   methods: {-->
<!--//     initWebSocketConnection() {-->
<!--//       const socket = new SockJS('http://localhost:8632/chat');-->
<!--//       this.stompClient = Stomp.over(socket);-->
<!--//       this.stompClient.connect({}, () => {-->
<!--//         console.log('WebSocket connection established');-->
<!--//       });-->
<!--//     },-->
<!--//     createChat() {-->
<!--//       // Отправка запроса на создание чата на сервер-->
<!--//       axios.post('http://localhost:8632/api/chat/create')-->
<!--//           .then(response => {-->
<!--//             this.chatId = response.data.chatId;-->
<!--//             // Подписываемся на получение сообщений из чата-->
<!--//             this.stompClient.subscribe(`/user/queue/chat`, this.onMessageReceived);-->
<!--//           })-->
<!--//           .catch(error => {-->
<!--//             console.error('Error creating chat:', error);-->
<!--//           });-->
<!--//     },-->
<!--//     onMessageReceived(message) {-->
<!--//       const chatMessage = JSON.parse(message.body);-->
<!--//       this.messages.push(chatMessage);-->
<!--//     },-->
<!--//     sendMessage() {-->
<!--//       if (this.chatId != null) {-->
<!--//         this.createChat()-->
<!--//       }-->
<!--//-->
<!--//       const chatMessage = {-->
<!--//         chatId: this.chatId,-->
<!--//         senderId: 'CURRENT_USER_ID', // Замените на актуальный идентификатор текущего пользователя-->
<!--//         recipientId: 'RECIPIENT_USER_ID', // Замените на актуальный идентификатор получателя-->
<!--//         message: this.newMessage-->
<!--//       };-->
<!--//-->
<!--//       // Отправка сообщения на сервер-->
<!--//       this.stompClient.send('/app/chat', {}, JSON.stringify(chatMessage));-->
<!--//-->
<!--//       this.newMessage = '';-->
<!--//     }-->
<!--//   }-->
<!--// }-->
<!--</script>-->

<!--<style scoped>-->
<!--.message-left {-->
<!--  text-align: left;-->
<!--  background-color: #5cdada;-->
<!--  /* Другие стили для сообщений слева */-->
<!--}-->

<!--.message-right {-->
<!--  text-align: right;-->
<!--  background-color: #9df886;-->
<!--  /* Другие стили для сообщений справа */-->
<!--}-->
<!--</style>-->