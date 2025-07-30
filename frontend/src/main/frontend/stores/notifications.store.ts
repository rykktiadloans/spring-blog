import { defineStore } from "pinia";
import type { Ref } from "vue";
import { ref } from "vue";
import { NotificationType, type Notification } from "../model/notification";

export const useNotificationsStore = defineStore("notifications", () => {
  const notifications: Ref<Notification[]> = ref([ ]);

  const notify = (text: string, type: NotificationType) => {
    const n: Notification = {
      text: text,
      timeToLiveMs: 5000,
      type: type,
    };
    notifications.value.push(n);

    setTimeout(() => {
      notifications.value.shift();
    }, n.timeToLiveMs);
  };

  return { notify, notifications };
});
