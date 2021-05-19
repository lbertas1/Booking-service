import {Component, OnInit, ViewChild} from '@angular/core';
import {NgxSmartModalComponent} from "ngx-smart-modal";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Subscription} from "rxjs";
import {JwtService, NotifyService, UserService} from "../../../core/services";
import {IChangePassword} from "../../../core/interfaces";

@Component({
  selector: 'app-change-password-modal',
  templateUrl: './change-password-modal.component.html',
  styleUrls: ['./change-password-modal.component.scss']
})
export class ChangePasswordModalComponent implements OnInit {

  // DOROBIĆ WALIDATORY ODPOWIEDNIE DO PASSWORD!

  // ZWALIDOWAĆ IDENTYCZNOŚĆ HASEŁ!!!!!!!!!!!!

  @ViewChild('changePasswordModal') changePasswordModal: NgxSmartModalComponent;

  public form: FormGroup;

  public isSubmitted = false;

  private _subscriptions: Subscription[] = [];

  private readonly _formFields = {
    oldPassword: ['', Validators.required],
    newPassword: ['', Validators.required],
    passwordConfirmation: ['', Validators.required]
  };

  constructor(
    private readonly _jwtService: JwtService,
    private readonly _formBuilder: FormBuilder,
    private readonly _userService: UserService,
    private readonly _notifyService: NotifyService
  ) {
  }

  ngOnInit(): void {
    this.form = this._formBuilder.group(this._formFields);
  }

  public get formControls() {
    return this.form.controls;
  }

  public submit(): void {
    let changePassword: IChangePassword = {
      userId: this._jwtService.getIdentity().id,
      oldPassword: this.form.controls['oldPassword'].value,
      newPassword: this.form.controls['newPassword'].value
    }

    this.isSubmitted = true;

    if (this.form.controls.newPassword.value !== this.form.controls.passwordConfirmation.value) {
      this._notifyService.pushError('', 'Password confirmation does not equal the new password');
      this.form.enable();
      this.isSubmitted = false;
      return;
    }

    if (!this.form.valid) {
      this.form.enable();
      this.isSubmitted = false;
      return;
    }

    this.form.disable();
    this._subscriptions.push(this._userService.changePassword(changePassword).subscribe(() => {
      this.isSubmitted = false;
      this._notifyService.pushSuccess('', 'Update success!');
      this.changePasswordModal.close();
    }, error => {
      this.form.enable();
      this.isSubmitted = false;
      this._notifyService.pushError('', 'Update failed');
    }));
  }

  ngOnDestroy(): void {
    this._subscriptions.forEach(value => value.unsubscribe());
  }
}
