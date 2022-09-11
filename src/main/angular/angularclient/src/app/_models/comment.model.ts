import { UserWithName } from "./user/user-with-name.model";

export interface Comment{
    
    id?: number;
    content: string;
    owner: UserWithName;
    likers: UserWithName[];
}