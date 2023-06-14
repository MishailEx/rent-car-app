import axios from "axios";
import {ref, onMounted} from 'vue';

export function useGetAnnId(id) {
    const announcement = ref(Object)

    const fetching = async () => {
        try {
            const response = await axios.get('http://localhost:8765/auto-ann/api/ann/' + id);
            announcement.value = response.data;
        } catch (e) {
            alert(e)
        }
    }

    onMounted(fetching)

    return {
        announcement
    }
}
