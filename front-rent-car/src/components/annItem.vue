<template>
  <div class="ann">
    <div>
      <div class="ann__title">{{ ann.name }}</div>
      <br>
      <div class="ann__price">Аренда от: {{ ann.price }}</div>
    </div>

    <div class="image-gallery">
      <div v-if="hasImages" class="controls controls-left" @click="previousImage" :disabled="currentIndex === 0">
        <div class="arrow-left"></div>
      </div>
      <div class="image-container">
        <img :src="currentImage" alt="Image" class="image" />
      </div>
      <div v-if="hasImages" class="controls controls-right" @click="nextImage" :disabled="currentIndex === images.length - 1">
        <div class="arrow-right"></div>
      </div>
    </div>

    <div class="ann__btns">
      <button-custom
          @click="$router.push('/ann/' + ann.id)"
      >
        Открыть
      </button-custom>
      <button-custom
          v-if="$store.state.user.uuid === ann.userUUID"
          @click="$emit('remove', ann)"
      >
        Удалить
      </button-custom>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    ann: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      currentIndex: 0,
      images: []
    };
  },
  mounted() {
    if (this.hasImages) {
      for (let i = 0; i < this.ann.images.length; i++) {
        this.images.push(this.getImageUrl(this.ann.images[i]))
      }
    }
  },
  computed: {
    hasImages() {
      return this.ann.images && this.ann.images.length > 0;
    },
    currentImage() {
      return this.images[this.currentIndex];
    }
  },
  methods: {
    getImageUrl(imageName) {
      const minioUrl = "http://localhost:9000";
      const bucketName = "car-rent";

      return `${minioUrl}/${bucketName}/${imageName}`;
    },
    previousImage() {
      if (this.currentIndex > 0) {
        this.currentIndex--;
      }
    },
    nextImage() {
      if (this.currentIndex < this.images.length - 1) {
        this.currentIndex++;
      }
    }
  }
}
</script>

<style scoped>

.controls {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  border: none;
  background-color: transparent;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.controls:hover {
  background-color: rgba(0, 0, 0, 0.1);
}

.arrow-left,
.arrow-right {
  transform: rotate(-135deg);
  width: 10px;
  height: 10px;
  border-top: 2px solid #000;
  border-right: 2px solid #000;
  transition: border-color 0.3s ease;
}

.controls-right .arrow-right {
  transform: rotate(45deg);
}

.controls:hover .arrow-left,
.controls:hover .arrow-right {
  border-color: #000;
}

.image-gallery {
  display: flex;
  align-items: center;
}

.image-container {
  margin: 0 10px;

}

.image {
  max-width: 200px;
  max-height: 200px;
  object-fit: cover;
}

.ann {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #f4f4f4;
  padding: 20px;
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.ann__title {
  font-size: 18px;
  font-weight: bold;
}

.ann__price {
  margin-top: 10px;
  font-weight: bold;
}

</style>