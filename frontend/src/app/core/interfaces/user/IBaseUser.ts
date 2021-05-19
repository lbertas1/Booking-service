import { Role } from "../../enums";

export interface IBaseUser {
  id: number;
  name: string;
  surname: string;
  username: string;
  role: Role;
}
