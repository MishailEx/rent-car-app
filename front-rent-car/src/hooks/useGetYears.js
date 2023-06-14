
import {ref, onMounted} from 'vue';

export function useGetYears() {
    const years = ref([])

    const fetching = () => {
        try {
            const year = {
                id: '',
                name: ''
            }
            const value = [];
            for (let i = 1980; i <= 2023; i++) {
                year.id = i;
                year.name = i;
                value.push(year);
            }
            years.value = value;
        } catch (e) {
            alert(e)
        }
    }

    onMounted(fetching)

    return {
        years
    }
}