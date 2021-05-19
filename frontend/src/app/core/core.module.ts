import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ShowForAuthDirective} from './directives';
import {HTTP_INTERCEPTORS} from "@angular/common/http";
import {AuthInterceptor} from "./interceptors";
import {Translate} from "./pipes";
import {
  InjectableRxStompConfig,
  RxStompService,
  rxStompServiceFactory
} from "@stomp/ng2-stompjs";
import {myRxStompConfig} from "../my-rx-stomp.config";

@NgModule({
  declarations: [ShowForAuthDirective, Translate],
  exports: [
    ShowForAuthDirective,
    Translate
  ],
  imports: [
    CommonModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
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
export class CoreModule {
}
