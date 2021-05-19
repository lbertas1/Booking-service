import {Component, OnInit, ViewChild} from '@angular/core';
import {NgxSmartModalComponent} from "ngx-smart-modal";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Subscription} from "rxjs";
import {JwtService, NotifyService, UserService} from "../../../core/services";
import {IChangePassword} from "../../../core/interfaces";

@Component({
  selector: 'app-add-opinion-modal',
  templateUrl: './add-opinion-modal.component.html',
  styleUrls: ['./add-opinion-modal.component.scss']
})
export class AddOpinionModalComponent implements OnInit {

  @ViewChild('changePasswordModal') changePasswordModal: NgxSmartModalComponent;

  public form: FormGroup;

  public isSubmitted = false;

  private _subscriptions: Subscription[] = [];

  private readonly _formFields = {
    opinion: ['', Validators.required],
    evaluation: ['', Validators.required]
  };

  constructor(
    private readonly _formBuilder: FormBuilder,
    private readonly _userService: UserService,
    private readonly _notifyService: NotifyService
  ) { }

  ngOnInit(): void {
    this.form = this._formBuilder.group(this._formFields);
  }

  public get formControls() {
    return this.form.controls;
  }

  public submit(): void {
    this.isSubmitted = true;
    this.form.disable();

    if (!this.form.valid) {
      this.form.enable();
      this.isSubmitted = false;
      return;
    }

    // this._subscriptions.push(this.(changePassword).subscribe(() => {
    //   this.isSubmitted = false;
    //   this._notifyService.pushSuccess('', 'Update success!');
    //   this.changePasswordModal.close();
    // }, error => {
    //   this.form.enable();
    //   this.isSubmitted = false;
    //   this._notifyService.pushError('', 'Update failed');
    // }));
  }

  ngOnDestroy(): void {
    this._subscriptions.forEach(value => value.unsubscribe());
  }

}
