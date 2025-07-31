import { defineStore } from "pinia";
import type { Ref } from "vue";
import { ref } from "vue";
import { NotificationType, type Notification } from "../model/notification";

/**
 * A Pinia store that contains and manages all the currently active notifications.
 * @returns { (text:string, type:NotificationType) => void } notify A function to create and store a new notification.
 * @returns { Ref<Notification[]> }   notifications Collection of all currently active notifications
 */
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
