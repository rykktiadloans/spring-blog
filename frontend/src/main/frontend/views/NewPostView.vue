<script setup lang="ts">
import { ref } from "vue";
import { useRepositoriesStore } from "../stores/repositories.store";
import { useRoute, useRouter } from "vue-router";
import PostContent from "../components/PostContent.vue";
import { watch } from "vue";
import { Post } from "../model/post";
import PostComponent from "../components/PostComponent.vue";
import { computed } from "vue";
import { onMounted } from "vue";

const router = useRouter();
const route = useRoute();
const repositories = useRepositoriesStore();

if (repositories.user == null) {
  router.push("/");
}

const id = ref(0);
const title = ref("");
const content = ref("");

onMounted(async () => {
  console.log(typeof route.query["post"])
  if(typeof route.query["post"] == "string") {
    id.value = parseInt(route.query["post"]);
    const post = await repositories.postRepository.fetchPostById(id.value);
    title.value = post.title;
    content.value = post.content;
  }
})

const post = computed(() =>
  new Post(id.value, title.value, content.value, new Date(Date.now()), "DRAFT"));

const onPublishClick = async () => {
  const newPost = await repositories.postRepository.sendPost(
    new Post(id.value, title.value, content.value, new Date(Date.now()), "PUBLISHED")
  );
  router.push(`/posts/${newPost.id}`);
};

const onDraftClick = async () => {
  const newPost = await repositories.postRepository.sendPost(
    new Post(id.value, title.value, content.value, new Date(Date.now()), "DRAFT")
  );
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

      <input type="submit" value="Publish" @click.prevent="onPublishClick" />
      <input type="submit" value="Save/Move to Draft" @click.prevent="onDraftClick" />
    </form>
  </main>
</template>
