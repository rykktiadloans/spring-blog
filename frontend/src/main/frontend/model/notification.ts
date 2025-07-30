export const enum NotificationType {
  ERROR
}

export interface Notification {
  text: string;
  timeToLiveMs: number;
  type: NotificationType
}
