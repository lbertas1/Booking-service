import {
  Component,
  EventEmitter,
  HostListener,
  Input,
  OnInit,
  Output
} from '@angular/core';
import {IIdentity, ISocketMessage} from "../../../core/interfaces";
import {Subscription} from "rxjs";
import {MessageIndex, MyConstant, Role} from "../../../core/enums";
import {JwtService, NotifyService, UserService, WebSocketService} from "../../../core/services";
import {SessionStorageService} from "../../../core/services/SessionStorageService";

@Component({
  selector: 'app-chat-window',
  templateUrl: './chat-window.component.html',
  styleUrls: ['./chat-window.component.scss']
})
export class ChatWindowComponent implements OnInit {

  @Input() isChatOpened;

  @Output() closeChatValue = new EventEmitter<boolean>();

  username: string;

  identifier: string;

  public readonly messageIndex = MessageIndex;

  private _subscriptions: Subscription[] = [];

  public message = '';

  private _isShiftClick = false;

  private _identity: IIdentity;

  public conversation: ISocketMessage[] = [];

  @HostListener('document:keyup', ['$event'])
  onKeyUp(evt: KeyboardEvent) {
    if (evt.code === 'ShiftLeft') {
      this._isShiftClick = false;
    }

    if (!this._isShiftClick && evt.code === 'Enter' && this.isChatOpened && this.message.length > 0) {
      this.sendMessage();
    }
  }

  @HostListener('document:keydown', ['$event'])
  onKeyDown(evt: KeyboardEvent) {
    if (evt.code === 'ShiftLeft') {
      this._isShiftClick = true;
    }
  }

  constructor(
    private readonly _webSocketService: WebSocketService,
    private readonly _jwtService: JwtService,
    private readonly _sessionStorageService: SessionStorageService,
    private readonly _userService: UserService,
    private readonly _notifyService: NotifyService
  ) {
  }

  ngOnInit(): void {
    this.getUsername();
    this._subscriptions.push(this._webSocketService.conversation$.subscribe(value => {
      if (value != null && value.message === MyConstant.SERVICE_MESSAGE_CHAT_HAS_BEEN_CLOSED) {
        this._notifyService.pushInfo('Chat closed',
          'The chat was closed by your interlocutor. We invite you to contact another person or at a different date. Remember that you can also send an email from the contact tab.');
        this._webSocketService.changeIsChatActiveValue(false);
      }

      if (value != null && value.message != MyConstant.SERVICE_MESSAGE_CHAT_HAS_BEEN_CLOSED) {
        this.conversation.push(value);
      }
    }));
  }

  getUsername() {
    this._identity = this._jwtService.getIdentity()

    if (this._identity.role === Role.ROLE_ADMIN) {
      this.identifier = this._identity.username;
      this.username = this.identifier;
    } else if (this._identity.role === Role.ROLE_USER) {
      this.identifier = this._identity.username;
      this.conversation.push({index: MessageIndex.INCOMING, message: 'Hello, how can we help you?'});
      this.username = this.identifier;
    } else {
      this.identifier = this._sessionStorageService.getSession();
      this.conversation.push({index: MessageIndex.INCOMING, message: 'Hello, how can we help you?'});
      this.username = this._sessionStorageService.getItem(MyConstant.UNRECOGNIZED_USERNAME);
    }
  }

  sendMessage(): void {
    this._webSocketService.sendMessage(this.message, this.identifier);
    this.message = '';
  }

  closeChat(): void {
    if (this._identity.username === undefined || this._identity.role === Role.ROLE_USER
    ) {
      this._webSocketService.userCloseChat();
    } else {
      this._webSocketService.adminCloseChat();
    }

    this._webSocketService.changeValueOfChatOpened(false);
    this._webSocketService.changeIsChatActiveValue(true);
    this._webSocketService.conversation$.next(null);
    this._webSocketService.closeCommonSubscriptions();
    this._subscriptions.forEach(value => value.unsubscribe());
    this.conversation = [];
    this.closeChatValue.emit(false);
  }

  onKeyPress(code: string) {
    if (code === 'Enter' && !this._isShiftClick) {
      return false;
    }
  }
}
