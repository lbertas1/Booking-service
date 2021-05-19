import {Component, Input, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {NgxSmartModalComponent} from "ngx-smart-modal";
import {IUpdateUser, IUser, IUserProfile} from "../../../core/interfaces";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NotifyService, UserService} from "../../../core/services";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-update-user-modal',
  templateUrl: './update-user-modal.component.html',
  styleUrls: ['./update-user-modal.component.scss']
})
export class UpdateUserModalComponent implements OnInit, OnDestroy {

  @Input() profile: IUserProfile;

  @ViewChild('updateUserModal') updateUserModal: NgxSmartModalComponent;

  public form: FormGroup;

  public isSubmitted = false;

  private _subscriptions: Subscription[] = [];

  public updateUser: IUpdateUser;

  private readonly _formFields = {
    name: [''],
    surname: [''],
    birthday: [''],
    city: [''],
    zipcode: [''],
    phone: [''],
    address: [''],
    country: [''],
    email: ['', Validators.email],
  };

  constructor(
    private readonly _formBuilder: FormBuilder,
    private readonly _userService: UserService,
    private readonly _notifyService: NotifyService
  ) { }

  ngOnInit(): void {
    this.form = this._formBuilder.group(this._formFields);
    this.updateUser = this.setUpdateUser();
  }

  private setUpdateUser(): IUpdateUser {
    return {
      id: this.profile.user.id,
      name: this.profile.user.name,
      surname: this.profile.user.surname,
      birthDate: this.profile.user.birthDate,
      city: this.profile.user.city,
      country: this.profile.user.country,
      email: this.profile.user.email,
      phone: this.profile.user.phone,
      address: this.profile.user.address,
      zipCode: this.profile.user.zipCode
    }
  }

  public get formControls() {
    return this.form.controls;
  }

  public submit(): void {
    this.setNewValues();

    this.isSubmitted = true;

    if (!this.form.valid) {
      return;
    }
    this.form.disable();
    this._subscriptions.push(this._userService.update(this.updateUser).subscribe((value) => {
      this.updateUser = value;
      this.isSubmitted = false;
      this._notifyService.pushSuccess('', 'Update success!');
      this.updateUserModal.close();
    }, error => {
      this.form.enable();
      this.isSubmitted = false;
      this._notifyService.pushError('', 'Update failed');
    }));
  }

  private setNewValues(): IUpdateUser {
    for (let controlsKey in this.form.controls) {
      if (this.form.controls[controlsKey].value !== '') {
        this.updateUser[controlsKey] = this.form.controls[controlsKey].value;
      }
    }

    return this.updateUser;
  }

  ngOnDestroy(): void {
    this._subscriptions.forEach(value => value.unsubscribe());
  }
}
