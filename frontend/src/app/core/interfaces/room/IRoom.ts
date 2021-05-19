import {Equipments} from "../../enums";

export interface IRoom {
  id: number;
  roomNumber: number;
  roomCapacity: number;
  description: string;
  priceForNight: number;
  isBusy: boolean;
  equipments: Equipments[];
}
