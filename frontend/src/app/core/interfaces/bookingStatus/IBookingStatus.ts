import { PaymentStatus } from "../../enums/paymentStatus";

export interface IBookingStatus {
  paymentStatus: PaymentStatus;
  totalAmountForReservation: number;
}
