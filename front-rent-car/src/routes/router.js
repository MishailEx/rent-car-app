import MainPage from "@/pages/MainPage";
import AddAnnouncement from "@/pages/AddAnnouncementPage";
import UserInfo from "@/pages/UserInfoPage";
import {createRouter, createWebHistory} from "vue-router";
import AnnouncementPage from "@/pages/AnnouncementPage";
import LoginPage from "@/pages/LoginPage";
import RegisterUserPage from "@/pages/RegisterUserPage";
import ChatsPage from "@/pages/ChatsPage";
// import {keycloak} from "@/utils/keycloack";

// const authMiddleware = async (to, from, next) => {
//     const authenticated = await keycloak.init({ onLoad: 'login-required' })
//
//     if (authenticated) {
//         next()
//     } else {
//         keycloak.login({
//             redirectUri: window.location.origin + to.fullPath,
//         })
//     }
// }

const routes = [
    {
        path: '/',
        component: MainPage
    },
    {
        path: '/ann/:id',
        component: AnnouncementPage,
    },
    {
        path: '/addAnn',
        component: AddAnnouncement
    },
    {
        path: '/user/:id',
        component: UserInfo
    },
    {
        path: '/login',
        component: LoginPage
    },
    {
        path: '/reg',
        component: RegisterUserPage
    },
    {
        path: '/chats',
        name: 'chats',
        component: ChatsPage,
        props: true
    },
]

const router = createRouter({
    routes,
    history: createWebHistory(process.env.BASE_URL)
})



export default router;