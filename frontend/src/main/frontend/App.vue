<script setup lang="ts">
import { RouterLink, RouterView } from "vue-router";
import { useRepositoriesStore } from "./stores/repositories.store";
import NotificationList from "./components/NotificationList.vue";
import "./assets/common.css";
import rssIcon from "./assets/rss.png";

const repositories = useRepositoriesStore();
</script>

<template>
  <header>
    <div class="wrapper">
      <div class="title"></div>
      <nav>
        <RouterLink to="/">Home</RouterLink>
        <RouterLink to="/posts">Posts</RouterLink>
        <RouterLink to="/owner/newpost" v-if="repositories.user != null">New Post</RouterLink>
        <a href="/rss" class="rss">
          <img :src="rssIcon" alt="RSS">
        </a>
      </nav>
    </div>
  </header>

  <div id="router-view-container">
    <div class="notification-container">
      <NotificationList class="notification-list"/>
    </div>
    <RouterView id="router-view" />
  </div>
</template>

<style scoped>
.wrapper {
  display: flex;
  flex-flow: row nowrap;
  align-items: center;
  gap: 1.5em;
  padding: 10px;
  height: 2em;
  background-color: var(--header-bg);
}

nav {
  display: flex;
  flex-flow: row nowrap;
  width: 100%;
  gap: 0.5em;
}

.rss {
  margin-left: auto;
}

.rss > img {
  max-height: 1.5em;
}

.title {
  color: var(--header-fg);
}

nav a:any-link {
  color: var(--link-fg);
  text-decoration: none;
  transition: text-shadow var(--link-glow-timing) ease-out;
}

nav a:any-link:hover {
  color: var(--link-fg-hover);
  text-shadow: 0px 0px 1em var(--link-fg-hover);
  text-decoration: none;
  transition: text-shadow var(--link-glow-timing) ease-out;
}

.title::before {
  content: var(--title);
}

#router-view-container {
  display: flex;
  flex-flow: column nowrap;
  align-items: center;
}

#router-view {
  padding: 10px;
  height: calc(100vh - 2em);
  width: 100%;
  max-width: 1000px;
  background-color: var(--main-bg);
}

.notification-container {
  position: fixed;
  pointer-events: none;
  width: 100%;
  height: calc(100vh - 2em);
}

.notification-list {
  max-width: 20em;
  width: 100%;
  position: absolute;
  right: 1em;
  top: 1em;
  pointer-events: all;
}
</style>
