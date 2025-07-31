import { formatDate } from "@vueuse/core";

/**
 * A function to format a date into an appropriate string
 * @param date - Date to format
 * @returns Formatted date
 */
export function printDate(date: Date): string {
  return formatDate(date, "D MMMM YYYY, HH:mm");
}
