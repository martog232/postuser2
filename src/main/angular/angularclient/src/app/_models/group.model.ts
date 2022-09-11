import { Post } from "./post.model";
import { UserWithName } from "./user/user-with-name.model";

export interface Group{ 
    id?:number;
    name:string;
    description:string;
    posts:Post[];
    members:UserWithName[];
    admins:UserWithName[];
}