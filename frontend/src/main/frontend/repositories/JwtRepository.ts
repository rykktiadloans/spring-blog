import StatusCodes from "../model/statusCodes";

export interface UserCredentials {
  username: string;
  password: string;
}

export class JwtRepository {
  private jwtToken: string | null;
  private credentials?: UserCredentials;
  private csrf: string;

  constructor(credentials?: UserCredentials) {
    this.jwtToken = null;
    this.credentials = credentials;
    this.csrf = "";
    fetch("/api/v1/auth/csrf")
      .then((response) => response.json())
      .then((json) => (this.csrf = json.token))
      .then(() => this.fetchJwtToken())
      .catch((error) => console.log(error));
  }

  setCredentials(credentials: UserCredentials): Promise<void> {
    this.credentials = credentials;
    return this.fetchJwtToken();
  }

  getJwtToken(): string | null {
    if (this.credentials == null) {
      return null;
    }
    return this.jwtToken;
  }

  private async fetchJwtToken(): Promise<void> {
    if (this.credentials == null || this.csrf === "") {
      return;
    }

    let jwtToken = await fetch("/api/v1/auth/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "X-CSRF-TOKEn": this.csrf,
      },
      body: JSON.stringify(this.credentials),
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
      .catch((response) => console.log("ERROR: " + response));

    if (jwtToken != null) {
      this.jwtToken = jwtToken;
    }
  }
}
