import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {NgxSmartModalComponent} from "ngx-smart-modal";

@Component({
  selector: 'app-confirmation-modal',
  templateUrl: './confirmation-modal.component.html',
  styleUrls: ['./confirmation-modal.component.scss']
})
export class ConfirmationModalComponent implements OnInit {

  @ViewChild('confirmationModal') confirmationModal: NgxSmartModalComponent;

  @Input() header: string;

  @Input() message: string;

  @Output() confirmation = new EventEmitter<boolean>();

  constructor() { }

  ngOnInit(): void {
  }

  confirmationValue(confirmationValue: boolean) {
    this.confirmation.emit(confirmationValue);
    this.confirmationModal.close();
  }
}
