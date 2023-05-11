import { AbstractControl, ValidatorFn } from '@angular/forms';

export function autocompleteValidator(options: string[]): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } | null => {
    if(!control.value){
      return null;
    }
    const selectedOption = control.value
    if (options.indexOf(selectedOption) === -1) {
      return { invalidOption: true };
    }
    return null;
  };
}