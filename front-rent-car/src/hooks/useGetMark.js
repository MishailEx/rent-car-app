import axios from "axios";
import {ref, onMounted} from 'vue';

export function useGetMark() {
    const marks = ref([])

    const fetching = async () => {
        try {
            const response = await axios.get('http://localhost:8525/api/search/mark');
            marks.value = response.data;
        } catch (e) {
            alert(e)
        }
    }

    onMounted(fetching)

    return {
        marks
    }
}