<script setup lang="ts">
import { RouterLink, RouterView } from "vue-router";
import { useRepositoriesStore } from "./stores/repositories.store";

const repositories = useRepositoriesStore();
</script>

<template>
  <header>
    <div class="wrapper">
      <nav>
        <RouterLink to="/">Home</RouterLink>
        <RouterLink to="/posts">Posts</RouterLink>
        <RouterLink to="/owner/newpost" v-if="repositories.user != null">New Post</RouterLink>
      </nav>
    </div>
  </header>

  <RouterView id="router-view" />
</template>

<style>
body {
  margin: 0;
}
</style>

<style scoped>
* {
  box-sizing: border-box;
}
nav {
  padding: 10px;
  height: 2em;

}

#router-view {
  padding: 10px;
  height: calc(100vh - 2em);
}
</style>
