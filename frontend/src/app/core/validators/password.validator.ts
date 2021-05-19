import { IValidationResult } from "../interfaces";
import { FormControl } from "@angular/forms";

export class PasswordValidator {
  public static strength({ value }: FormControl): IValidationResult {
    const hasNumber = /[0-9]+/.test(value);
    const hasUpperCase = /[A-Z]+/.test(value);
    const hasLowerCase = /[a-z]+/.test(value);
    const hasMinMaxLength = /.{8,100}/.test(value);
    const hasSymbol = /[!@#$%^&*()_+=\[{\]};:<>|./?,-]/.test(value);

    if (!(hasNumber && hasUpperCase && hasLowerCase && hasMinMaxLength && hasSymbol)) {
      return {
        strength: true
      };
    }

    return null;
  }
}
