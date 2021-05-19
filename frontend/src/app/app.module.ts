import { NgModule } from "@angular/core";
import { AppComponent } from "./app.component";
import { BrowserModule } from "@angular/platform-browser";
import { AppRoutingModule } from "./app-routing.module";
import { HTTP_INTERCEPTORS, HttpClientModule } from "@angular/common/http";
import { AuthInterceptor } from "./core/interceptors";
import { UiModule } from "./ui/ui.module";
import { IvyCarouselModule } from "angular-responsive-carousel";
import { NgxSmartModalModule } from "ngx-smart-modal";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { ToastrModule } from "ngx-toastr";
import { MainModule } from "./modules/main/main.module";
import {CoreModule} from "./core/core.module";

@NgModule({
  declarations: [
    AppComponent
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        UiModule,
        IvyCarouselModule,
        HttpClientModule,
        NgxSmartModalModule.forRoot(),
        BrowserAnimationsModule,
        ToastrModule.forRoot(),
        MainModule,
        CoreModule
    ],
  providers: [
      {
        provide: HTTP_INTERCEPTORS,
        useClass: AuthInterceptor,
        multi: true
      }
    ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
