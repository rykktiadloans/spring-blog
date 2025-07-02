import { Post } from "../model/post";

export default class PostRepository {
  jwtToken: string | null;

  constructor(jwtToken?: string, csrf?: string) {
    if (jwtToken == null) {
      this.jwtToken = null;
    }
    this.jwtToken = jwtToken!;

  }

  async fetchPostById(id: number): Promise<Post> {
    let post = await fetch(`/api/v1/posts/${id}`)
      .then((response) => response.json())
      .then((json) => Post.deserialize(json))
      .catch((error) => console.log(error));
    if (post == null) {
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

  async sendPost(post: Post): Promise<Post> {
    if (this.jwtToken == null) {
      throw new Error("No JWT token has been provided");
    }
    let newPost = await fetch("/api/v1/posts", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${this.jwtToken}`
      },
      body: JSON.stringify(post),
    }).then((response) => {
      return response.json();
    })
     .then((json) => Post.deserialize(json));
    return newPost;
  }
}
