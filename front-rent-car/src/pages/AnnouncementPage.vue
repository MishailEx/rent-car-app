<template>
  <div class="announcement">
    <h2 class="announcement__title">{{ announcement.name }}</h2>
    <div class="announcement__content">
      <p class="announcement__description">Описание:</p>
      <p class="announcement__description-text">{{ announcement.description }}</p>
    </div>
    <div>
      <p class="announcement__price">Аренда от: {{ price }}</p>
      <button-custom @click="convertEUR(announcement.price)">EUR</button-custom>
      <button-custom @click="convertUSD(announcement.price)">USD</button-custom>
      <button-custom @click="convertCNY(announcement.price)">CNY</button-custom>

      <div v-if="announcement.images && announcement.images.length > 0" class="image-container">
        <div v-for="(image, index) in announcement.images" :key="index" class="image-wrapper">
          <img :src="getImageUrl(image)" alt="Image" class="image"/>
          <div class="image-overlay"></div>
        </div>
      </div>

      <div v-if="$store.state.user.uuid !== ''">
        <div v-if="$store.state.user.uuid !== announcement.userUUID">
          <button-custom style="margin-top: 10px" @click="$router.push({
            name: 'chats',
            query: {
              recUUID: announcement.userUUID,
              name: announcement.name
            }
          })">
            Связаться
          </button-custom>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { useGetAnnId } from "@/hooks/useGetAnnId";
import { useRoute } from "vue-router";
import ButtonCustom from "@/components/UI/ButtonCustom";
import { ref, watchEffect } from "vue";
import { useStore } from 'vuex';
import axios from "axios";

export default {
  components: { ButtonCustom },
  data() {
    return {
      chats: []
    }
  },
  setup(props) {
    const route = useRoute();
    const store = useStore();
    const { announcement } = useGetAnnId(route.params.id);
    const price = ref(null);
    const uuid = ref(null);
    const chat = ref(null);

    watchEffect(() => {
      if (announcement.value !== undefined) {
        price.value = announcement.value.price;
        uuid.value = announcement.value.userUUID;
        if (store.state.user.uuid === announcement.value.userUUID) {
          axios.post('http://localhost:8765/auto-chat/getChatsByUser', { uuid: store.state.user.uuid }).then(rsl => {
            if (rsl.data !== null) {
              chat.value = rsl.data
            }
          })
        }
      }
    });

    const convertEUR = (num) => {
      if (num !== null) {
        price.value = num * 5;
      }
    };
    const convertUSD = (num) => {
      if (num !== null) {
        price.value = num * 2;
      }
    };
    const convertCNY = (num) => {
      if (num !== null) {
        price.value = num * 10;
      }
    };

    return {
      announcement,
      props,
      price,
      uuid,
      chat,
      convertEUR,
      convertUSD,
      convertCNY
    }
  },
  methods: {
    getImageUrl(imageName) {
      const minioUrl = "http://localhost:9000";
      const bucketName = "car-rent";

      return `${minioUrl}/${bucketName}/${imageName}`;
    }
  }
}
</script>

<style scoped>
.announcement {
  background-color: #f2f2f2;
  border-radius: 4px;
  padding: 20px;
  margin-bottom: 20px;
}

.announcement__title {
  font-size: 24px;
  font-weight: bold;
  color: teal;
  margin-bottom: 10px;
}

.announcement__content {
  margin-bottom: 10px;
}

.announcement__description {
  font-size: 16px;
  font-weight: bold;
  color: #333333;
  margin-bottom: 5px;
}

.announcement__description-text {
  font-size: 14px;
  color: #666666;
}

.announcement__price {
  font-size: 18px;
  color: #666666;
}

.image-container {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin: 20px 20px;
}

.image-wrapper {
  width: 200px;
  height: 200px;
  position: relative;
  overflow: hidden;
  border-radius: 4px;
}

.image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.4);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.image-wrapper:hover .image-overlay {
  opacity: 1;
}

.image {
  width: 100%;
  height: 100%;
  object-fit: contain;
  transition: transform 0.3s ease;
}

.image-wrapper:hover .image {
  transform: scale(1.1);
}
</style>