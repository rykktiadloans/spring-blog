<script setup lang="ts">
import { onMounted, ref, type Ref } from "vue";
import { Post } from "../model/post";
import { useRepositoriesStore } from "../stores/repositories.store";
import Loading from "../components/Loading.vue";
import PostCard from "../components/PostCard.vue";
import { useInfiniteScroll, useTitle, useWindowScroll, useWindowSize } from "@vueuse/core";
import { watch } from "vue";
import { useTemplateRef } from "vue";
import { useRouter } from "vue-router";
import ResourceCard from "../components/ResourceCard.vue";

const router = useRouter();
const store = useRepositoriesStore();
const resourceRepository = store.resourceRepository;

if (store.user == null) {
  router.push("/");
}

const resources: Ref<string[]> = ref([]);
const page = ref(0);
const isProcessing = ref(true);
const stopLoading = ref(false);
const resourceList = useTemplateRef("resourceList");

const title = useTitle("Resources");

let pushNewResources = async () => {
  isProcessing.value = true;
  const tempResources = await resourceRepository.getResourcesByPage(page.value);
  page.value++;
  resources.value.push(...tempResources);
  isProcessing.value = false;
  if (tempResources.length < 20) {
    stopLoading.value = true;
  }
};

const { reset } = useInfiniteScroll(
  resourceList,
  async () => {
    await pushNewResources();
  },
  {
    distance: 200,
    canLoadMore: () => !stopLoading.value,
  },
);
</script>

<template>
  <main ref="resourceList" >
    <div>
      <TransitionGroup class="post-card-container" name="list" tag="Card">
        <ResourceCard v-for="resource in resources" :name="resource" :key="resource" />
      </TransitionGroup>
      <center>
        <h2 v-if="!isProcessing"> That's it! </h2>
      </center>
    </div>
    <Loading v-if="isProcessing" />
  </main>
</template>

<style scoped>
main {
  height: 400px;
  overflow-y: auto;
}

.post-card-container {
  display: flex;
  flex-flow: column wrap;
  gap: 0.8em;
}

.list-enter-active,
.list-leave-active {
  transition: all var(--loading-timing) ease-out;
}

.list-enter-from,
.list-leave-to {
  transform: translateY(5em);
  opacity: 0;
}
</style>
