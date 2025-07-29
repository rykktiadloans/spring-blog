<script setup lang="ts">
import "../assets/common.css";
</script>

<template>
  <div class="card">
    <slot></slot>
  </div>
</template>

<style scoped>
.card {
  border: var(--card-error-border);
  border-radius: var(--card-border-radius);
  padding: 1em;
  transition: box-shadow var(--card-hover-timing) ease-out,
    background-color var(--card-hover-timing) ease-out;
}

.card:hover {
  background-color: var(--card-bg-hover);
  box-shadow: 0px 0px 0.5em var(--card-error);
  transition: box-shadow var(--card-hover-timing) ease-out,
    background-color var(--card-hover-timing) ease-out;

}
</style>
