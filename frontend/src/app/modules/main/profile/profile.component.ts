import { Component, OnInit } from '@angular/core';
import {JwtService, ModalService, ReservationService} from "../../../core/services";
import { UserService } from "../../../core/services";
import { IDiscount } from "../../../core/interfaces/IDiscount";
import { ActivatedRoute } from "@angular/router";
import { Subscription, throwError } from "rxjs";
import {IUserProfile} from "../../../core/interfaces";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

  user: IUserProfile;

  // room: IRoom = {
  //   id: 1,
  //   roomNumber: 1,
  //   roomCapacity: 2,
  //   description: 'lalal',
  //   priceForNight: 100,
  //   equipment: ['air-conditioning', 'minibar', 'tv', 'phone']
  // }

  // bookingStatus: IBookingStatus = {
  //   paymentStatus: PaymentStatus.UNPAID,
  //   totalAmountForReservation: 1200
  // }

  profile: IUserProfile;
  // reservations: IReservation[];

  discount: IDiscount = {
    userId: 1,
    amount: 100,
    howLongAvailableInDays: 2,
    additionalInfo: 'on bar',
    realised: false
  };

  // opinion: IOpinion = {
  //   date: Date.now(),
  //   opinion: 'Beautiful place, great service, I heartily recommend. Definitely 5 stars',
  //   evaluation: 5
  // }

  // STWORZYĆ DUŻY OBIEKT, JEDEN DUŻY PRZESYŁANY TUTAJ NA WYŚWIETLENIE PROFILU !!!

  // NAPISAĆ LOGIKĘ, ŻE DLA KAŻDEJ JEDNEJ REZERWACJI MOŻE DODAĆ OPINIĘ I NIE WIĘCEJ
  // NAPISAĆ JAKĄŚ LOGIKĘ Z ZASADAMI PRZYZNAWANIA PROMOCJI ITD, OD LICZBY REZERWACJI, WYDANEJ KASY NA TERENIE HOTELU, LICZBY REZERWACJI NA RAZ, ŻE ILE OSÓB ITD...

  private _subscriptions: Subscription[] = [];

  constructor(
    private readonly _modalService: ModalService,
    private readonly _userService: UserService,
    private readonly _activatedRoute: ActivatedRoute,
    private readonly _reservationService: ReservationService,
    private readonly _jwtService: JwtService
  ) {
  }

  ngOnInit(): void {
    this._activatedRoute
      .data
      .subscribe(value => this.profile = value.profile);
  }

  public getStatusName(status: number): string {
    return status === 1 ? 'UNPAID' : 'PAID';
  }

  public openModal(id: string) {
    this._modalService.open(id);
  }

  ngOnDestroy(): void {
    this._subscriptions.forEach(sub => sub.unsubscribe());
  }
}
