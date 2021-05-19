import {Role} from "../../enums";

export interface IUserResponse {
  id: number;
  username: string;
  name: string;
  surname: string;
  birthDate: string;
  email: string;
  phone: string;
  address: string;
  city: string;
  zipCode: string;
  country: string;
  role: Role;
}
