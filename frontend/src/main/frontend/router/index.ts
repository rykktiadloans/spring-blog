import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import PostsView from "../views/PostsView.vue";
import PostView from "../views/PostView.vue";
import LoginView from "../views/LoginView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: HomeView,
    },
    {
      path: "/posts",
      name: "posts",
      component: PostsView,
    },
    {
      path: "/posts/:id",
      name: "post",
      component: PostView,
    },
    {
      path: "/login",
      name: "login",
      component: LoginView
    }
  ],
});

export default router;
