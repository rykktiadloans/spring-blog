export type PostState = "PUBLISHED" | "DRAFT";

export class Post {
  id: number;
  title: string;
  content: string;
  summary: string;
  creationDate: Date;
  state: PostState;

  constructor(
    id: number,
    title: string,
    content: string,
    summary: string,
    creationDate: Date,
    state: PostState,
  ) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.creationDate = creationDate;
    this.summary = summary;
    this.state = state;
  }

  static deserialize(json: any): Post {
    return new Post(
      json.id,
      json.title,
      json.content,
      json.summary,
      new Date(json.creationDate),
      json.state,
    );
  }

  static deserializeFromSimple(json: any): Post {
    return new Post(json.id, json.title, "", "", new Date(), "DRAFT");
  }
}
