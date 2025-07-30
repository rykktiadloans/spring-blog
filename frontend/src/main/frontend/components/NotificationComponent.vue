<script setup lang="ts">
import { computed } from 'vue';
import { type Notification, NotificationType } from '../model/notification';
import "../assets/common.css";

const props = defineProps<{
  notification: Notification;
}>()

const typeClass = computed(() => ({
  error: props.notification.type == NotificationType.ERROR,
}));
</script>

<template>
  <Transition name="fade">
    <div class="notification" :class="typeClass">
      {{notification.text}}
    </div>
  </Transition>
</template>

<style scoped>
.notification {
  background-color: var(--notif-bg);
  border: 2px solid var(--notif-bg);
  border-radius: var(--card-border-radius);
  padding: 1em;
}

.error {
  border: 2px solid var(--error);
  box-shadow: 0px 0px 0.5em var(--error);
}
</style>
