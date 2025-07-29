<script setup lang="ts">
import { Post } from "../model/post";
import PostContent from "./PostContent.vue";
import { printDate } from "../model/formatDate";
import "../assets/common.css";

const props = defineProps<{ post: Post }>();

</script>

<template>
  <div>
      <h1>{{post.title}}</h1>
      <b class="date">📅 {{printDate(post.creationDate)}}</b>
      <article>
        <PostContent :content="post.content"/>
      </article>
  </div>
</template>

<style scoped>

.date {
  color: var(--date-fg);
}
</style>
