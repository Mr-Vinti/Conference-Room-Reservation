import { Component, OnInit, Input, SimpleChanges, OnChanges, Output, EventEmitter } from '@angular/core';
import { Message } from '../../models/message.model';

@Component({
  selector: 'app-msg-banner',
  templateUrl: './msg-banner.component.html',
  styleUrls: ['./msg-banner.component.scss']
})
export class MsgBannerComponent implements OnInit, OnChanges {
  private messageListValue: Message[];
  @Input()
  get msgList() {
    return this.messageListValue;
  }
  @Output() msgListChange = new EventEmitter<Message[]>();
  set msgList(val: Message[]) {
    this.messageListValue = val;
    this.msgListChange.emit(this.messageListValue);
  }


  constructor() { }


  ngOnInit(): void {
    this.messageListValue = this.messageListValue.map((item: Message) => {
      if (item.disabled === undefined) {
        item.disabled = false;
      }
      return item;
    });
  }

  public ngOnChanges(changes: SimpleChanges) {
  }

  removeMsg(msg: Message) {
    if (!msg.disabled) {
      this.msgList  = this.msgList.filter(item => item.content !== msg.content);
    }
  }

}
