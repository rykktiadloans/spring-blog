import { Post } from "../model/post";
import StatusCodes from "../model/statusCodes";

export class ResourceRepository {
  jwtToken: string | null;

  constructor(jwtToken?: string, csrf?: string) {
    if (jwtToken == null) {
      this.jwtToken = null;
    }
    this.jwtToken = jwtToken!;
  }

  async checkResourceExistsByName(name: string): Promise<boolean> {
    if (!name.startsWith("/resources/")) {
      throw new Error("Name should have the form of '/resources/name.ext'");
    }
    const resource = await fetch(name)
      .then((response) => {
        if (response.status == StatusCodes.NOT_FOUND) {
          return false;
        }
        return true;
      })
      .catch((error) => {
        console.log(error);
        return false;
      });
    return resource;
  }

  async getResourcesByPage(page: number): Promise<string[]> {
    if (this.jwtToken == null) {
      throw new Error("No JWT token has been provided");
    }
    return await fetch(`/api/v1/resources/list?page=${page}`, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${this.jwtToken}`,
      },
    })
      .then((response) => response.json())
      .then((json) => json.map((res: any) => res.name));
  }

  async getPostsOfAResource(name: string): Promise<Post[]> {
    return await fetch(`/api/v1/resources/${name}/posts`)
      .then((response) => response.json())
      .then((json) => json.map((res: any) => Post.deserializeFromSimple(res)));
  }

  async postImage(file: File): Promise<string | null> {
    if (this.jwtToken == null) {
      throw new Error("No JWT token has been provided");
    }
    const data = new FormData();
    data.append("file", file);
    const resource = await fetch(`/api/v1/resources`, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${this.jwtToken}`,
      },
      body: data,
    })
      .then((response) =>
        response.status == StatusCodes.PAYLOAD_TOO_LARGE ? null : response.json(),
      )
      .catch((error) => console.log(error));

    if (resource == null) {
      return null;
    }
    return resource.name;
  }

  async deleteResource(name: string): Promise<boolean> {
    if (this.jwtToken == null) {
      throw new Error("No JWT token has been provided");
    }
    const resource = await fetch(`/api/v1/resources/${name}`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${this.jwtToken}`,
      },
    })
      .then((response) => response.json())
      .then((json) => {console.log(json); return json as boolean})
      .catch((error) => {
        console.log(error);
        return false;
      });

    return resource;
  }
}
