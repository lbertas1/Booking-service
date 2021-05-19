export interface IDiscount {
  userId: number;
  amount: number;
  howLongAvailableInDays: number;
  additionalInfo: string;
  realised: boolean;
}
