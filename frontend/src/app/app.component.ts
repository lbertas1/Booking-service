import {Component, OnDestroy, OnInit} from '@angular/core';
import {Break} from "./core/enums";
import {RxStompService} from "@stomp/ng2-stompjs";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, OnDestroy {

  public isTechnicalBreak = false;

  private topicSubscription: Subscription;

  constructor(
    private readonly _rxStompService: RxStompService
  ) { }

  ngOnInit(): void {
    this.topicSubscription = this._rxStompService
      .watch("/break/technical-break")
      .subscribe(message => {
        message.body === Break.BREAK_START.toString() ? this.isTechnicalBreak = true : this.isTechnicalBreak = false;
      })
  }

  ngOnDestroy(): void {
    this.topicSubscription.unsubscribe();
  }
}
