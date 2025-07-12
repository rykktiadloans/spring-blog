<script setup lang="ts">
import { useRouter } from "vue-router";
import { type PostState, Post } from "../model/post";
import { printDate } from "../model/formatDate";
const props = defineProps<{
  post: Post;
}>();

const router = useRouter();


const click = () => {
  router.push({ path: `/posts/${props.post.id}` });
};
</script>

<template>
  <div @click="click">
    <h3>{{post.state == "DRAFT" ? "DRAFT: " : ""}}{{post.title}}</h3>
    <p>{{post.content.slice(0, 50)}}...</p>
    <p>
      <b>{{printDate(post.creationDate)}}</b>
    </p>
  </div>
</template>

<style scoped>
</style>
