import { Image } from "./image.model";
import { Comment } from "./comment.model";
import { User } from "./user/user.model";
import { Group } from "./group.model";
import { UserWithName } from "./user/user-with-name.model";

export interface Post {
    id?: number;
    content: string;
    imageList: Image[];
    owner: UserWithName;
    // group:Group;
    likers: UserWithName[];
    comments: Comment[];
}