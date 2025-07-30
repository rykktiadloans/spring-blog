<script setup lang="ts">
import { useRepositoriesStore } from "../stores/repositories.store";
import "../assets/common.css";
import type { Ref } from "vue";
import type { Post } from "../model/post";
import { ref } from "vue";
import Loading from "../components/Loading.vue";
import PostCard from "../components/PostCard.vue";
import { onMounted } from "vue";
import { useTitle } from "@vueuse/core";

const repositories = useRepositoriesStore();
const postRepository = repositories.postRepository;

const posts: Ref<Post[]> = ref([]);
const isProcessing = ref(true);

const title = useTitle("Home");

onMounted(async () => {
  const newPosts = await postRepository.fetchPostsByPage(0);
  posts.value = newPosts.slice(0, 5);
  isProcessing.value = false;
});
</script>

<template>
  <main>
    <h2>Welcome to rykktiadLoans' blog!</h2>
    <h3>Latest few posts:</h3>
    <div>
      <TransitionGroup class="post-card-container" name="list" tag="PostCard">
        <PostCard v-for="post in posts" :key="post.id" :post="post" />
      </TransitionGroup>
      <Loading v-if="isProcessing" />
    </div>
  </main>
</template>

<style scoped>
h2, h3 {
  text-align: center;
}

main {
  height: 400px;
  overflow-y: auto;
}

.post-card-container {
  display: flex;
  flex-flow: column wrap;
  gap: 0.8em;
}

.list-enter-active,
.list-leave-active {
  transition: all var(--loading-timing) ease-out;
}

.list-enter-from,
.list-leave-to {
  transform: translateY(5em);
  opacity: 0;
}
</style>
