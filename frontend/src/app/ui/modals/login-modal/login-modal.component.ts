import {Component, EventEmitter, OnDestroy, OnInit, Output, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {JwtService, NotifyService, UserService, WebSocketService} from "../../../core/services";
import {NgxSmartModalComponent} from "ngx-smart-modal";
import {Subject, Subscription} from "rxjs";
import {Role} from "../../../core/enums";
import {IIdentity} from "../../../core/interfaces";

@Component({
  selector: 'app-login-modal',
  templateUrl: './login-modal.component.html',
  styleUrls: ['./login-modal.component.scss']
})
export class LoginModalComponent implements OnInit, OnDestroy {

  private _subscriptions: Subscription[] = [];

  @ViewChild('loginModal') loginModal: NgxSmartModalComponent;

  @Output() registerModalOpen = new EventEmitter<any>();

  public form: FormGroup;

  public isSubmitted = false;

  private readonly _loginFields = {
    username: ['', Validators.required],
    password: ['', Validators.required]
  }
  private _iIdentity: IIdentity;

  constructor(
    private readonly _formBuilder: FormBuilder,
    private readonly _userService: UserService,
    private readonly _notifyService: NotifyService,
    private readonly _jwtService: JwtService,
    private readonly _webSocketService: WebSocketService
  ) {
  }

  ngOnInit(): void {
    this.initForm();
  }

  public submit(): void {
    this.isSubmitted = true;

    if (!this.form.valid) {
      return;
    }

    this.form.disable();

    this._subscriptions.push(this._userService.login(this.form.value).subscribe((identity) => {
      this.isSubmitted = false;
       this._iIdentity = this._jwtService.saveIdentity(identity);
      this._notifyService.pushSuccess('', 'Login success!');
      this.connectToChat();
      this.form.enable();
      this.loginModal.close();
    }, error => {
      this.form.enable();
      this.isSubmitted = false;
      this._notifyService.pushError('', 'Login failed');
    }));
  }

  public get fc() {
    return this.form.controls;
  }

  public openRegisterModal(): void {
    this.loginModal.close();
    this.registerModalOpen.emit();
  }

  public onClose(): void {
    this.initForm();
  }

  private initForm(): void {
    this.form = this._formBuilder.group(this._loginFields);
  }

  private connectToChat() {
    if (this._iIdentity.role === Role.ROLE_ADMIN) {
      this._webSocketService.adminStartSubscription();
    }
  }

  ngOnDestroy(): void {
    this._subscriptions.forEach(value => value.unsubscribe());
  }
}
