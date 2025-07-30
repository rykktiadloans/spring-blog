<script setup lang="ts">
import { computed } from "vue";
import renderMarkdown from "../model/toMarkdown";
const props = defineProps<{content: string}>();
const render = computed(() => renderMarkdown(props.content));
import "../assets/common.css";

</script>

<template>
  <div class="content" v-html="render"></div>
</template>

<style>
.content {
  height: fit-content;
}

img {
  max-width: 60vh;
  display: block;
  margin: auto;
  border-radius: var(--image-radius);
}

a:any-link {
  color: var(--link-fg);
  text-decoration: none;
  transition: text-shadow var(--link-glow-timing) ease-out;
}

a:any-link:hover {
  color: var(--link-fg-hover);
  text-shadow: 0px 0px 1em var(--link-fg-hover);
  text-decoration: none;
  transition: text-shadow var(--link-glow-timing) ease-out;
}
</style>
