<template>
  <div class="announcement-form">
    <div class="form-field">
      <label for="category">Category</label>
      <select-custom id="category"
                     v-model="selectedCategory"
                     :options="categories"
                     @change="loadModels"
      ></select-custom>
    </div>
    <div class="empty-field" v-if="catEmpty">Укажите категорию</div>
    <div class="form-field">
      <label for="mark">Mark</label>
      <select-custom id="mark"
                     :name="name"
                     v-model="selectedMark"
                     :options="marks"
                     @change="loadModels"
      ></select-custom>
    </div>
    <div class="empty-field" v-if="markEmpty">Укажите марку</div>
    <div class="form-field">
      <label for="model">Model</label>
      <select-custom id="model"
                     v-model="selectedModel"
                     :options="models"
      ></select-custom>
    </div>
    <div class="empty-field" v-if="modelEmpty">Укажите модель</div>
    <div class="form-field">
      <div class="split-container">
        <div class="left-section">
          <label for="yearCar" class="input-label">Year</label>
          <select class="select-year" id="yearCar" v-model="selectedYear">
            <option value="">Выберите год</option>
            <option v-for="year in years" :key="year" :value="year">{{ year }}</option>
          </select>
          <div class="empty-field" v-if="yearEmpty">Укажите год</div>
        </div>
        <div class="right-section">
          <label for="price" class="input-label">Стоимость аренды в сутки</label>
          <input type="number" id="priceFrom" v-model="price">
        </div>
      </div>
    </div>
    <div class="empty-field" v-if="priceEmpty">Укажите цену</div>
    <div class="form-field">
      <textarea
          id="description"
          v-model="description"
          placeholder="Description"
      ></textarea>
    </div>
    <div class="empty-field" v-if="descriptionEmpty">Описание отсутствует</div>

    <div v-if="urls && urls.length > 0" class="form-field">
      <div class="image-container">
        <div v-for="(image, index) in urls" :key="index">
          <img  :src="image" alt="Image">
          <div class="delete-button-wrapper">
            <button-custom @click="removeImage(index)">Удалить</button-custom>
          </div>
        </div>
      </div>
    </div>

    <div class="form-field">
      <div class="file-upload">
        <input type="file" id="images" ref="imageInput" multiple @change="handleImageUpload" class="btn">
        <button-custom >Выберите фото авто</button-custom>
      </div>
    </div>

  </div>
  <button-custom @click="saveAnnAndRedirect">Сохранить</button-custom>
</template>

<script>

import SelectCustom from "@/components/UI/SelectCustom";
import axios from "axios";
import ButtonCustom from "@/components/UI/ButtonCustom";
import {useGetCategory} from "@/hooks/useGetCategory";
import {useGetMark} from "@/hooks/useGetMark";
export default {
  data() {
    return {
      selectedCategory: '',
      catEmpty: false,
      selectedMark: '',
      markEmpty: false,
      selectedModel: '',
      modelEmpty: false,
      selectedYear: '',
      yearEmpty: false,
      price: '',
      priceEmpty: false,
      description: '',
      descriptionEmpty: false,
      years: [],
      models: [],
      urls: [],
      images: [],
    };
  },
  components: {ButtonCustom, SelectCustom},
  setup(props) {

    const {categories} = useGetCategory();
    const {marks} = useGetMark();

    return {
      categories,
      marks,
      props,
    }
  },
  methods: {
    handleImageUpload(event) {
      const newImages = Array.from(event.target.files);
      this.images = this.images.concat(newImages);
      newImages.forEach(image => this.urls.push(URL.createObjectURL(image)))
    },
    removeImage(index) {
      this.images.splice(index, 1);
      this.urls.splice(index, 1);
    },
    loadModels: function () {
      if (this.selectedCategory && !this.selectedMark) {
        axios.get('http://localhost:8765/auto-ann/api/search/model?categoryId=' + this.selectedCategory).then(response => {
          this.models = response.data;
        });
      } else if (!this.selectedCategory && this.selectedMark) {
        axios.get('http://localhost:8765/auto-ann/api/search/model?markId=' + this.selectedMark).then(response => {
          this.models = response.data;
        });
      } else if (this.selectedCategory && this.selectedMark) {
        axios.get('http://localhost:8765/auto-ann/api/search/model?categoryId=' + this.selectedCategory + '&markId=' + this.selectedMark).then(response => {
          this.models = response.data;
        });
      } else {
        this.models = [];
      }
    },
    saveAnnAndRedirect() {
      !this.selectedCategory ? this.catEmpty = true : this.catEmpty = false;
      !this.selectedMark ? this.markEmpty = true : this.markEmpty = false;
      !this.selectedModel ? this.modelEmpty = true : this.modelEmpty = false;
      !this.selectedYear ? this.yearEmpty = true : this.yearEmpty = false;
      !this.price ? this.priceEmpty = true : this.priceEmpty = false;
      !this.description ? this.descriptionEmpty = true : this.descriptionEmpty = false;

      if (!this.catEmpty && !this.markEmpty && !this.modelEmpty && !this.yearEmpty && !this.priceEmpty && !this.descriptionEmpty) {
        this.saveAnn();
      }
    },
    saveAnn() {
      var formData = new FormData();

      this.images.forEach(image => {
        formData.append(`images[]`, image);
      });

      var auto = {
        categoryId: this.selectedCategory,
        markId: this.selectedMark,
        modelId: this.selectedModel,
        carYear: this.selectedYear
      };
      axios.post('http://localhost:8765/auto-ann/api/search/autoForAddAnn', auto).then(resp => {

        formData.append("autoId", resp.data.id);
        formData.append("price", this.price);
        formData.append("description", this.description);
        formData.append("userUUID", this.$store.state.user.uuid);

        axios.post('http://localhost:8765/auto-ann/api/ann/add', formData).then(response => {
          console.log(response.data);
        })
            .catch(error => {
              if (error.response && error.response.data && error.response.data.error) {
                console.log(error.response.data.error);
              } else {
                console.log("Произошла ошибка при выполнении запроса");
              }
            });
      });

      setTimeout(() => {
        this.$router.push('/');
      }, 1000);

    }
  },
  mounted: function () {
    for (let i = 1980; i <= 2023; i++) {
      this.years.push(i);
    }
  }
}
</script>

<style scoped>

.file-upload {
  position: relative;
}

.file-upload input[type="file"] {
  position: absolute;
  left: 0;
  top: 0;
  opacity: 0;
  width: 100%;
  height: 100%;
}

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

.select-year {
  width: 30%;
  padding: 10px;
  border: 1px solid teal;
  border-radius: 4px;
  font-size: 16px;
  background-color: white;
  color: teal;
}

.announcement-form {
  display: flex;
  flex-direction: column;
}

.form-field {
  margin-bottom: 15px;
}

label {
  margin-right: 20px;
  font-size: 18px;
  margin-bottom: 5px;
  color: teal;
}

.empty-field {
  color: red;
  margin-top: 5px;
  font-size: 14px;
}

input[type="number"],
textarea {
  width: 80%;

  padding: 10px;
  border: 1px solid teal;
  border-radius: 4px;
  font-size: 16px;
  resize: none;
}

button {
  padding: 10px 20px;
  background-color: teal;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
}

button-custom {
  margin-top: 10px;
}

img {
  margin: 10px 10px;
  max-width: 400px;
  max-height: 200px;
}


.split-container {
  display: flex;
  width: 60%;
  margin: 0 auto;
  justify-content: center;
}

.left-section,
.right-section {
  flex: 1;
  padding-right: 10px;
}

.input-label {
  display: block;
  margin-bottom: 5px;
}

.select-year,
input[type="number"] {
  width: 100%;
  padding: 10px;
  border: 1px solid teal;
  border-radius: 4px;
  font-size: 16px;
}

.empty-field {
  color: red;
  margin-top: 5px;
}


.image-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
  align-items: flex-start;
}

.image-container > div {
  margin: 10px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.image-container img {
  max-width: 200px;
  max-height: 200px;
  object-fit: contain;
  border-radius: 4px;
}

.image-container button-custom {
  margin-top: 5px;
}

.delete-button-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 5px;
}


</style>