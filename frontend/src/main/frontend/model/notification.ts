/**
 * An enum of possible notification types
 */
export const enum NotificationType {
  ERROR
}

/**
 * An interface for notifications
 */
export interface Notification {

  /**
   * Contents of a notification
   */
  text: string;

  /**
   * How much time a notification has left to live
   */
  timeToLiveMs: number;

  /**
   * The type of the notification
   */
  type: NotificationType
}
