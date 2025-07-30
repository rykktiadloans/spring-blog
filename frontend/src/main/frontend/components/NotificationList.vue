<script setup lang="ts">
import { useNotificationsStore } from '../stores/notifications.store';
import NotificationComponent from './NotificationComponent.vue';

const {notify, notifications} = useNotificationsStore();
</script>

<template>
  <div>
    <TransitionGroup class="flex" name="list" tag="NotificationComponent">
      <NotificationComponent v-for="notif in notifications" :key="notif.text"
        :notification="notif"/>
    </TransitionGroup>
  </div>
</template>

<style scoped>
.flex {
  display: flex;
  flex-direction: column;
  gap: 1em;
}

.list-move,
.list-enter-active,
.list-leave-active {
  transition: transform var(--loading-timing) ease-out,
    opacity var(--loading-timing) ease-out,
    border 5s ease-out,
    box-shadow 5s ease-out
    ;
}

.list-enter-from,
.list-leave-to {
  transform: translateX(5em);
  opacity: 0;
}

.list-enter-to,
.list-leave-from {
  border: 2px solid var(--main-fg);
  box-shadow: 0px 0px 0.5em var(--main-fg);
}

</style>
