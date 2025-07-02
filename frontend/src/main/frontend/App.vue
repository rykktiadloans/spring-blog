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

  <RouterView />
</template>

<style scoped>
</style>
