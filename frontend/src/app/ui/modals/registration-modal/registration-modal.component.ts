import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NotifyService, UserService} from "../../../core/services";
import {Subscription} from "rxjs";
import {NgxSmartModalComponent} from "ngx-smart-modal";
import {PasswordValidator} from "../../../core/validators";
import {IRegisterUser, IUser} from "../../../core/interfaces";

@Component({
  selector: 'app-registration-modal',
  templateUrl: './registration-modal.component.html',
  styleUrls: ['./registration-modal.component.scss']
})
export class RegistrationModalComponent implements OnInit, OnDestroy {

  private _subscriptions: Subscription[] = [];

  @ViewChild('registerModal') registerModal: NgxSmartModalComponent;

  public form: FormGroup;

  public isSubmitted = false;

  private readonly _formFields = {
    name: ['', Validators.required],
    surname: ['', Validators.required],
    birthDate: ['', Validators.required],
    username: ['', Validators.required],
    password: ['', [Validators.required, PasswordValidator.strength]],
    city: ['', Validators.required],
    zipCode: ['', Validators.required],
    phone: ['', Validators.required],
    address: ['', Validators.required],
    country: ['', Validators.required],
    email: ['', Validators.compose([Validators.required, Validators.email])],
    ageConfirmation: ['', Validators.required],
    acceptedTerms: ['', Validators.required]
  };

  constructor(
    private readonly _formBuilder: FormBuilder,
    private readonly _userService: UserService,
    private readonly _notifyService: NotifyService
  ) {
  }

  ngOnInit(): void {
    this.form = this._formBuilder.group(this._formFields);
  }

  public get fc() {
    return this.form.controls;
  }

  public submit(): void {
    this.isSubmitted = true;

    console.log('Birthdate ' + this.form.controls.birthDate.value);


    let user: IRegisterUser = {
      name: this.form.controls['name'].value,
      surname: this.form.controls['surname'].value,
      birthDate: this.form.controls['birthDate'].value,
      phone: this.form.controls['phone'].value,
      username: this.form.controls['username'].value,
      password: this.form.controls['password'].value,
      city: this.form.controls['city'].value,
      address: this.form.controls['address'].value,
      country: this.form.controls['country'].value,
      zipCode: this.form.controls['zipCode'].value,
      email: this.form.controls['email'].value
    }

    // NAPRAWIĆ WALIDACJĘ!!!
    // if (!this.form.valid) {
    //   console.log('NIE JEST VALID');
    //   this.isSubmitted = false;
    //   return;
    // }

    this.form.disable();

    this._subscriptions.push(this._userService.register(user).subscribe(() => {
      this._notifyService.pushSuccess('', 'Register success!');
      this.isSubmitted = false;
      this.form.enable();
      this.registerModal.close();
    }, error => {
      this.form.enable();
      this.isSubmitted = false;
      this._notifyService.pushError('', 'Register failed');
    }));
  }

  ngOnDestroy(): void {
    this._subscriptions.forEach(value => value.unsubscribe());
  }
}
