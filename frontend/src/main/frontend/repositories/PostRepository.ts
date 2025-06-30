import { Post } from "../model/post";

export default class PostRepository {
  jwtToken: string | null;
  csrf: string | null;

  constructor(jwtToken?: string, csrf?: string) {
    if(jwtToken == null) {
      this.jwtToken = null;
    }
    this.jwtToken = jwtToken!;

    if(csrf == null) {
      this.csrf = null;
    }
    this.csrf = csrf!;
  }

  async fetchPostById(id: number): Promise<Post> {
    let post = await fetch(`/api/v1/posts/${id}`)
      .then((response) => response.json())
      .then((json) => Post.deserialize(json))
      .catch((error) => console.log(error));
    if(post == null) {
      throw new Error(`Post with ID ${id} could not be fetched`);
    }
    return post;
  }

  async fetchPostsByPage(page: number): Promise<Post[]> {
    let posts = await fetch(`/api/v1/posts?page=${page}`)
      .then((response) => response.json())
      .then((json) => json.map((el: any) => Post.deserialize(el)))
      .catch((error) => console.log(error));
    return posts;

  }

  sendPost(post: Post): Post {
    if (this.jwtToken == null) {
      throw new Error("No JWT token has been provided");
    }
    if (this.csrf == null) {
      throw new Error("No CSRF token has been provided");
    }
    return post;
  }
}
