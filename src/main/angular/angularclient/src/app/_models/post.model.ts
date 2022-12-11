import { Image } from "./image.model";
import { Comment } from "./comment.model";
import { UserWithName } from "./user/user-with-name.model";

export interface Post {
    id?: number;
    content: string;
    imageList: Image[];
    owner: UserWithName;
    // group:Group;
    likers: UserWithName[];
    comments: Comment[];
    created:Date;
}

export interface NewPost {
    groupId: string;
    content: string;
    file?: any;
}