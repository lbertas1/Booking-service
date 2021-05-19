import {Injectable} from "@angular/core";
import {RxStompService} from "@stomp/ng2-stompjs";
import {BehaviorSubject, Subscription} from "rxjs";
import {JwtService} from "./JwtService";
import {SessionStorageService} from "./SessionStorageService";
import {Message} from "@stomp/stompjs";
import {MessageIndex, MyConstant, Role} from "../enums";
import {ICityUsernameData, IIdentity, ISocketMessage} from "../interfaces";
import {UserService} from "./UserService";
import {ChatService} from "./ChatService";

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {

  private _identity: IIdentity;

  private _userSubscriptions: Subscription[] = [];

  private _adminSubscriptions: Subscription[] = [];

  private cityAdminData: ICityUsernameData;

  private cityAdminDataSubject$ = new BehaviorSubject<any>(null);

  conversation$ = new BehaviorSubject<ISocketMessage>(null);

  private _isChatActiv = true;

  userIdentifier: string;

  isChatOpened$ = new BehaviorSubject<boolean>(false);

  private adminUsername: string;

  isEnterAllowed$ = new BehaviorSubject<boolean>(false);

  isChatActive$ = new BehaviorSubject<boolean>(true);

  userUsername$ = new BehaviorSubject<string>(null);

  constructor(
    private readonly _rxStompService: RxStompService,
    private readonly _jwtService: JwtService,
    private readonly _sessionService: SessionStorageService,
    private readonly _chatService: ChatService
  ) {
  }

  getCityAdminData(data: ICityUsernameData) {
    this.cityAdminDataSubject$.next(data);
  }

  changeValueOfEnterAllowed(value: boolean) {
    this.isEnterAllowed$.next(value);
  }

  changeValueOfChatOpened(value: boolean) {
    this.isChatOpened$.next(value);
  }

  changeIsChatActiveValue(value: boolean) {
    this.isChatActive$.next(value);
  }

  getUserUsername(username: string) {
    this.userUsername$.next(username);
  }

  startSubscription() {
    this._userSubscriptions.push(this.cityAdminDataSubject$.subscribe(value => this.cityAdminData = value));
    this._userSubscriptions.push(this.isChatActive$.subscribe(value => this._isChatActiv = value));

    this._identity = this._jwtService.getIdentity();
    if (this._identity.role === Role.ROLE_USER) {

      this.userIdentifier = this._identity.username;

    } else this.userIdentifier = this._sessionService.createSession();

    this._userSubscriptions.push(this._chatService.startChat({
      userIdentifier: this.userIdentifier,
      adminIdentifier: this.cityAdminData.username
    }).subscribe());

    this._userSubscriptions.push(this.connect(this.userIdentifier));

    this.isChatOpened$.next(true);
  }

  adminStartSubscription() {
    this._adminSubscriptions.push(this.isChatActive$.subscribe(value => this._isChatActiv = value));
    this._adminSubscriptions.push(this.connect(this._jwtService.getIdentity().username));
    this._identity = this._jwtService.getIdentity();
    this.adminUsername = this._identity.username;
    this._userSubscriptions.push(this.isChatActive$.subscribe(value => this._isChatActiv = value));
  }

  connect(userIdentifier: string): Subscription {
    return this._rxStompService
      .watch('/topic/chat/' + userIdentifier)
      .subscribe((message: Message) => {

        this.conversation$.next({index: MessageIndex.INCOMING, message: message.body});
        if (
          this._identity.role === Role.ROLE_ADMIN &&
          this.conversation$.value != null &&
          this.isChatOpened$.value === false &&
          message.body !== MyConstant.SERVICE_MESSAGE_CHAT_HAS_BEEN_CLOSED) {
          this.isChatOpened$.next(true);
        }
      })
  }

  sendMessage(message: string, identifier: string): string {
    if (this._isChatActiv) {
      this.conversation$.next({index: MessageIndex.THIS, message: message});

      this._rxStompService.publish(
        {
          destination: '/app/chat/' + identifier,
          body: message
        });

      return message;

    } else {
      this.conversation$.next({
        index: MessageIndex.ERROR,
        message: MyConstant.SORRY_CHAT_CLOSED});

      return null;
    }
  }

  userCloseChat() {
    this._userSubscriptions
      .push(this._chatService.endChat(this.userIdentifier)
        .subscribe());
  }

  adminCloseChat() {
    this._userSubscriptions
      .push(this._chatService.endChat(this.adminUsername)
        .subscribe());
  }

  closeCommonSubscriptions() {
    this._userSubscriptions.forEach(value => value.unsubscribe());
  }

  closeAdminSubscriptions() {
    this._adminSubscriptions.forEach(value => value.unsubscribe());
  }
}
