import {Injectable} from "@angular/core";
import {ApiService} from "./ApiService";
import {IRoom} from "../interfaces";
import {Observable} from "rxjs";
import {HttpParams} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class RoomsService {

  private path: string = '/rooms';

  constructor(
    private readonly _apiService: ApiService
  ) {
  }

  public get(id: number): Observable<IRoom> {
    return this._apiService.get(`${this.path}/${id}`);
  }

  public getByFilters(
    arrivalDate: string,
    departureDate: string,
    roomCapacity: number,
    selectedEquipments: string[],
    priceRange: string
    ): Observable<IRoom[]> {

    const params = new HttpParams()
      .set('arrivalDate', arrivalDate !== null && arrivalDate !== undefined ? arrivalDate : null)
      .set('departureDate', departureDate !== null && departureDate !== undefined ? departureDate : null)
      .set('roomCapacity', roomCapacity !== null && roomCapacity !== undefined ? roomCapacity.toString() : null)
      .set('selectedEquipments', selectedEquipments !== null && selectedEquipments !== undefined ? selectedEquipments.toString() : null)
      .set('priceRange', priceRange !== null && priceRange !== undefined ? priceRange : null);

    return this._apiService.get(`${this.path}/filtered-rooms`, params);
  }
}
