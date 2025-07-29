<script setup lang="ts">
import { useRouter } from "vue-router";
import { type PostState, Post } from "../model/post";
import { printDate } from "../model/formatDate";
import "../assets/common.css";
import Card from "./Card.vue";
const props = defineProps<{
  post: Post;
}>();

const router = useRouter();


const click = () => {
  router.push({ path: `/posts/${props.post.id}` });
};
</script>

<template>
  <Card @click="click">
    <h3>{{post.state == "DRAFT" ? "DRAFT: " : ""}}{{post.title}}</h3>
    <p class="content">{{post.content.slice(0, 50)}}...</p>
    <p class="date">
      <b>📅 {{printDate(post.creationDate)}}</b>
    </p>
  </Card>
</template>

<style scoped>
.date, .content {
  color: var(--date-fg);
}
</style>
