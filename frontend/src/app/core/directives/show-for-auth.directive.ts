import { Directive, Input, OnInit, TemplateRef, ViewContainerRef } from '@angular/core';
import { JwtService } from "../services";

@Directive({
  selector: '[appShowForAuth]'
})
export class ShowForAuthDirective implements OnInit {

  @Input() appShowForAuth: boolean;

  private _isAuthenticated: boolean;

  constructor(
    private readonly _jwtService: JwtService,
    private readonly _templateRef: TemplateRef<any>,
    private readonly _viewContainer: ViewContainerRef
  ) {
  }

  ngOnInit(): void {
    this._jwtService.isAuthenticated$.subscribe(value => {
      if (this._isAuthenticated !== value) {
        if (this.appShowForAuth) {
          value ? this._viewContainer.createEmbeddedView(this._templateRef) : this._viewContainer.clear();
        } else {
          value ? this._viewContainer.clear() : this._viewContainer.createEmbeddedView(this._templateRef);
        }
        this._isAuthenticated = value;
      }
    });
  }
}
