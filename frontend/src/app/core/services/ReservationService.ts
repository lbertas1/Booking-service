import {Injectable} from "@angular/core";
import {ApiService} from "./ApiService";
import {JwtService} from "./JwtService";
import {
  IOpinionRequest,
  IReservationRequest,
  IReservationResponse, IRoomBookingDates,
  IUserProfile
} from "../interfaces";
import {Observable} from "rxjs";
import {IOpinion} from "../interfaces/opinion/IOpinion";

@Injectable({
  providedIn: 'root'
})
export class ReservationService {
  private path: string = '/reservations';

  constructor(
    private readonly _apiService: ApiService,
    private readonly jwtService: JwtService
  ) {
  }

  public searchForEndingReservations(): Observable<IReservationResponse[]> {
    return this._apiService.get(`${this.path}/endings`);
  }

  public save(iReservation: IReservationRequest): Observable<IReservationResponse> {
    return this._apiService.post(`${this.path}/save`, iReservation);
  }

  public update(iReservation: IReservationRequest): Observable<IReservationResponse> {
    return this._apiService.put(`${this.path}/update`, iReservation);
  }

  public getFullProfile(userId: number): Observable<IUserProfile> {
    return this._apiService.get(`${this.path}/profile/${userId}`)
  }

  public getRoomBookingDates(roomId: number): Observable<IRoomBookingDates[]> {
    return this._apiService.get(`${this.path}/room-booking-dates/${roomId}`);
  }

  public unpaidReservationToDate(date: string): Observable<IReservationResponse[]> {
    return this._apiService.get(`${this.path}/unpaid-reservation-to-date/${date}`);
  }

  public removeCompletedReservations(): Observable<IReservationResponse[]> {
    return this._apiService.delete(`${this.path}/remove-completed-reservations`);
  }

  public showAllUserReservations(id: number): Observable<IReservationResponse[]> {
    return this._apiService.get(`${this.path}/show-all-reservations/${id}`);
  }

  public addOpinion(opinion: IOpinionRequest): Observable<IOpinionRequest> {
    return this._apiService.post(`${this.path}/add-opinion`, opinion);
  }

  public updateOpinion(opinion: IOpinionRequest): Observable<IOpinionRequest> {
    return this._apiService.put(`${this.path}/update-opinion`, opinion);
  }

  public removeOpinion(reservationId: number): Observable<IOpinion> {
    return this._apiService.post(`${this.path}/remove-opinion/${reservationId}`);
  }
}
