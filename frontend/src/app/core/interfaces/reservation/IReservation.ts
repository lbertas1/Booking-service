import { IUser } from "../user/IUser";
import { IRoom } from "../room/IRoom";
import { IBookingStatus } from "../bookingStatus/IBookingStatus";
import {IOpinion} from "../opinion/IOpinion";

export interface IReservation {
  id: string;
  reservationNumber: number;
  startOfBooking: Date;
  endOfBooking: Date;
  user: IUser;
  room: IRoom;
  opinion: IOpinion;
  bookingStatus: IBookingStatus;
}
