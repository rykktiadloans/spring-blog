import { formatDate } from "@vueuse/core";

export function printDate(date: Date): string {
  return formatDate(date, "D MMMM YYYY, HH:mm");
}
