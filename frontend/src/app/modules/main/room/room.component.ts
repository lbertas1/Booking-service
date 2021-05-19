import {
  AfterViewInit,
  Component,
  OnDestroy,
  OnInit,
  ViewChild
} from '@angular/core';
import {
  IReservationRequest,
  IRoom,
  IRoomBookingDates
} from "../../../core/interfaces";
import {ActivatedRoute} from "@angular/router";
import {Calendar, CalendarOptions, DateSelectArg, FullCalendarComponent} from "@fullcalendar/angular";
import {JwtService, ModalService, NotifyService, ReservationService} from "../../../core/services";
import {Subscription} from "rxjs";
import {DatePipe} from "@angular/common";
import {Translate} from "../../../core/pipes";

@Component({
  selector: 'app-room',
  templateUrl: './room.component.html',
  styleUrls: ['./room.component.scss'],
  providers: [DatePipe, Translate]
})
export class RoomComponent implements OnInit, AfterViewInit, OnDestroy {

  // RENDEREM2 POBRAĆ WSZYSTKIE DNI I Z NICH WYZNACZYĆ TE Z DAT I IM DODAĆ KLASĘ, RENDEREM POBIERA TE WIRTUALNE ELEMENTY HTML

  @ViewChild('calendar') calendarComponent: FullCalendarComponent;

  room: IRoom;

  header = 'Reservation confirmation';

  confirmationMessage: string;

  reservationPrice: number = null;

  private _startReservation: string;

  private _endReservation: string;

  private _dateFormat = 'YYYY-MM-dd'

  private _calendarApi: Calendar;

  private _subscriptions: Subscription[] = [];

  private _roomBookingDates: IRoomBookingDates[] = [];

  private _newReservation: IReservationRequest;

  private _confirmationMessageQuestion = 'Are you sure you want to make a reservation on the selected date: ';

  private _confirmationMessageCosts = 'The cost of the reservation is: ';

  public images = [
    {
      path: 'app/assets/images/room/room1.jpg'
    },
    {
      path: 'app/assets/images/room/room2.jpg'
    },
    {
      path: 'app/assets/images/room/room3.jpg'
    },
    {
      path: 'app/assets/images/room/room4.jpg'
    },
    {
      path: 'app/assets/images/room/room5.jpg'
    },
    {
      path: 'app/assets/images/room/room6.jpg'
    }
  ];

  calendarOptions: CalendarOptions = {
    initialView: 'dayGridMonth',
    selectable: true,
    select: (info) => {
      this.calculatePriceForReservation(info);

      this._startReservation = this.formatDate(info.start, this._dateFormat);
      this._endReservation = this.formatDate(info.end, this._dateFormat);


      if (!!this._jwtService.getIdentity() === false) {

      }
    }
    // selectAllow: (span, movingEvent) => {
    //
    // }
  };

  constructor(
    private readonly _activatedRoute: ActivatedRoute,
    private readonly _reservationService: ReservationService,
    private readonly _notifyService: NotifyService,
    private readonly _datePipe: DatePipe,
    private readonly _jwtService: JwtService,
    private readonly _translatePipe: Translate,
    private readonly _modalService: ModalService
  ) {
  }

  ngOnInit(): void {
    this._activatedRoute
      .data
      .subscribe(value => this.room = value.room);
  }

  ngAfterViewInit(): void {
    this._calendarApi = this.calendarComponent.getApi();
    this.getEvents();
  }

  getEvents() {
    this._subscriptions
      .push(this._reservationService
        .getRoomBookingDates(this.room.id)
        .subscribe(value => {
          this._roomBookingDates = value;

          value
            .forEach(dates => {
              this._calendarApi.addEvent({
                start: dates.reservationStartDate,
                end: dates.reservationEndDate,
                display: 'background',
                color: '#000000',
                // classNames: ['event-disabled']
              });
            });
        }));

    this._calendarApi.render();
  }

  createReservation(): boolean {
    if (this.checkCorrectnessDates()) {
      if (!!this._jwtService.getIdentity() !== false) {
        this._newReservation = {
          id: null,
          startOfBooking: this._startReservation,
          endOfBooking: this._endReservation,
          roomId: this.room.id,
          userId: this._jwtService.getIdentity().id
        };

        this.prepareMessage();

        this._modalService.open('confirmationModal');
      } else {
        this._notifyService.pushInfo('Account', 'You must be logged in to make a reservation. ' +
          'If you have an account, please log in, otherwise register your account and log in to make a reservation.');
        setTimeout(() => this._modalService.open('loginModal'), 2000);
      }
    }

    return true;
  }

  confirmReservation(confirmationValue: boolean) {
    if (confirmationValue) {
      this._subscriptions
        .push(this._reservationService
          .save(this._newReservation)
          .subscribe((value) => {
            if (value.roomDto === undefined) {
              this._notifyService.pushError('Failure', 'Unfortunately, the attempt to make a reservation was unsuccessful.');
            } else {
              this._calendarApi.addEvent({
                start: value.startOfBooking,
                end: value.endOfBooking,
                display: 'background',
                color: '#000000'
              });
              this._notifyService.pushSuccess('Success', 'The reservation has been confirmed');
              this._roomBookingDates.push({ reservationStartDate: this._startReservation, reservationEndDate: this._endReservation });
              this._calendarApi.render();
            }
          }));
    }
  }

  private checkCorrectnessDates() {
    let result = true;

    if (new Date(this._startReservation).getTime() < new Date().getTime()) {
      this._notifyService.pushError('Invalid date', 'Sorry, you cannot book a room for an expired date.');
      result = false;
      return false;
    }

    this._roomBookingDates
      .forEach(roomBookingDate => {
        if (new Date(this._startReservation).getTime() > new Date(roomBookingDate.reservationStartDate).getTime() &&
          new Date(this._startReservation).getTime() < new Date(roomBookingDate.reservationEndDate).getTime()) {
          this._notifyService.pushError('Invalid date', 'Sorry, the given date is not available. ' +
            'Please select a free date or another room.');
          result = false;
          return false;
        }

        if (new Date(this._endReservation).getTime() > new Date(roomBookingDate.reservationStartDate).getTime() &&
          new Date(this._endReservation).getTime() < new Date(roomBookingDate.reservationEndDate).getTime()) {
          this._notifyService.pushError('Invalid date', 'Sorry, the given date is not available. ' +
            'Please select a free date or another room.');
          result = false;
          return false;
        }

        if (new Date(this._startReservation).getTime() < new Date(roomBookingDate.reservationStartDate).getTime() &&
        new Date(this._endReservation).getTime() > new Date(roomBookingDate.reservationEndDate).getTime()) {
          this._notifyService.pushError('Invalid date', 'Sorry, the given date is not available. ' +
            'Please select a free date or another room.');
          result = false;
          return false;
        }
      })

    return result;
  }

  private prepareMessage(): void {
    this.header = this._translatePipe.transform(this.header);
    this._confirmationMessageQuestion = this._translatePipe.transform(this._confirmationMessageQuestion);
    this._confirmationMessageCosts = this._translatePipe.transform(this._confirmationMessageCosts);
    this.confirmationMessage = this._confirmationMessageQuestion.concat(this._startReservation, ' - ', this._endReservation, '? ', this._confirmationMessageCosts, this.reservationPrice.toString());
    this._confirmationMessageQuestion = 'Are you sure you want to make a reservation on the selected date: ';
    this._confirmationMessageCosts = 'The cost of the reservation is: ';
  }

  private calculatePriceForReservation(info: DateSelectArg) {
    var differenceInTime = info.end.getTime() - info.start.getTime();
    var differenceInDays = Math.floor(differenceInTime / (1000 * 3600 * 24));
    this.reservationPrice = differenceInDays * this.room.priceForNight;
    setTimeout(() => this.reservationPrice = null, 8000);
  }

  private formatDate(someDate: Date, format: string): string {
    return this._datePipe.transform(someDate, format);
  }

  ngOnDestroy(): void {
    this._subscriptions.forEach(value => value.unsubscribe());
  }
}
