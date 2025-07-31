/**
 * Potential states of a post.
 */
export type PostState = "PUBLISHED" | "DRAFT";

/**
 * A post model
 */
export class Post {
  /**
   * ID of the post
   */
  id: number;

  /**
   * Title of the post
   */
  title: string;

  /**
   * Contents of a post
   */
  content: string;

  /**
   * Creation date of the post
   */
  creationDate: Date;

  /**
   * State of the post
   */
  state: PostState;

  /**
   * @constructor
   * @param id - ID of the post
   * @param title - Title of the post
   * @param content - Content of the post
   * @param creationDate - Creation date of the post
   * @param state - State of the post
   */
  constructor(id: number, title: string, content: string, creationDate: Date, state: PostState) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.creationDate = creationDate;
    this.state = state;
  }

  /**
   * Turn a JSON object into a post
   * @param json - JSON object
   * @returns Post
   */
  static deserialize(json: any): Post {
    return new Post(json.id, json.title, json.content, new Date(json.creationDate), json.state);
  }
}
