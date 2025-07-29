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
import "../assets/common.css";

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
  try {
    post.value = await postRepository.fetchPostById(postId);
  }
  catch(e) {
    router.push("/404");
  }
});

let canEdit = computed(() => jwtToken != null);

let edit = () => {
  router.push(`/owner/newpost?post=${postId}`);
};
</script>

<template>
  <main>
    <Loading v-if="post == null" />
    <Transition name="slide">
      <div v-if="post != null">
        <button v-if="canEdit" @click="edit">Edit</button>
        <PostComponent :post="post" />
      </div>
    </Transition>
  </main>
</template>

<style scoped>
main {
  height: 400px;
  overflow-y: auto;
}

.slide-enter-active,
.slide-leave-active {
  transition: all var(--loading-timing) ease-out;
}

.slide-enter-from,
.slide-leave-to {
  transform: translateY(5em);
  opacity: 0;
}
</style>
