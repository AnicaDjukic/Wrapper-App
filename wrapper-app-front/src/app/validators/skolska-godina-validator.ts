import { AbstractControl } from '@angular/forms';

export function skolskaGodinaValidator(
    control: AbstractControl
  ): { [key: string]: any } | null {
    const valid = /^20\d{2}\/\d{2}$/.test(control.value)
    return valid
      ? null
      : { isSkolskaGodina: true }
  }