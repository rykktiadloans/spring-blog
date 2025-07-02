<script setup lang="ts">
import { ref } from "vue";
import { useRepositoriesStore } from "../stores/repositories.store";
import { useRouter } from "vue-router";
import PostContent from "../components/PostContent.vue";
import { watch } from "vue";
import { Post } from "../model/post";
import PostComponent from "../components/PostComponent.vue";
import { computed } from "vue";

const router = useRouter();
const repositories = useRepositoriesStore();

if (repositories.user == null) {
  console.log("LOSER");
  router.push("/");
}

const title = ref("");
const content = ref("");

const post = computed(() =>
  new Post(0, title.value, content.value, new Date(Date.now()), "DRAFT"));

const onClick = async () => {
  const newPost = await repositories.postRepository.sendPost(post.value);
  router.push(`/posts/${newPost.id}`);
};
</script>

<template>
  <main>
    <form action="/api/v1/posts" method="post">
      <label for="title">Title</label>
      <input v-model="title" type="text" name="title" id="title" />
      <label for="content">Content</label>
      <textarea v-model="content" name="content" id="content"> </textarea>

      <PostComponent :post="post" />

      <input type="submit" value="Post" @click.prevent="onClick" />
    </form>
  </main>
</template>
