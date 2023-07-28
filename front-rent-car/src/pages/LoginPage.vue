<template>

  <div class="login-form">
    <div class="input-container">
      <label for="email" class="input-label">Email</label>
      <my-input v-model="email" id="email" type="text" class="input-field" placeholder="Введите email"/>
    </div>

    <div class="input-container">
      <label for="password" class="input-label">Пароль</label>
      <my-input v-model="password" id="password" type="password" class="input-field" placeholder="Введите пароль"/>
    </div>
  </div>
  <div v-if="errorMessage !== ''">
    <p style="color: red"> {{errorMessage}} </p>
  </div>

  <button-custom @click="login">Войти</button-custom>
</template>

<script>
import axios from 'axios';
import {mapState} from "vuex";
import MyInput from "@/components/UI/InputCustom";
import ButtonCustom from "@/components/UI/ButtonCustom";


export default {
  components: {ButtonCustom, MyInput},
  data() {
    return {
      email: '',
      password: '',
      errorMessage: ''
    };
  },
  computed: {
    ...mapState({
      uuid: state => state.user.uuid,
      token: state => state.user.token
    })
  },
  methods: {
    login() {
      const credentials = {
        email: this.email,
        password: this.password,
      };

      axios.post('http://localhost:8765/auto-user-service/login', credentials)
          .then(response => {
            this.$store.commit('user/setToken', response.data.token);
            this.$store.commit('user/setUuid', response.data.uuid);
            this.$store.commit('user/setName', response.data.name);
            this.$router.push("/");
          })
          .catch(error => {
            if (error.response.status === 400) {
              this.errorMessage = "Неверный логи или пароль"
            }
          });
    },
  },
};
</script>

<style scoped>
.login-form {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.input-container {
  margin-bottom: 5px;
}

.input-label {
  font-size: 14px;
  margin-bottom: 5px;
  color: teal;
}

.input-field {
  width: 100%;
  padding: 5px;
  border-radius: 4px;
  font-size: 16px;
}


</style>