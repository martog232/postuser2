import { Comment } from "../comment.model";
import { Group } from "../group.model";
import { Post } from "../post.model";

export interface User {
    id?: number;
    username: string;
    email: string;
    password: string;
    posts: Post[];
    likedPosts: Post[];
    likedComments: Comment[];
    groupMember:Group[];
    groupAdmin:Group[]
    isConfirmed: boolean;
}