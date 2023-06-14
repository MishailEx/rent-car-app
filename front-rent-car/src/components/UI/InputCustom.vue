<template>
  <div class="input-form">
    <input :value="modelValue" @input="updateValue" class="input-form__input" type="text">
  </div>


</template>

<script>
export default {
  name: 'my-input',
  props: {
    modelValue: [String, String]
  },
  methods: {
    updateValue(event) {
      this.$emit('update:modelValue', event.target.value)
    }
  }
}
</script>

<style scoped>
.input-form {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.input-form__input {
  width: 300px;
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid teal;
  border-radius: 4px;
}
</style>