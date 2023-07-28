<template>
  <div>
    <div class="app__btns">
      <label for="sort-select" style="color: teal; margin-right: 20px">Сортировка</label>
      <select-custom
          id="sort-select"
          v-model="selectedSort"
          :options="sortOptions"
          @change="handleSortChange"
      />
    </div>
    <my-dialog v-model:show="dialogVisible">
      <post-add
      />
    </my-dialog>
    <ann-list
        :anns="announcements"
        v-if="!isPostsLoading"
        @remove="removeAnn"
    />
    <div v-else>Идет загрузка...</div>
  </div>
</template>

<script>
import {useAnnouncements} from "@/hooks/useAnnouncements";
import AnnList from "@/components/annList";
import axios from "axios";
import {onMounted} from "vue";
import SelectCustom from "@/components/UI/SelectCustom";
import {mapState} from "vuex";

export default {
  components: {SelectCustom, AnnList},
  data() {
    return {
      selectedSort: null
    }
  },
  setup(props) {
    const {announcements, isPostsLoading, fetching} = useAnnouncements();

    onMounted(fetching);

    return {
      announcements,
      isPostsLoading,
      props,
      fetching
    }
  },
  computed: {
    ...mapState({
      sortOptions: state => state.user.sortOptions
    })
  },
  methods: {
    removeAnn(ann) {
      axios.delete('http://localhost:8765/auto-ann/api/ann/delete/' + ann.id).then(rsl => {
        if (Math.floor(rsl.status / 100) === 2) {
          this.fetching(this.selectedSort, null);
        } else {
          console.log(rsl.status);
        }
      })
          .catch(error => {
            console.log(error);
          });
    },
    handleSortChange() {
      if (this.selectedSort === "Модель") {
        console.log(this.announcements)
        this.fetching("model", null);
      } else if (this.selectedSort === "Цена") {
        this.fetching("price", null);
      }
    }
  },

}
</script>

<style scoped>

</style>