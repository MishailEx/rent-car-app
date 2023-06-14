import axios from "axios";
import {ref} from 'vue';

export function useAnnouncements() {
    const announcements = ref([])
    const isPostsLoading = ref(true)

    const fetching = async () => {
        try {
            const response = await axios.get('http://localhost:8765/auto-ann/api/ann/all');
            announcements.value = response.data;
        } catch (e) {
            alert(e)
        } finally {
            isPostsLoading.value = false;
        }
    };

    return {
        announcements,
        isPostsLoading,
        fetching
    }
}
