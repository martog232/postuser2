import { Comment } from "../comment.model";
import { Post } from "../post.model";

export interface User {
    id?: number;
    username: string;
    email: string;
    password: string;
    posts: Post[];
    likedPosts: Post[];
    likedComments: Comment[];
    isConfirmed: boolean;
}