import {IRoom} from "../room/IRoom";
import {IOpinion} from "../opinion/IOpinion";
import {IBookingStatus} from "../bookingStatus/IBookingStatus";

export interface IReservationResponse {
  id: number;
  reservationNumber: number;
  startOfBooking: Date;
  endOfBooking: Date;
  roomDto: IRoom;
  opinionDto: IOpinion;
  bookingStatusDto: IBookingStatus;
}
