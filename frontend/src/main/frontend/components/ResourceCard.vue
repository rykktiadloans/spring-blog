<script setup lang="ts">
import { useRouter } from "vue-router";
import { type PostState, Post } from "../model/post";
import { printDate } from "../model/formatDate";
import "../assets/common.css";
import Loading from "./Loading.vue";
import Card from "./Card.vue";
import { ref } from "vue";
import type { Ref } from "vue";
import { useRepositoriesStore } from "../stores/repositories.store";
import { useNotificationsStore } from "../stores/notifications.store";
import { NotificationType } from "../model/notification";
const props = defineProps<{
  name: string;
}>();

const router = useRouter();
const repositories = useRepositoriesStore();
const resourceRepository = repositories.resourceRepository;
const { notify } = useNotificationsStore();

const expanded = ref(false);
const posts: Ref<Post[] | null> = ref(null);


const click = async () => {
  expanded.value = !expanded.value;
  if(posts.value == null) {
    posts.value = await resourceRepository.getPostsOfAResource(props.name);
  }
};

const onDelete = async () => {
  if(posts.value == null || posts.value.length > 0) {
    notify("Resource can't be deleted since there are posts that use it",
      NotificationType.ERROR);
    return;
  }
  let res = await resourceRepository.deleteResource(props.name);
  if(res) {
    router.push("/posts");
  }
  else {
    notify(`Couldn't delete the file '${props.name}'`, NotificationType.ERROR);
  }
}

</script>

<template>
  <Card @click="click">
    <div>
      <a :href="`/resources/${name}`">{{name}}</a>
    </div>
    <Transition name="list">
      <div class="expanded" v-if="expanded">
        <Loading v-if="posts == null"/>
        <div v-if="posts != null">
          <h3 v-if="posts.length > 0">Posts where the image is used:</h3>
          <h3 v-else>This resource is never used</h3>
          <ul>
            <li v-for="post in posts">
              <a :href="`/posts/${post.id}`">{{post.title}}</a>
            </li>
          </ul>
          <button @click.prevent.stop="onDelete" class="delete" :class="{enabled: posts.length < 1}">Delete</button>
        </div>
      </div>
    </Transition>
  </Card>
</template>

<style scoped>

.list-enter-active,
.list-leave-active {
  transition: all var(--loading-timing) ease-out;
}

.list-enter-from,
.list-leave-to {
  transform: translateY(-1em);
  opacity: 0;
}

.delete {
  border-radius: 5px;
  color: var(--delete-fg);
  background-color: var(--delete-disable-bg);
  border: 2px solid var(--delete-disable-bg);

}
.delete.enabled{
  background-color: var(--delete-bg);
  border: 2px solid var(--delete-bg);
}

</style>
