import { Pipe, PipeTransform } from "@angular/core";
import { en } from "../../../environments/translations/en";
import { pl } from "../../../environments/translations/pl";

@Pipe({
  name: 'translatePipe'
})
export class Translate implements PipeTransform {

  transform(key: string): string {
    return localStorage.getItem('language') === 'english' ? en[key] : pl[key];
  }
}
