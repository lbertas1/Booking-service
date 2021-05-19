import { IBaseUser } from "./IBaseUser";

export interface IUser extends IBaseUser {
  birthDate: string;
  password: string;
  email: string;
  phone: string;
  address: string;
  city: string;
  zipCode: string;
  country: string;
  isNotLocked: boolean;
}
