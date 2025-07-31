import MarkdownIt from "markdown-it";
import { ref } from "vue";

const markdown = ref(MarkdownIt().enable("image"));

/**
 * Render a string with a Markdown document into HTML
 * @param input - Markdown document
 * @returns HTML document
 */
export default function renderMarkdown(input: string): string {
  return markdown.value.render(input);
}
