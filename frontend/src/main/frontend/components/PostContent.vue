<script setup lang="ts">
import { computed } from "vue";
import renderMarkdown from "../model/toMarkdown";
const props = defineProps<{content: string}>();
const render = computed(() => renderMarkdown(props.content));

</script>

<template>
  <div v-html="render"></div>
</template>
