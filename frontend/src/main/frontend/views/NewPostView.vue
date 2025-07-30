<script setup lang="ts">
import { ref, type Ref } from "vue";
import { useRepositoriesStore } from "../stores/repositories.store";
import { useRoute, useRouter } from "vue-router";
import { Post } from "../model/post";
import PostComponent from "../components/PostComponent.vue";
import { computed } from "vue";
import { onMounted } from "vue";
import { useTemplateRef } from "vue";
import "../assets/common.css";
import Card from "../components/Card.vue";
import CardError from "../components/ErrorCard.vue";
import { useTitle } from "@vueuse/core";
import { useNotificationsStore } from "../stores/notifications.store";
import { NotificationType } from "../model/notification";

const router = useRouter();
const route = useRoute();
const repositories = useRepositoriesStore();
const resourceRepository = repositories.resourceRepository;
const { notify } = useNotificationsStore();


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
const errors: Ref<string[]> = ref([]);

const pageTitle = useTitle(`New: ${title.value}`)

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

const validateInput = async () => {
  errors.value = [];
  if(title.value.length > 255) {
    errors.value.push("Title is too long. Max length is 255");
  }
  if(content.value.length > 8192) {
    errors.value.push("Content section is too long. Max length is 8192");
  }
};

const onUploadClick = async (imageName: string) => {
  const input = fileInput.value;
  if (input == null) {
    return;
  }
  input.onchange = async () => {
    const files = input.files;
    if (files == null) {
      throw new Error("No files selected");
    }
    if (files[0].name != imageName.split("/").pop()) {
      const text = `Wrong filename. ${imageName} is needed, while ${files[0].name} is supplied`;
      notify(text, NotificationType.ERROR);
      throw new Error(text);
    }
    const name = imageName.split("/").pop();
    if (name == undefined) {
      return;
    }

    const resource = await resourceRepository.postImage(files[0]);
    if(resource == null) {
      const text = `${name} is too large!`;
      notify(text, NotificationType.ERROR);
      throw new Error(text);
    }
    await validateImages();
  };
  input.click();
};

const onPublishClick = async () => {
  await validateImages();
  await validateInput();
  if (badImages.value.length > 0 || errors.value.length > 0) {
    return;
  }
  const newPost = await repositories.postRepository.sendPost(
    new Post(id.value, title.value, content.value, new Date(Date.now()), "PUBLISHED"),
  );
  if (newPost != null) {
    router.push(`/posts/${newPost.id}`);
  }
};

const onDraftClick = async () => {
  await validateInput();
  if (errors.value.length > 0) {
    return;
  }
  const newPost = await repositories.postRepository.sendPost(
    new Post(id.value, title.value, content.value, new Date(Date.now()), "DRAFT"),
  );
  if(newPost != null) {
    router.push(`/posts/${newPost.id}`);
  }
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
        <Card v-for="image in badImages" :key="image">
          <p>Image "{{ getImageName(image) }}" does not exist.</p>
          <button @click.prevent="onUploadClick(image)">Upload</button>
        </Card>
      </div>
      <div class="bad-image-help" v-if="badImages.length != 0">
        Remember: an image can be accessed with "/resources/{image name}"
      </div>

      <div class="error-container" v-if="errors.length > 0">
        <CardError v-for="error, index in errors" :key="index">
          {{error}}
        </CardError>
      </div>

      <input type="submit" value="Validate images" @click.prevent="validateImages" />
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
  padding: 0.5em;
}

.form {
  border-right: 2px solid var(--newpost-form-bg);
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
  padding: 0.5em;
  color: var(--warn)
}

input, textarea, button{
  background-color: var(--form-input-bg);
  border: 2px solid var(--form-input-border);
  border-radius: 5px;
  color: var(--form-input-fg);
}

input[type=submit] {
  padding: 0.4em;
}

</style>
