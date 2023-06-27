

export const userModule = {
    state: () => ({
        isAuth: false,
        name: '',
        uuid: '',
        token: '',
        sortOptions: [
            {value: 'model', name: 'Модель'},
            {value: 'price', name: 'Цена'}
        ]
    }),
    mutations: {
        setAuth(state, bool) {
            state.isAuth = bool
        },
        setUuid(state, uuid) {
            state.uuid = uuid;
        },
        setToken(state, token) {
            state.token = token;
        },
        setName(state, name) {
            state.name = name;
        }
    },
    getters: {
        getUuid(state) {
            return state.uuid;
        },
        getToken(state) {
            return state.token;
        }
    },
    namespaced: true

}
