import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {CalendarValue, DatePickerComponent} from "ng2-date-picker";
import {DatePipe} from "@angular/common";
import {UserWebsocketService} from "../../core/services/UserWebsocketService";
import {IUser} from "../../core/interfaces";

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss'],
  providers: [DatePipe]
})
export class MainComponent implements OnInit, OnDestroy {

  @ViewChild('arrivalDayPicker') arrivalDayPicker: DatePickerComponent;

  @ViewChild('departureDayPicker') departureDayPicker: DatePickerComponent;

  currentDate = new Date();
  intermediateDate = new Date().setDate(this.currentDate.getDate() + 1);
  nextDate = new Date(this.intermediateDate);

  public selectedArrivalDate: CalendarValue;
  public selectedDepartureDate: CalendarValue;

  public numberOfGuests = 0;

  public datePickerConfig = {
    format: "DD MMMM yyyy"
  };

  constructor(
    private readonly _datePipe: DatePipe,
    private readonly _userWebSocketService: UserWebsocketService
  ) {
  }

  ngOnInit(): void {
    // this._userWebSocketService.connect(() => {
    //   this._userWebSocketService.createMethod<IUser[]>((payload => {
    //     console.log(payload)
    //   }));
    // });

    // this._userWebSocketService.connect(() => {
    //     this._userWebSocketService.createMethod<IUser[]>((payload => {
    //       console.log('JEST POŁĄCZONY');
    //     }));
    // })
  }

  getArrivalDate(calendarValue: CalendarValue): string {
    if (calendarValue != undefined) {
      return this.changeFormat(calendarValue);
    } else {
      return this._datePipe.transform(this.currentDate, 'YYYY-MM-dd');
    }
  }

  getDepartureDate(calendarValue: CalendarValue): string {
    if (calendarValue != undefined) {
      return this.changeFormat(calendarValue);
    } else {
      return this._datePipe.transform(this.nextDate, 'YYYY-MM-dd');
    }
  }

  changeFormat(calendarValue: CalendarValue): string {
    return this._datePipe.transform(new Date(calendarValue.toString()), 'YYYY-MM-dd');
  }

  ngOnDestroy(): void {
    // this._userWebSocketService.disconnect();
  }
}
