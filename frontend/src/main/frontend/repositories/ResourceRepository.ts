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

  async postImage(file: File): Promise<string> {
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
      .then((response) => response.json())
      .then((json) => json.name)
      .catch((error) => console.log(error));
    return resource;
  }
}
