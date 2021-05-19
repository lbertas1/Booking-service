import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {NavbarComponent} from './navbar/navbar.component';
import {RouterModule} from "@angular/router";
import {NgxSmartModalModule} from "ngx-smart-modal";
import {LoginModalComponent} from './modals/login-modal/login-modal.component';
import {FooterComponent} from './footer/footer.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {RegistrationModalComponent} from "./modals/registration-modal/registration-modal.component";
import {CoreModule} from "../core/core.module";
import {IvyCarouselModule} from "angular-responsive-carousel";
import {ChatModalComponent} from './modals/chat-modal/chat-modal.component';
import {InjectableRxStompConfig, RxStompService, rxStompServiceFactory} from "@stomp/ng2-stompjs";
import {myRxStompConfig} from "../my-rx-stomp.config";
import {ChatComponent} from "./chat/chat.component";
import {ChatWindowComponent} from "./chat/chat-window/chat-window.component";
import {UpdateUserModalComponent} from './modals/update-user-modal/update-user-modal.component';
import {ChangePasswordModalComponent} from './modals/change-password-modal/change-password-modal.component';
import {AddOpinionModalComponent} from './modals/add-opinion-modal/add-opinion-modal.component';
import { ConfirmationModalComponent } from './modals/confirmation-modal/confirmation-modal.component';

@NgModule({
  declarations: [NavbarComponent, LoginModalComponent, RegistrationModalComponent, FooterComponent, ChatComponent, ChatWindowComponent, ChatModalComponent, UpdateUserModalComponent, ChangePasswordModalComponent, AddOpinionModalComponent, ConfirmationModalComponent],
    exports: [
        NavbarComponent,
        FooterComponent,
        ChatModalComponent,
        UpdateUserModalComponent,
        ChangePasswordModalComponent,
        AddOpinionModalComponent,
        ConfirmationModalComponent
    ],
  imports: [
    CommonModule,
    RouterModule,
    NgxSmartModalModule,
    ReactiveFormsModule,
    CoreModule,
    IvyCarouselModule,
    FormsModule
  ],
  providers: [
    {
      provide: InjectableRxStompConfig,
      useValue: myRxStompConfig,
    },
    {
      provide: RxStompService,
      useFactory: rxStompServiceFactory,
      deps: [InjectableRxStompConfig],
    },
  ]
})
export class UiModule {
}
