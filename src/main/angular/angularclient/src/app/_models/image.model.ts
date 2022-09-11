import { Post } from "./post.model";

export interface Image {
    id?: number;
    url: string;
    post: Post;
}