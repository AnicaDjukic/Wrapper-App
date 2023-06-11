import { Component, Inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormArray } from '@angular/forms';
import { Observable } from 'rxjs';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { OrganizacionaJedinicaDto } from '../dtos/OrganizacionaJedinicaDto';
import { ProstorijaService } from '../services/prostorija.service';
import { autocompleteValidator } from '../validators/autocomplete-validator';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-prostorija-dialog',
  templateUrl: './prostorija-dialog.component.html',
  styleUrls: ['./prostorija-dialog.component.scss']
})
export class ProstorijaDialogComponent {

  title: string = "Nova prostorija"
  actionBtn: string = "Sačuvaj"
  organizacioneJedinice: OrganizacionaJedinicaDto[] = [];
  orgjediniceOptions: string[] = [];
  filteredOptions!: Observable<string[]>;
  prostorijaForm!: FormGroup
  counter: number = 0;
  options: string[][] = [[]];

  constructor(private formBuilder: FormBuilder,
    private api: ProstorijaService,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private dialogRef: MatDialogRef<ProstorijaDialogComponent>,
    private toastr: ToastrService) { }

  ngOnInit(): void {

    this.orgjediniceOptions = this.data.options;
    this.organizacioneJedinice = this.data.organizacioneJedinice;

    this.prostorijaForm = this.formBuilder.group({
      oznaka: ['', Validators.required],
      tip: ['', Validators.required],
      kapacitet: ['', [Validators.required, Validators.min(1)]],
      orgJedinica: this.formBuilder.array([])
    });

    if (this.data.editData) {
      this.title = "Izmeni prostoriju";
      this.actionBtn = "Sačuvaj izmene";
      this.prostorijaForm.controls['oznaka'].setValue(this.data.editData.oznaka);
      this.prostorijaForm.controls['tip'].setValue(this.data.editData.tip);
      this.prostorijaForm.controls['kapacitet'].setValue(this.data.editData.kapacitet);
      if(this.data.editData.orgJedinica) {
        for (let orgJed of this.data.editData.orgJedinica) {
          this.orgJedinicaFieldAsFormArray.push(this.formBuilder.control(orgJed.naziv, [Validators.required, autocompleteValidator(this.orgjediniceOptions)]));
        }
      }
    }
  }

  get orgJedinicaFieldAsFormArray(): any {
    return this.prostorijaForm.get('orgJedinica') as FormArray;
  }

  addControl(): void {
    this.orgJedinicaFieldAsFormArray.push(this.formBuilder.control('', [Validators.required, autocompleteValidator(this.orgjediniceOptions)]));
  }

  remove(i: number): void {
    this.orgJedinicaFieldAsFormArray.removeAt(i);
  }

  public _filter($event: any, index: number) {
    const value = $event?.target?.value;
    if (value == '' || !value) {
      this.options[index] = this.orgjediniceOptions;
      return;
    }
    
    const filterValue = value.toLowerCase();
    let all = this.orgjediniceOptions;
    let options = all.filter(option => option.toLowerCase().includes(filterValue));
    this.options[index] = options;
  }

  add() {
    if (!this.data.editData) {
      if (this.prostorijaForm.valid) {
        let orgJedinice = [];
        for (let org of this.prostorijaForm.value.orgJedinica) {
          org = this.organizacioneJedinice.filter(o => o.naziv == org).map(value => value.id)[0];
          orgJedinice.push(org);
        }
        this.prostorijaForm.value.orgJedinica = orgJedinice;
        console.log(this.prostorijaForm);
        this.api.post(this.prostorijaForm.value)
          .subscribe({
            next: () => {
              this.toastr.success('Nova prostorija je uspešno dodata!', 'Uspešno!');
              this.prostorijaForm.reset();
              this.dialogRef.close('save');
            },
            error: (message) => {
              if (message.error.message) {
                this.toastr.error('Prostorija sa oznakom <b>' + this.prostorijaForm.value.oznaka + '</b> već postoji!',
                  'Greška!', {
                  enableHtml: true,
                  closeButton: true,
                  timeOut: 10000
                });
                console.log(message);
              } else {
                this.toastr.error('Došlo je do greške prilikom dodavanja nove prostorije!', 'Greška!');
              }
            }
          })
      }
    } else {
      this.update();
    }
  }

  update() {
    if (!this.prostorijaForm.valid) {
      return
    }
    let orgJedinice = [];
    for (let org of this.prostorijaForm.value.orgJedinica) {
      org = this.organizacioneJedinice.filter(o => o.naziv == org).map(value => value.id)[0];
      orgJedinice.push(org);
    }
    this.prostorijaForm.value.orgJedinica = orgJedinice;
    this.api.put(this.prostorijaForm.value, this.data.editData.id)
      .subscribe({
        next: () => {
          this.toastr.success('Prostorija je uspešno izmenjena!', 'Uspešno!');
          this.prostorijaForm.reset();
          this.dialogRef.close('update');
        },
        error: (message) => {
          if (message.error.message) {
            this.toastr.error('Prostorija sa oznakom <b>' + this.prostorijaForm.value.oznaka + '</b> već postoji!',
              'Greška!', {
              enableHtml: true,
              closeButton: true,
              timeOut: 10000
            });
            console.log(message);
          } else {
            this.toastr.error('Došlo je do greške prilikom izmene prostorije!', 'Greška!');
          }
        }
      })
  }
}
