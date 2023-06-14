<template>
  <div v-if="anns.length > 0">
    <transition-group name="ann-list">
      <ann-item style="margin-bottom: 5px"
          class="ann"
          v-for="ann in anns"
          v-bind:key="ann.id"
          :ann="ann"
          @remove="$emit('remove', ann)">
      </ann-item>
    </transition-group>
  </div>
  <h2 style="color: red" v-else>Список пуст</h2>
</template>

<script>
import AnnItem from "@/components/annItem";

export default {
  components: {AnnItem},
  props: {
    anns: {
      type: Array,
      required: true
    }
  }
}
</script>

<style scoped>

</style>