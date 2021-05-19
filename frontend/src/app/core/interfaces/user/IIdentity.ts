import { IBaseUser } from "./IBaseUser";

export interface IIdentity extends IBaseUser {
  token: string;
}
