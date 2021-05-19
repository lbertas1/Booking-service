import {
  Component,
  ElementRef,
  EventEmitter,
  HostListener,
  Input,
  OnDestroy,
  OnInit,
  ViewChild
} from '@angular/core';
import {NgxSmartModalComponent, NgxSmartModalService} from "ngx-smart-modal";
import {JwtService, NotifyService, UserService, WebSocketService} from "../../../core/services";
import {Subscription, throwError} from "rxjs";
import {Router} from "@angular/router";
import {ICityUsernameData} from "../../../core/interfaces";
import {MyConstant, Role} from "../../../core/enums";
import {SessionStorageService} from "../../../core/services/SessionStorageService";

@Component({
  selector: 'app-chat-modal',
  templateUrl: './chat-modal.component.html',
  styleUrls: ['./chat-modal.component.scss']
})
export class ChatModalComponent implements OnInit, OnDestroy {

  @ViewChild('chatModal') chatModal: NgxSmartModalComponent;

  @ViewChild('city') cityElement: ElementRef;

  @Input() availableAdmins: ICityUsernameData[];

  user: string;

  usernameValue: string;

  private isEnterAllowed: boolean;

  private subscriptions: Subscription[] = [];


  @HostListener('document:keydown', ['$event'])
  onEnter(keyboardEvent: KeyboardEvent) {
    if (keyboardEvent.code === 'Enter' && this.isEnterAllowed) {
      this.getUserChoice();
    }
  }

  constructor(
    private readonly _notifyService: NotifyService,
    private readonly _userService: UserService,
    private readonly _router: Router,
    private readonly _ngxSmartModal: NgxSmartModalService,
    private readonly _webSocketApi: WebSocketService,
    private readonly _jwtService: JwtService,
    private readonly _sessionStorageService: SessionStorageService
  ) {
  }

  ngOnInit(): void {
    this.subscriptions.push(this._webSocketApi.isEnterAllowed$.subscribe(value => this.isEnterAllowed = value));
    this.subscriptions.push(this._webSocketApi.userUsername$.subscribe(value => {
      this.user = value;
      this.usernameValue = value;
    }));

    if (this.user !== undefined) {
      this.usernameValue = this.user;
    }
  }

  public getUserChoice(): void {
    let city = this.cityElement.nativeElement.value;

    if (this.user === null) {
      this._sessionStorageService.setItem(MyConstant.UNRECOGNIZED_USERNAME, this.usernameValue);
    }

    if (this.usernameValue.length > 5) {

      this._webSocketApi.getCityAdminData(this.availableAdmins.find(value => value.city === city));
      this._webSocketApi.changeValueOfEnterAllowed(false);
      this._webSocketApi.startSubscription();
      this.chatModal.close();
    } else {
      this._notifyService.pushError("Username incorrect",
        "Your username should be at least 5 characters long");
      this.chatModal.close();
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(value => value.unsubscribe());
  }
}

