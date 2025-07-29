<script setup lang="ts">
import { onMounted, ref, type Ref } from "vue";
import { Post } from "../model/post";
import { useRepositoriesStore } from "../stores/repositories.store";
import Loading from "../components/Loading.vue";
import PostCard from "../components/PostCard.vue";
import { useInfiniteScroll, useWindowScroll, useWindowSize } from "@vueuse/core";
import { watch } from "vue";
import { useTemplateRef } from "vue";

const store = useRepositoriesStore();
const postRepository = store.postRepository;

const posts: Ref<Post[]> = ref([]);
const page = ref(0);
const isProcessing = ref(true);
const stopLoading = ref(false);
const postList = useTemplateRef("postList");

let pushNewPosts = async () => {
  isProcessing.value = true;
  const tempPosts = await postRepository.fetchPostsByPage(page.value);
  page.value++;
  posts.value.push(...tempPosts);
  isProcessing.value = false;
  if (tempPosts.length < 20) {
    stopLoading.value = true;
  }
};

const { reset } = useInfiniteScroll(
  postList,
  async () => {
    await pushNewPosts();
  },
  {
    distance: 200,
    canLoadMore: () => !stopLoading.value,
  },
);
</script>

<template>
  <main ref="postList" >
    <div>
      <TransitionGroup class="post-card-container" name="list" tag="PostCard">
        <PostCard v-for="post in posts" :key="post.id" :post="post" />
      </TransitionGroup>
      <center>
        <h2 v-if="!isProcessing"> That's it! </h2>
      </center>
    </div>
    <Loading v-if="isProcessing" />
  </main>
</template>

<style scoped>
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
