import { Post } from "../model/post";
import StatusCodes from "../model/statusCodes";

export default class PostRepository {
  jwtToken: string | null;

  constructor(jwtToken?: string, csrf?: string) {
    if (jwtToken == null) {
      this.jwtToken = null;
    }
    this.jwtToken = jwtToken!;
  }

  async fetchPostById(id: number): Promise<Post> {
    if (this.jwtToken != null) {
      return this.fetchAnyPostById(id);
    }
    let post = await fetch(`/api/v1/posts/${id}`)
      .then((response) => {
        if (response.status == StatusCodes.NOT_FOUND) {
          throw new Error(`Post with ID ${id} could not be fetched`);
        }
        return response.json();
      })
      .then((json) => Post.deserialize(json))
      .catch((error) => console.log(error));
    if (post == null) {
      throw new Error(`Post with ID ${id} could not be fetched`);
    }
    return post;
  }

  private async fetchAnyPostById(id: number): Promise<Post> {
    let post = await fetch(`/api/v1/posts/anystate/${id}`, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${this.jwtToken}`,
      },
    })
      .then((response) => {
        if (response.status == StatusCodes.NOT_FOUND) {
          throw new Error(`Post with ID ${id} could not be fetched`);
        }
        return response.json();
      })
      .then((json) => Post.deserialize(json))
      .catch((error) => console.log(error));
    if (post == null) {
      throw new Error(`Post with ID ${id} could not be fetched`);
    }
    return post;
  }
  async fetchPostsByPage(page: number): Promise<Post[]> {
    if (this.jwtToken != null) {
      return this.fetchAllPostsByPage(page);
    }
    let posts = await fetch(`/api/v1/posts?page=${page}`)
      .then((response) => response.json())
      .then((json) => json.map((el: any) => Post.deserialize(el)))
      .catch((error) => console.log(error));
    return posts;
  }

  private async fetchAllPostsByPage(page: number): Promise<Post[]> {
    if (this.jwtToken == null) {
      throw new Error("No JWT token has been provided");
    }
    let posts = await fetch(`/api/v1/posts/anystate?page=${page}`, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${this.jwtToken}`,
      },
    })
      .then((response) => response.json())
      .then((json) => json.map((el: any) => Post.deserialize(el)))
      .catch((error) => console.log(error));
    return posts;
  }

  async sendPost(post: Post): Promise<Post> {
    if (this.jwtToken == null) {
      throw new Error("No JWT token has been provided");
    }
    const method = post.id == 0 ? "POST" : "PUT";
    let newPost = await fetch("/api/v1/posts", {
      method: method,
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${this.jwtToken}`,
      },
      body: JSON.stringify(post),
    })
      .then((response) => {
        return response.json();
      })
      .then((json) => Post.deserialize(json));
    return newPost;
  }
}
