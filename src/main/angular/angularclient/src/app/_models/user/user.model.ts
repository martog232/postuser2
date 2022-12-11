import { Comment } from "../comment.model";
import { Group } from "../group.model";
import { Post } from "../post.model";
import { UserWithName } from "./user-with-name.model";

export interface User {
    id?: number;
    username: string;
    posts: Post[];
    followers:UserWithName[];
    followings:UserWithName[];
    
}