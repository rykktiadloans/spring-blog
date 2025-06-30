<script setup lang="ts">
import { onMounted, ref, type Ref } from "vue";
import { Post } from "../model/post";
import { useRepositoriesStore } from "../stores/repositories.store";
import Loading from "../components/Loading.vue";
import PostCard from "../components/PostCard.vue";

const store = useRepositoriesStore();
const postRepository = store.postRepository;

const posts: Ref<Post[]> = ref([]);
const page = ref(1);
const isProcessing = ref(true);

onMounted(async () => {
  const tempPosts = await postRepository.fetchPostsByPage(page.value);
  page.value++;
  posts.value.push(...tempPosts);
  isProcessing.value = false;
});

</script>

<template>
  <main>
    <Loading v-if="isProcessing" />
    <PostCard v-else v-for="post in posts" :key="post.id" :post="post" />
  </main>
</template>
