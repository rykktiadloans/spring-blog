<script setup lang="ts">
import { Post } from "../model/post";
import PostContent from "./PostContent.vue";

const props = defineProps<{ post: Post }>();

</script>

<template>
  <div>
      <h1>{{post.title}}</h1>
      <b>{{post.creationDate}}</b>
      <article>
        <PostContent :content="post.content"/>
      </article>
  </div>
</template>
