import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MainRoutingModule } from './main-routing.module';
import { MainComponent } from './main.component';
import { IvyCarouselModule } from "angular-responsive-carousel";
import { ProfileComponent } from './profile/profile.component';
import { RoomsComponent } from "./rooms/rooms.component";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { RoomComponent } from './room/room.component';
import { DpDatePickerModule } from "ng2-date-picker";
import { CoreModule } from "../../core/core.module";
import { NgSelectModule } from "@ng-select/ng-select";
import { UiModule } from "../../ui/ui.module";
import { TechnicalBreakComponent } from './technical-break/technical-break.component';
import {FullCalendarModule} from "@fullcalendar/angular";
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';

FullCalendarModule.registerPlugins([
  dayGridPlugin,
  interactionPlugin
]);

@NgModule({
  declarations: [MainComponent, ProfileComponent, RoomsComponent, RoomComponent, TechnicalBreakComponent],
    exports: [
        TechnicalBreakComponent
    ],
  imports: [
    CommonModule,
    MainRoutingModule,
    IvyCarouselModule,
    ReactiveFormsModule,
    DpDatePickerModule,
    CoreModule,
    FormsModule,
    NgSelectModule,
    UiModule,
    FullCalendarModule
  ]
})
export class MainModule {
}
