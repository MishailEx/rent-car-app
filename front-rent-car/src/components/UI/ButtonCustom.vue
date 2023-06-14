<template>
  <button class="btn">
    <slot></slot>
  </button>
</template>

<script>
export default {
  name: 'button-custom'
}
</script>

<style scoped>
.btn {
  align-self: flex-end;
  padding: 10px 15px;
  margin: 2px;
  background-color: #1bbebe;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.btn:hover {
  background-color: #008080;
}

.btn:focus {
  outline: none;
  box-shadow: 0 0 0 3px rgba(0, 128, 128, 0.3);
}

.btn:active {
  background-color: #006666;
}
</style>