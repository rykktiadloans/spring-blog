import { Post } from "../model/post";
import StatusCodes from "../model/statusCodes";

/**
 * A class for accessing the posts API
 */
export class PostRepository {

  /**
   * A JWT token currently in use
   * */
  jwtToken: string | null;

  /**
   * A constructor for the object
   * @constructor
   * @param jwtToken - Optional JWT token
   */
  constructor(jwtToken?: string) {
    if (jwtToken == null) {
      this.jwtToken = null;
    }
    this.jwtToken = jwtToken!;
  }

  /**
   * A function for fetching a post. Can only fetch published posts without a JWT token
   * @param id - ID of the post
   * @returns Post promise
   */
  async fetchPostById(id: number): Promise<Post> {
    if (this.jwtToken != null) {
      return this.fetchAnyPostById(id);
    }
    let post = await fetch(`/api/v1/posts/${id}`)
      .then((response) => {
        if (!response.ok) {
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

  /**
   * An internal function for fetching post with any state.
   * @param id - ID of the post
   * @returns Post promise
   */
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


  /**
   * Fetch a list of posts on a specific page. Can only fetch published posts if the JWT token is
   * absent
   * @param page - Page to request
   * @returns Promise of a list of posts
   */
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

  /**
   * Fetches posts of all states by their page
   * @param page - Page to request
   * @returns Promise of a list of posts
   */
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

  /**
   * Send a list of posts to the API. If the id is set, then PUTs it, uses POST otherwise
   * @param post - Post to send
   * @returns An updated post
   */
  async sendPost(post: Post): Promise<Post | null> {
    if (this.jwtToken == null) {
      throw new Error("No JWT token has been provided");
    }
    const method = post.id == 0 ? "POST" : "PUT";
    const newPost = await fetch("/api/v1/posts", {
      method: method,
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${this.jwtToken}`,
      },
      body: JSON.stringify(post),
    })
      .then((response) => {
      if (response.status == StatusCodes.BAD_REQUEST) {
        return null;
      }
      return response.json();
    });
    if (newPost == null) {
      return null;
    }
    return Post.deserialize(newPost);
  }
}
