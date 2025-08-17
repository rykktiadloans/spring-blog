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
import { useNotificationsStore } from "../stores/notifications.store";
import { NotificationType } from "../model/notification";

const router = useRouter();
const store = useRepositoriesStore();
const resourceRepository = store.resourceRepository;

const {notify} = useNotificationsStore();

if (store.user == null) {
  router.push("/");
}

const resources: Ref<string[]> = ref([]);
const page = ref(0);
const isProcessing = ref(true);
const stopLoading = ref(false);
const resourceList = useTemplateRef("resourceList");
const fileInput = useTemplateRef("fileInput");

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

let newFile = () => {
  const input = fileInput.value;
  if (input == null) {
    return;
  }
  input.onchange = async () => {
    const files = input.files;
    if (files == null) {
      throw new Error("No files selected");
    }

    const resource = await resourceRepository.postImage(files[0]);
    if(resource == null) {
      const text = `${name} is too large!`;
      notify(text, NotificationType.ERROR);
      throw new Error(text);
    }
  };
  input.click();

};
</script>

<template>
  <main ref="resourceList" >
    <form action="/api/v1/resources" class="file-form">
      <input type="submit" value="Send new file" @click.prevent="newFile"/>
      <input type="file" ref="fileInput" style="display: none" />
    </form>
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

input {
  background-color: var(--form-input-bg);
  border: 2px solid var(--form-input-border);
  border-radius: 5px;
  color: var(--form-input-fg);
  font-size: 1.5em;
  padding: 0.5em;
  width: fit-content;
  transition: box-shadow var(--link-glow-timing) ease-out;
}
input:hover {
  box-shadow: 0px 0px 1em var(--link-fg-hover);
}

.file-form {
  display: flex;
  flex-flow: row wrap;
  justify-content: center;
  width: 100%;
  margin: 2em;
}
</style>
