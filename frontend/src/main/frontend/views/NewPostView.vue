<script setup lang="ts">
import { ref, type Ref } from "vue";
import { useRepositoriesStore } from "../stores/repositories.store";
import { useRoute, useRouter } from "vue-router";
import PostContent from "../components/PostContent.vue";
import { watch } from "vue";
import { Post } from "../model/post";
import PostComponent from "../components/PostComponent.vue";
import { computed } from "vue";
import { onMounted } from "vue";
import StatusCodes from "../model/statusCodes";
import { useFileDialog } from "@vueuse/core";
import { templateRef } from "@vueuse/core";
import { useTemplateRef } from "vue";

const router = useRouter();
const route = useRoute();
const repositories = useRepositoriesStore();
const resourceRepository = repositories.resourceRepository;

function getAllImages(content: string): string[] {
  const images: string[] = [];

  const matches = content.matchAll(/!\[(.*)\]\((.+)\)/g);

  for (const match of matches) {
    images.push(match[2]);
  }

  return images;
}

async function validateImage(imageUrl: string): Promise<boolean> {
  const r = await resourceRepository.checkResourceExistsByName(imageUrl);
  return r;
}

function getImageName(imageUrl: string): string {
  const pos = imageUrl.lastIndexOf("/");
  return imageUrl.slice(pos + 1);
}

if (repositories.user == null) {
  router.push("/");
}

const id = ref(0);
const title = ref("Sample title");
const content = ref(`
*Lorem ipsum* and so on.

As you can see, this thing supports Markdown.
`);
const badImages: Ref<string[]> = ref([]);
const fileInput = useTemplateRef("fileInput");

onMounted(async () => {
  if (typeof route.query["post"] == "string") {
    id.value = parseInt(route.query["post"]);
    const post = await repositories.postRepository.fetchPostById(id.value);
    title.value = post.title;
    content.value = post.content;
  }
});

const post = computed(
  () => new Post(id.value, title.value, content.value, new Date(Date.now()), "DRAFT"),
);

const validateImages = async () => {
  const images = getAllImages(content.value);
  badImages.value = [];
  for (const image of images) {
    if (!(await validateImage(image))) {
      badImages.value.push(image);
    }
  }
};

const onUploadClick = async (imageName: string) => {
  const input = fileInput.value;
  if (input == null) {
    return;
  }
  input.onchange = () => {
    const files = input.files;
    if (files == null) {
      throw new Error("No files selected");
    }
    if (files[0].name != imageName.split("/").pop()) {
      throw new Error(`Wrong filename. ${imageName} is needed, while ${files[0].name} is supplied`);
    }
    const name = imageName.split("/").pop();
    if (name == undefined) {
      return;
    }
    resourceRepository.postImage(files[0]);
  };
  input.click();
};

const onPublishClick = async () => {
  await validateImages();
  if (badImages.value.length != 0) {
    return;
  }
  const newPost = await repositories.postRepository.sendPost(
    new Post(id.value, title.value, content.value, new Date(Date.now()), "PUBLISHED"),
  );
  router.push(`/posts/${newPost.id}`);
};

const onDraftClick = async () => {
  const newPost = await repositories.postRepository.sendPost(
    new Post(id.value, title.value, content.value, new Date(Date.now()), "DRAFT"),
  );
  router.push(`/posts/${newPost.id}`);
};
</script>

<template>
  <main>
    <form class="form" action="/api/v1/posts" method="post">
      <div class="form-item">
        <label for="title">Title</label>
        <input v-model="title" type="text" name="title" id="title" />
      </div>
      <div class="form-item">
        <label for="content">Content</label>
        <textarea v-model="content" rows="10" name="content" id="content"> </textarea>
      </div>


      <div class="bad-image-container">
        <div class="bad-image" v-for="image in badImages" :key="image">
          <p>Image "{{ getImageName(image) }}" does not exist.</p>
          <button @click.prevent="onUploadClick(image)">Upload</button>
        </div>
      </div>
      <div class="bad-image-help" v-if="badImages.length != 0">
        Remember: an image can be accessed with "/resources/{image name}"
      </div>

      <button @click.prevent="validateImages">Validate images</button>
      <input type="submit" value="Publish" @click.prevent="onPublishClick" />
      <input type="submit" value="Save/Move to Draft" @click.prevent="onDraftClick" />


      <input type="file" ref="fileInput" style="display: none" />
    </form>
    <PostComponent class="content" :post="post" />
  </main>
</template>

<style scoped>
main {
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-template-rows: 1fr;
  grid-template-areas: "form content";
  column-gap: 15px;
}

.form, .content {
  border: 2px solid black;
  padding: 0.5em;
}

.content {
  overflow-y: scroll;
}

.form, .bad-image-container {
  display: flex;
  flex-flow: column nowrap;
  row-gap: 1em;
}

.form-item {
  display: flex;
  flex-flow: column nowrap;
}

.bad-image {
  border-top: 2px solid black;
  border-bottom: 2px solid black;
  padding: 0.5em;
}

.bad-image-help {
  border: 2px solid red;
  border-radius: 15px;
  padding: 0.5em;
}

</style>
