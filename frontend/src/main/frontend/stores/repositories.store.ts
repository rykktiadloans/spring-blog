import StatusCodes from "../model/statusCodes";
import { User, UserRole } from "../model/user";
import { type UserCredentials } from "../repositories/JwtRepository";
import PostRepository from "../repositories/PostRepository";
import { defineStore } from "pinia";
import { onMounted, ref, watch, type Ref } from "vue";

export const useRepositoriesStore = defineStore("repositories", () => {
  const postRepository: Ref<PostRepository> = ref(new PostRepository());
  const jwtToken: Ref<string | null> = ref(null);
  const user: Ref<User | null> = ref(null);


  watch(jwtToken, (newValue) => {
    postRepository.value.jwtToken = newValue;
  });

  let login = async (credentials: UserCredentials): Promise<User | null> => {
    let tempToken = await fetch("/api/v1/auth/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(credentials),
    })
      .then((response) => {
        if (response.ok) {
          return response.text();
        }
        if (response.status == StatusCodes.UNAUTHORIZED) {
          console.log("Incorrect credentials");
        }
        throw new Error(`Can't authenticate. HTTP status: ${response.status}`);
      })
      .catch((response) => {
        console.log("ERROR: " + response);
        return null;
      });

    if (tempToken == null) {
      return null;
    }
    jwtToken.value = tempToken;
    user.value = new User(credentials.username, credentials.password, UserRole.OWNER);

    return user.value;
  };

  return { postRepository, jwtToken, user, login};
});

