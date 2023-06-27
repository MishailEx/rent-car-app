import axios from "axios";
import {ref} from 'vue';

export function useAnnouncements() {
    const announcements = ref([])
    const isPostsLoading = ref(true)

    const fetching = async (param = null) => {
        try {
            const response = await axios.get('http://localhost:8765/auto-ann/api/ann/all');

            if (param === "model") {
                announcements.value = [...announcements.value].sort((a, b) => a.name.localeCompare(b.name));
            } else if (param === "price") {
                announcements.value = [...announcements.value].sort((a, b) => b.price - a.price);
            } else {
                announcements.value = response.data;
            }
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
