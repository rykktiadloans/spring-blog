<script setup lang="ts">
import { ref, type Ref } from "vue";
import { useRepositoriesStore } from "../stores/repositories.store";
import { useRouter } from "vue-router";
import type { UserCredentials } from "../model/user";
import Loading from "../components/Loading.vue";
import "../assets/common.css";
import { useTitle } from "@vueuse/core";

const repositories = useRepositoriesStore();
const router = useRouter();
const login = repositories.login;
const username = ref("");
const password = ref("");
const isLoading = ref(false);
const error = ref("");

const title = useTitle("Login")

const loginCallback = async () => {
  isLoading.value = true;
  error.value = "";
  const credentials: UserCredentials = {
    username: username.value,
    password: password.value,
  };
  const user = await login(credentials);
  isLoading.value = false;
  if (user === null) {
    error.value = "Invalid credentials!";
  } else {
    router.push("/");
  }
};
</script>

<template>
  <main>
    <h2 class="login-title">Login</h2>
    <form class="form">
      <div class="form-item">
        <label for="username">Username</label>
        <input type="text" id="username" v-model="username" />
      </div>
      <div class="form-item">
        <label for="password">Password</label>
        <input type="text" id="password" v-model="password" />
      </div>
      <input type="submit" value="Login" @click.prevent="loginCallback" />
      <p v-if="isLoading"><Loading/></p>
      <p v-if="error.length !== 0">{{ error }}</p>
    </form>
  </main>
</template>

<style scoped>
main,
.form,
.form-item {
  display: flex;
  flex-flow: column nowrap;
}

input{
  background-color: var(--form-input-bg);
  border: 2px solid var(--form-input-border);
  border-radius: 5px;
  color: var(--form-input-fg);
}

input[type=submit] {
  padding: 0.4em;
}

main, .form {
  align-items: center;
}

.form {
  gap: 15px;
}

.login-title {
  text-align: center;
}
</style>
