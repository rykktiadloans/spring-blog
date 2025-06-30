export type PostState = "PUBLISHED" | "DRAFT";

export class Post {
  id: number;
  title: string;
  content: string;
  creationDate: Date;
  state: PostState;

  constructor(id: number, title: string, content: string, creationDate: Date, state: PostState) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.creationDate = creationDate;
    this.state = state;
  }

  static deserialize(json: any): Post {
    return new Post(json.id, json.title, json.content, new Date(json.creationDate), json.state);
  }
}
