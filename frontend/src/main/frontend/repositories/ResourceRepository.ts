import StatusCodes from "../model/statusCodes";

/**
 * A repository object for accessing the resources API
 */
export class ResourceRepository {

  /**
   * A JWT token used for authorization
   * */
  jwtToken: string | null;

  /**
   * @constructor
   * @param jwtToken - An optional JWT token
   */
  constructor(jwtToken?: string) {
    if (jwtToken == null) {
      this.jwtToken = null;
    }
    this.jwtToken = jwtToken!;
  }

  /**
   * Check if a resource with a specific name exists
   * @param name - Name of the resource
   * @returns Boolean promise
   */
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

  /**
   * A function for uploading a File to the backend
   * @param file - File to upload
   * @returns The name of the new resource.
   */
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
      .then((response) => response.status == StatusCodes.PAYLOAD_TOO_LARGE
        ? null : response.json())
      .catch((error) => console.log(error));

    if(resource == null) {
      return null;
    }
    return resource.name;
  }
}
