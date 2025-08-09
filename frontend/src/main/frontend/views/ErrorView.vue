<script setup lang="ts">
import { useTitle } from '@vueuse/core';
import { useRoute } from 'vue-router';

const route = useRoute();

let code = "400";
let message = "There seems to be an issue";

if (typeof route.query["code"] == "string") {
  code = route.query["code"];
}

if (typeof route.query["message"] == "string") {
  message = route.query["message"];
}

const title = useTitle(`Oops! ${message}`);


</script>

<template>
  <main>
    <h1>Oops! There was an error with code <em>{{code}}</em></h1>
    <h2>The error is <em>{{message}}</em></h2>
  </main>
</template>
