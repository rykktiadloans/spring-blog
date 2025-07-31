/**
 * Possible roles of a user
 */
export const enum UserRole {
  USER = "user",
  OWNER = "owner"

}

/**
 * User credentials
 */
export interface UserCredentials {
  /**
   * User's username
   */
  username: string;

  /**
   * Password to log in as a user
   */
  password: string;
}

/**
 * A class representing a user
 */
export class User {

  /**
   * User's username
   */
  username: string;

  /**
   * Password of a user
   */
  password: string;

  /**
   * Role of the user
   */
  role: UserRole;

  /**
   * @constructor
   * @param username - User's username
   * @param password - User's password
   * @param role - User's role
   */
  constructor(username: string, password: string, role: UserRole) {
    this.username = username;
    this.password = password;
    this.role = role;
  }
}
