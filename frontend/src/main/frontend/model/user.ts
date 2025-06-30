export const enum UserRole {
  USER = "user",
  OWNER = "owner"

}

export class User {
  username: string;
  password: string;
  role: UserRole;

  constructor(username: string, password: string, role: UserRole) {
    this.username = username;
    this.password = password;
    this.role = role;
  }
}
