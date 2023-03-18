import { Component, Inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormArray } from '@angular/forms';
import { map, Observable, startWith } from 'rxjs';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { OrganizacionaJedinicaDto } from '../dtos/OrganizacionaJedinicaDto';
import { ProstorijaService } from '../services/prostorija.service';
import { PredavacService } from '../services/predavac.service';

@Component({
  selector: 'app-prostorija-dialog',
  templateUrl: './prostorija-dialog.component.html',
  styleUrls: ['./prostorija-dialog.component.scss']
})
export class ProstorijaDialogComponent {

  title: string = "Nova prostorija"
  actionBtn: string = "Sačuvaj"
  organizacioneJedinice: OrganizacionaJedinicaDto[] = [];
  options: string[] = [];
  filteredOptions!: Observable<string[]>;
  prostorijaForm!: FormGroup
  counter: number = 0;

  constructor(private formBuilder: FormBuilder,
    private api: ProstorijaService,
    private predavacApi: PredavacService,
    @Inject(MAT_DIALOG_DATA) public editData: any,
    private dialogRef: MatDialogRef<ProstorijaDialogComponent>) { }

  ngOnInit(): void {
    this.prostorijaForm = this.formBuilder.group({
      oznaka: ['', Validators.required],
      tip: ['', Validators.required],
      kapacitet: ['', Validators.required],
      orgJedinica: this.formBuilder.array([])
    })

    if (this.editData) {
      this.title = "Izmeni prostoriju";
      this.actionBtn = "Sačuvaj izmene";
      this.prostorijaForm.controls['oznaka'].setValue(this.editData.oznaka);
      this.prostorijaForm.controls['tip'].setValue(this.editData.tip);
      this.prostorijaForm.controls['kapacitet'].setValue(this.editData.kapacitet);
      if(this.editData.orgJedinica) {
        for (let orgJed of this.editData.orgJedinica) {
          this.orgJedinicaFieldAsFormArray.push(this.formBuilder.control(orgJed));
        }
      }
    }

    this.predavacApi.getAllKatedra()
      .subscribe({
        next: (res) => {
          this.organizacioneJedinice = res;
          res.forEach((element: OrganizacionaJedinicaDto) => {
            this.options.push(element.naziv)
          });
        }
      })

    this.predavacApi.getAllDepartman()
      .subscribe({
        next: (res) => {
          this.organizacioneJedinice.push(...res);
          res.forEach((element: OrganizacionaJedinicaDto) => {
            this.options.push(element.naziv)
          });
        }
      })

    this.filteredOptions = this.prostorijaForm.get('orgJedinica')!.valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value || '', this.orgJedinicaFieldAsFormArray.length - 1)),
    );
  }

  get orgJedinicaFieldAsFormArray(): any {
    return this.prostorijaForm.get('orgJedinica') as FormArray;
  }

  addControl(): void {
    this.orgJedinicaFieldAsFormArray.push(this.formBuilder.control(''));
  }

  remove(i: number): void {
    this.orgJedinicaFieldAsFormArray.removeAt(i);
  }

  private _filter(value: string, index: number): string[] {
    if (value == '')
      return this.options;
    const filterValue = value[index].toLowerCase();
    let all = this.options;
    let option = all.filter(option => option.toLowerCase().includes(filterValue));
    return option;
  }

  add() {
    if (!this.editData) {
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
              alert("Nova prostorija je uspešno dodata!");
              this.prostorijaForm.reset();
              this.dialogRef.close('save');
            },
            error: () => {
              alert("Greška")
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
    this.api.put(this.prostorijaForm.value, this.editData.id)
      .subscribe({
        next: () => {
          alert("Prostorija je uspešno izmenjena!");
          this.prostorijaForm.reset();
          this.dialogRef.close('update');
        },
        error: () => {
          alert("Greška!");
        }
      })
  }
}
