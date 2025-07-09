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

function getAllImages(content: string): string[] {
  const images: string[] = [];

  const matches = content.matchAll(/!\[(.*)\]\((.+)\)/g);

  for (const match of matches) {
    images.push(match[2]);
  }

  return images;
}

async function validateImage(imageUrl: string): Promise<boolean> {
  const response = await fetch(imageUrl);
  if (!response.ok) {
    return false;
  }
  return true;
}

function getImageName(imageUrl: string): string {
  const pos = imageUrl.lastIndexOf("/");
  return imageUrl.slice(pos + 1);
}

const router = useRouter();
const route = useRoute();
const repositories = useRepositoriesStore();

if (repositories.user == null) {
  router.push("/");
}

const id = ref(0);
const title = ref("");
const content = ref("");
const badImages: Ref<string[]> = ref([]);

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
  badImages.value = images.filter((image) => validateImage(image));
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
    <form action="/api/v1/posts" method="post">
      <label for="title">Title</label>
      <input v-model="title" type="text" name="title" id="title" />
      <label for="content">Content</label>
      <textarea v-model="content" name="content" id="content"> </textarea>

      <PostComponent :post="post" />

      <div v-for="image in badImages" :key="image">
        <p>Image "{{ getImageName(image) }}" does not exist.</p>
      </div>
      <p v-if="badImages.length != 0">
        Remember: an image can be accessed with "/resources/{image name}"
      </p>

      <button @click.prevent="validateImages">Validate images</button>
      <input type="submit" value="Publish" @click.prevent="onPublishClick" />
      <input type="submit" value="Save/Move to Draft" @click.prevent="onDraftClick" />
    </form>
  </main>
</template>
