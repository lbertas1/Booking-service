import {MessageIndex} from "../enums";

export interface ISocketMessage {
  index: MessageIndex;
  message: string;
}
