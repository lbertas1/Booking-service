import { Injectable } from "@angular/core";
import { NgxSmartModalService } from "ngx-smart-modal";

@Injectable({
  providedIn: 'root'
})
export class ModalService {
  constructor(
    private readonly _modalService: NgxSmartModalService
  ) { }

  public open(id: string): void {
    this._modalService.open(id);
  }
}
