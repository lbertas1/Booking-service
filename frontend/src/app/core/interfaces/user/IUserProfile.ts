import {ISimpleReservation} from "../reservation/ISimpleReservation";
import {Role} from "../../enums";
import {IUserRequest} from "./IUserRequest";
import {IUserResponse} from "./IUserResponse";

export interface IUserProfile {
  user: IUserResponse;
  reservations: ISimpleReservation[];
}
