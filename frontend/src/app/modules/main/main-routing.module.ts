import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { MainComponent } from "./main.component";
import { RoomsComponent } from "./rooms/rooms.component";
import { RoomComponent } from "./room/room.component";
import { ProfileComponent } from "./profile/profile.component";
import { RoomResolver, UserResolver } from "../../core/resolvers";
import {FullProfileResolver} from "../../core/resolvers/fullProfile.resolver";

const routes: Routes = [
  {
    path: '',
    component: MainComponent
  },
  {
    path:'rooms',
    component: RoomsComponent,
    // canActivate: [AuthGuard] ogarnąć to
  },
  {
    path: 'rooms/:id',
    component: RoomComponent,
    resolve: {
      room: RoomResolver
    }
  },
  {
    path: 'profile/:id',
    component: ProfileComponent,
    resolve: {
      profile: FullProfileResolver
    }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MainRoutingModule { }
