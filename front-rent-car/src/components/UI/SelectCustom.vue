<template>
  <select class="select" @change="changeOptions" >
    <option value="">
      Упорядочить...
    </option>
    <option v-for="option in options"
            :key="option.id"
            :value="option.id"
    >
      {{option.name}}
    </option>
  </select>
</template>

<script>

export default {
  name: 'select-custom',
  props: {
    modelValue: {
      type: Object,
      required: true
    },
    options: {
      type: Array,
      default: () => []
    }
  },
  methods: {
    changeOptions(event) {
      this.$emit('update:modelValue', event.target.value);
    }
  }
}
</script>

<style scoped>
.select {
  width: 50%;
  padding: 10px 15px;
  margin-bottom: 15px;
  border: none;
  border-bottom: 2px solid teal;
  background-color: transparent;
  font-size: 16px;
  color: teal;
  transition: border-bottom-color 0.3s ease;
  outline: none;
  appearance: none;
}

.select:focus {
  border-bottom-color: #008080;
}

.select option {
  color: teal;
  background-color: white;
}
</style>