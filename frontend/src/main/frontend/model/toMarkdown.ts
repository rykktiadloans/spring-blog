import MarkdownIt from "markdown-it";
import { ref } from "vue";

const markdown = ref(MarkdownIt().enable("image"));

export default function renderMarkdown(input: string): string {
  return markdown.value.render(input);
}
