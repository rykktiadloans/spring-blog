<script setup lang="ts">
import { ref, type Ref } from "vue";
import { useRepositoriesStore } from "../stores/repositories.store";
import type { UserCredentials } from "../repositories/JwtRepository";
import { useRouter } from "vue-router";

const repositories = useRepositoriesStore();
const router = useRouter();
const login = repositories.login;
const username = ref("");
const password = ref("");
const isLoading = ref(false);
const error = ref("");

const loginCallback = async () => {
  isLoading.value = true;
  error.value = "";
  const credentials: UserCredentials = {
    username: username.value,
    password: password.value,
  };
  const user = await login(credentials);
  isLoading.value = false;
  if(user === null) {
    error.value = "Invalid credentials!";
  }
  else {
    router.push("/")
  }

};
</script>

<template>
  <main>
    <h2>Login</h2>
    <form>
      <label for="username">Username</label>
      <input type="text" id="username" v-model="username" />
      <label for="password">Password</label>
      <input type="text" id="password" v-model="password" />
      <input type="submit" value="Login" @click.prevent="loginCallback" />
      <p v-if="isLoading">LOADING...</p>
      <p v-if="error.length !== 0">{{ error }}</p>
    </form>
  </main>
</template>
