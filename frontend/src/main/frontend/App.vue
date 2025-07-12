<script setup lang="ts">
import { RouterLink, RouterView } from "vue-router";
import { useRepositoriesStore } from "./stores/repositories.store";
import "./assets/common.css";

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
      </nav>
    </div>
  </header>

  <div id="router-view-container">
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

.title {
  color: var(--header-fg);
}

nav a:any-link {
  color: var(--link-fg);
  text-decoration: none;
}

nav a:any-link:hover {
  color: var(--link-fg-hover);
  text-decoration: none;
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
  max-width: 1200px;
}
</style>
