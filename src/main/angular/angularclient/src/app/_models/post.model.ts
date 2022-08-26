import { Image } from "./image.model";
import { Comment } from "./comment.model";
import { User } from "./user/user.model";

export interface Post {
    id?: number;
    content: string;
    imageList: Image[];
    likers: User[];
    name: string;
    comment: Comment[];
    owner: User;
}