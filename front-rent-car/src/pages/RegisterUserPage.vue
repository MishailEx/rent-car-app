<template>
  <div>

    <label class="input-label" for="username">Имя</label><br>
    <my-input class="input-field" v-model="username" type="text" id="username" placeholder="Name"></my-input>

    <label class="input-label" for="email">Email</label><br>
    <my-input class="input-field" v-model="email" type="text" id="email" placeholder="Email"></my-input>

    <label class="input-label" for="password">Пароль</label><br>
    <my-input class="input-field" v-model="password" type="password" id="password" placeholder="Password"></my-input>
    <div class="input-container">
      <label class="input-label" for="phone">Телефон</label><br>
      <my-input class="input-field" v-model="phone" type="text" id="phone" placeholder="Phone"></my-input>
    </div>
    <button-custom @click="reg">Регистрация</button-custom>
  </div>
</template>

<script>
import api from "@/utils/axios";
import {mapState} from "vuex";
import MyInput from "@/components/UI/InputCustom";
import ButtonCustom from "@/components/UI/ButtonCustom";

export default {
  components: {ButtonCustom, MyInput},
  data() {
    return {
      username: '',
      email: '',
      password: '',
      phone: '',
    };
  },
  computed: {
    ...mapState({
      uuid: state => state.user.uuid,
      token: state => state.user.token
    })
  },
  methods: {
    reg() {
      const user = {
        username: this.username,
        email: this.email,
        password: this.password,
        phone: this.phone,
      };

      api.post('http://localhost:8765/auto-user-service/register', user)
          .then(rsl => {
            this.$store.commit('user/setToken', rsl.data.token);
            this.$store.commit("user/setUuid", rsl.data.uuid);
            this.$router.push("/");
          })
          .catch(error => {
            console.log(error)
          });
    },
  },
};
</script>
<style scoped>
.input-label {
  font-size: 16px;
  color: teal;
}

.input-field {
  width: 100%;
  padding: 5px;
  border-radius: 4px;
  font-size: 16px;
}

.input-container {
  margin-bottom: 5px;
}
</style>