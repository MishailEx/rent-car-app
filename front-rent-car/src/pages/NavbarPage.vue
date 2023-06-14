<template>
  <div class="navbar">
    <button-custom @click="$router.push('/')"> Главная</button-custom>
    <div class="navbar_btns">
      <button-custom @click="routeAddAnn"> Разместить объявление</button-custom>
      <button-custom v-if="$store.state.user.uuid !== ''" @click="$router.push('/user/:id')"> Личный кабинет</button-custom>
      <button-custom v-if="$store.state.user.uuid === ''" @click="$router.push('/login')"> Войти</button-custom>
      <button-custom v-if="$store.state.user.uuid === ''" @click="$router.push('/reg')">Регистрация</button-custom>
      <button-custom v-if="$store.state.user.uuid !== ''" @click="$router.push('/chats')">Чаты</button-custom>
      <button-custom v-if="$store.state.user.uuid !== ''" @click="logout">Выход</button-custom>

    </div>
  </div>
</template>

<script>

import {mapState} from "vuex";
import api from "@/utils/axios";
import router from "@/routes/router";

export default {
  computed: {
    ...mapState({
      uuid: state => state.user.uuid,
      token: state => state.user.token
    })
  },
  methods: {
    logout() {
      api.post('http://localhost:8765/auto-user-service/logout')
          .then(rsl => {
            if (rsl.status === 200) {
              this.$store.commit("user/setToken", '')
              this.$store.commit("user/setUuid", '')
              router.push('/')
            }
          })
          .catch(error => {
            console.log(error);
          });
    },
    routeAddAnn() {
      if (this.$store.state.user.uuid !== '') {
        this.$router.push('/addAnn');
      } else {
        this.$router.push('/login');
      }
    }
  },
}
</script>

<style scoped>
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #fdfdfd;
  padding: 10px 20px;
}

.navbar_btns {
  margin-left: auto;
}
</style>