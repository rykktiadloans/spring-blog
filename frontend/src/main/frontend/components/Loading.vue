<script lang="ts" setup>
import { ref } from "vue";
import "../assets/common.css";
import { onMounted } from "vue";

const content = "LOADING...";
let pos = ref(0);
let left = ref(content.slice(0, pos.value));
let char = ref(content[pos.value]);
let right = ref(content.slice(pos.value + 1));

function onStep(): void {
  pos.value++;
  if(pos.value >= content.length) {
    pos.value = 0;
  }
  left.value = content.slice(0, pos.value);
  char.value = content[pos.value];
  right.value= content.slice(pos.value + 1);
}

onMounted(() => {
  setInterval(onStep, 300);
});

</script>

<template>
  <div class="loading-container">
    <h2>
      {{left}}<span class="selected">{{char}}</span>{{right}}
    </h2>
  </div>
</template>

<style scoped>
.loading-container {
  display: flex;
  flex-flow: column wrap;
  align-items: center;
}

.selected {
  color: var(--loading-focus);
}
</style>

