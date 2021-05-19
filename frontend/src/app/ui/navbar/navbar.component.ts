import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Subscription } from "rxjs";
import {
  NavigationCancel,
  NavigationEnd,
  NavigationError,
  NavigationStart,
  Router
} from "@angular/router";
import { IUser } from "../../core/interfaces";
import {AuthService, JwtService, NotifyService, UserService, WebSocketService} from "../../core/services";
import { ModalService } from "../../core/services";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

  @ViewChild('filters') filterContainer: ElementRef<HTMLElement>;

  private readonly _subscriptions: Subscription[] = [];

  public user: IUser;
  public showLoadingBar = true;

  constructor(
    private readonly _router: Router,
    private readonly _authenticationService: AuthService,
    private readonly _modalService: ModalService,
    private readonly _jwtService: JwtService,
    private readonly _notifyService: NotifyService,
    private readonly _userService: UserService,
    private readonly _webSocketService: WebSocketService
  ) { }

  public images = [
    {
      path: 'app/assets/images/navbar/hotel1.jpg'
    },
    {
      path: 'app/assets/images/navbar/hotel2.jpg'
    },
    {
      path: 'app/assets/images/navbar/hotel4.jpg'
    },
    {
      path: 'app/assets/images/navbar/hotel5.jpg'
    },
    {
      path: 'app/assets/images/navbar/hotel6.jpg'
    },
    {
      path: 'app/assets/images/navbar/hotel7.jpg'
    },
    {
      path: 'app/assets/images/navbar/hotel8.jpg'
    },
    {
      path: 'app/assets/images/navbar/hotel9.jpg'
    },
    {
      path: 'app/assets/images/navbar/hotel10.jpg'
    },
    {
      path: 'app/assets/images/navbar/hotel11.jpg'
    },
    {
      path: 'app/assets/images/navbar/hotel12.jpg'
    }
  ];

  ngOnInit(): void {
    this._router.events.subscribe(event => {
      if (event instanceof NavigationStart) {
        this.showLoadingBar = true;
      } else if (
        event instanceof NavigationEnd ||
        event instanceof NavigationError ||
        event instanceof NavigationCancel
      ) {
        setTimeout(() => this.showLoadingBar = false, 1000);
      }
    });
  }

  public logout(): void {
    this._subscriptions.push(this._userService.logout(this._jwtService.getIdentity()).subscribe());
    this._jwtService.removeIdentity();
    this._notifyService.pushSuccess('Logout', 'Logout was successful. Have a nice day and come back soon');
    this._webSocketService.closeAdminSubscriptions();
    this._router.navigateByUrl('main');
  }

  slideToBottom() {
    console.dir(this.filterContainer.nativeElement.getBoundingClientRect());
    const { y, height } = this.filterContainer.nativeElement.getBoundingClientRect(); // destrukturyzacja.
    // window.scrollTo(0 , t);
    let offset = 0;

    const anim = () => {
      offset += 10;
      window.scrollTo(0, offset);
      if ((y  - height) >= offset) {
        requestAnimationFrame(anim);
      }
    };
    requestAnimationFrame(anim);
    // request animaon frame, offsetop
  }

  public openModal(id: string) {
    this._modalService.open(id);
  }

  public getUsername(): string {
    return this._jwtService.getIdentity().username;
  }

  public changeOnEnglish() {
    localStorage.setItem("language", "english");
    window.location.reload();
  }

  public changeOnPolish() {
    localStorage.setItem("language", "polish");
    window.location.reload();
  }

  public redirectToProfile() {
    let id = this._jwtService.getIdentity().id;
    this._router.navigateByUrl('profile/' + id);
  }

  ngOnDestroy(): void {
    this._subscriptions.forEach(sub => sub.unsubscribe());
  }
}
