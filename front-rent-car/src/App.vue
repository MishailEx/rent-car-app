<template>
  <navbar-page></navbar-page>
  <div id="app">
    <router-view></router-view>
  </div>

</template>

<script>
import NavbarPage from "@/pages/NavbarPage";

export default {
  name: 'App',
  components: {
    NavbarPage,
  }
}
</script>

<style>
#app {
  text-align: center;
  width: 80%;
  margin: 0 auto;
}
</style>
