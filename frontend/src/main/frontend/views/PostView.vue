<script setup lang="ts">
import { useRoute, useRouter } from "vue-router";
import { Post } from "../model/post";
import { ref } from "vue";
import type { Ref } from "vue";
import { useRepositoriesStore } from "../stores/repositories.store";
import { onMounted } from "vue";
import Loading from "../components/Loading.vue";
import PostComponent from "../components/PostComponent.vue";
import { computed } from "vue";

const route = useRoute();
const router = useRouter();
if (route.params.id instanceof Array) {
  throw new Error("ID of the post should not be an array");
}
const postId: number = parseInt(route.params.id as string);

const store = useRepositoriesStore();
const postRepository = store.postRepository;
const jwtToken = store.jwtToken;

const post: Ref<Post | null> = ref(null);

onMounted(async () => {
  post.value = await postRepository.fetchPostById(postId);
});

let canEdit = computed(() => jwtToken != null);

let edit = () => {
  router.push(`/owner/newpost?post=${postId}`);
};
</script>

<template>
  <main>
    <Loading v-if="post == null" />
    <template v-else>
      <button v-if="canEdit" @click="edit">Edit</button>
      <PostComponent :post="post" />
    </template>
  </main>
</template>
