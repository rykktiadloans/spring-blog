import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import PostsView from "../views/PostsView.vue";
import PostView from "../views/PostView.vue";
import LoginView from "../views/LoginView.vue";
import NewPostView from "../views/NewPostView.vue";
import NotFoundView from "../views/NotFoundView.vue";

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
      path: "/owner/login",
      name: "login",
      component: LoginView
    },
    {
      path: "/owner/newpost",
      name: "newpost",
      component: NewPostView
    },
    {
      path: "/:pathMatch(.*)*",
      name: "notfound",
      component: NotFoundView
    }
  ],
});

export default router;
