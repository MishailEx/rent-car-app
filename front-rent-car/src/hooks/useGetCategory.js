import axios from "axios";
import {ref, onMounted} from 'vue';

export function useGetCategory() {
    const categories = ref([])

    const fetching = async () => {
        try {
            const response = await axios.get('http://localhost:8525/api/search/category');
            categories.value = response.data;
        } catch (e) {
            alert(e)
        }
    }

    onMounted(fetching)

    return {
        categories
    }
}