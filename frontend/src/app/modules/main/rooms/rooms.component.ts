import {Component, OnDestroy, OnInit} from '@angular/core';
import {IMyHttpResponse, IRoom} from '../../../core/interfaces';
import {RoomsService} from "../../../core/services";
import {ActivatedRoute, Router} from "@angular/router";
import {Subscription} from "rxjs";
import {DatePipe} from "@angular/common";
import {IFiltersCriteria} from "../../../core/interfaces/IFiltersCriteria";
import {Equipments} from "../../../core/enums";

@Component({
  selector: 'app-rooms',
  templateUrl: './rooms.component.html',
  styleUrls: ['./rooms.component.scss'],
  providers: [DatePipe]
})
export class RoomsComponent implements OnInit, OnDestroy {

  public datePickerConfiguration = {
    format: "DD MMMM y"
  };

  priceRange = [
    {
      id: 1,
      name: '50 - 125'
    },
    {
      id: 2,
      name: '125-200'
    },
    {
      id: 3,
      name: '200-250'
    },
    {
      id: 4,
      name: '250-300'
    },
    {
      id: 5,
      name: '300-350'
    },
    {
      id: 6,
      name: '350-400'
    },
    {
      id: 7,
      name: '400-500'
    },
    {
      id: 8,
      name: '500-600'
    },
    {
      id: 9,
      name: '600-700'
    },
    {
      id: 10,
      name: '700-800'
    },
    {
      id: 11,
      name: '800-900'
    },
    {
      id: 12,
      name: '900-1000'
    },
    {
      id: 13,
      name: '1000-2000'
    },
  ];

  capacity = [
    {
      id: 1,
      name: 1
    },
    {
      id: 2,
      name: 2
    },
    {
      id: 3,
      name: 3
    },
    {
      id: 4,
      name: 4
    },
    {
      id: 5,
      name: 5
    }
  ];

  arrivalDate: Date;
  departureDate: Date;
  numberOfGuests: number;
  roomCapacity: number;
  selectedPriceRangesArray: any[] = [];
  selectedEquipments: string[] = [];

  arrivalPlaceholderString: string;
  departurePlaceholderString: string;

  displayedFormat = 'd MMMM y';
  filterFormat = 'YYYY-MM-dd';

  filters: IFiltersCriteria;

  private _subscriptions: Subscription[] = [];

  equipments: any[] = [];

  rooms: IRoom[];

  constructor
  (
    private readonly _roomService: RoomsService,
    private readonly _router: Router,
    private readonly _activatedRoute: ActivatedRoute,
    private readonly _datePipe: DatePipe
  ) {
  }

  ngOnInit(): void {
    this._activatedRoute.queryParams.subscribe(({
                                                  arrivalDateFromMain,
                                                  departureDateFromMain,
                                                  numberOfGuestsFromMain
                                                }) => {
      this.setDates(arrivalDateFromMain, departureDateFromMain);
      this.numberOfGuests = numberOfGuestsFromMain;
      this.roomCapacity = numberOfGuestsFromMain;

      this.createEquipments();

      this._subscriptions
        .push(this._roomService
          .getByFilters(
            arrivalDateFromMain,
            departureDateFromMain,
            numberOfGuestsFromMain,
            this.selectedEquipments,
            null
          )
          .subscribe(value => {
            this.rooms = value;
          }, (error: IMyHttpResponse) => console.log(error)));
    });
  }

  createEquipments() {
    Object.keys(Equipments)
      .map(value => this.equipments.push({
        id: value, name: value
          .toString()
          .toLowerCase()
          .replace('_', ' ')
          .replace('_', ' ')
      }));
  }

  filterRooms() {
    let priceDifference;

    if (this.selectedPriceRangesArray !== undefined) {
      priceDifference = this.calculateDifference();
    } else {
      priceDifference = null;
    }

    this._subscriptions
      .push(this._roomService
        .getByFilters(
          this.changeDateFormat(this.arrivalDate, this.filterFormat),
          this.changeDateFormat(this.departureDate, this.filterFormat),
          this.roomCapacity,
          this.selectedEquipments,
          priceDifference
        )
        .subscribe(value => this.rooms = value));
  }

  private setDates(arrival: any, departure: any) {
    if (arrival != null) {
      this.arrivalPlaceholderString = this.changeDateFormat(new Date(arrival), this.displayedFormat);
    } else {
      this.arrivalPlaceholderString = this.changeDateFormat(new Date(), this.displayedFormat);
    }

    if (departure != null) {
      this.departurePlaceholderString = this.changeDateFormat(new Date(departure), this.displayedFormat);
    } else {
      const tmpDate = new Date().setDate(new Date().getDate() + 1);
      const validDepartureDate = new Date(tmpDate);
      this.departurePlaceholderString = this.changeDateFormat(validDepartureDate, this.displayedFormat);
    }
  }

  getArrivalPlaceholder(): string {
    return 'Arrival: ' + this.arrivalPlaceholderString;
  }

  getDeparturePlaceholder(): string {
    return 'Departure: ' + this.departurePlaceholderString;
  }

  getCapacityPlaceholder(): string {
    if (this.numberOfGuests === null || this.numberOfGuests === undefined) {
      return 'room capacity';
    } else {
      return this.numberOfGuests.toString();
    }
  }

  public getRangeOfCapacity(): number[] {
    return [...new Set(this.rooms.map(value => value.roomCapacity).sort((a, b) => a >> b))];
  }

  changeDateFormat(someDate: Date, format: string): string {
    return this._datePipe.transform(someDate, format);
  }

  onPriceRangeAdd(priceRange: any) {
    if (this.selectedPriceRangesArray === null || this.selectedPriceRangesArray === undefined) {
      this.selectedPriceRangesArray = [];
    }
    this.selectedPriceRangesArray.push(priceRange);
  }

  onPriceRangeRemove(priceRangeIndex: any) {
    this.selectedPriceRangesArray = this.selectedPriceRangesArray.filter(value => value.name !== priceRangeIndex.value.name);
  }

  onEquipmentAdd(equipment: any) {
    this.selectedEquipments.push(equipment.id);
  }

  onEquipmentRemove(equipment: any) {
    this.selectedEquipments = this.selectedEquipments.filter(value => value !== equipment.value.name);
  }

  calculateDifference(): string {
    if (this.selectedPriceRangesArray.length > 0) {
      const minIndex = this.selectedPriceRangesArray.sort((a, b) => a.id - b.id)[0].id;
      const maxIndex = this.selectedPriceRangesArray.sort((a, b) => b.id - a.id)[0].id;

      const maxPriceRange = this.priceRange.find(value => value.id === maxIndex).name;
      const minPriceRange = this.priceRange.find(value => value.id === minIndex).name;

      const maxValue = Number(maxPriceRange.split('-')[1]);
      const minValue = Number(minPriceRange.split('-')[0]);

      return minValue.toString().concat('-').concat(maxValue.toString());
    }
    return null;
  }

  onClearRanges() {
    this.selectedPriceRangesArray = [];
  }

  onClearEquipment() {
    this.selectedEquipments = [];
  }

  ngOnDestroy(): void {
    this._subscriptions.forEach(sub => sub.unsubscribe());
  }
}

