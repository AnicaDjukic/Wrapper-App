import { Component, Inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { map, Observable, startWith } from 'rxjs';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { OrganizacionaJedinicaDto } from '../dtos/OrganizacionaJedinicaDto';
import { PredavacService } from '../services/predavac.service';
import { autocompleteValidator } from '../validators/autocomplete-validator';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-predavac-dialog',
  templateUrl: './predavac-dialog.component.html',
  styleUrls: ['./predavac-dialog.component.scss']
})
export class PredavacDialogComponent {

  title: string = "Novi predavač"
  actionBtn: string = "Sačuvaj"
  organizacioneJedinice: OrganizacionaJedinicaDto[] = [];
  options: string[] = [];
  filteredOptions!: Observable<string[]>;
  predavacForm!: FormGroup

  constructor(private formBuilder: FormBuilder,
    private api: PredavacService,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private dialogRef: MatDialogRef<PredavacDialogComponent>,
    private toastr: ToastrService) { }

  ngOnInit(): void {

    this.options = this.data.options;
    this.organizacioneJedinice = this.data.organizacioneJedinice;

    this.predavacForm = this.formBuilder.group({
      oznaka: ['', Validators.required],
      ime: ['', Validators.required],
      prezime: ['', Validators.required],
      titula: [''],
      organizacijaFakulteta: [false],
      dekanat: [false],
      orgJedinica: ['', [Validators.required, autocompleteValidator(this.options)]],
    })

    if (this.data.editData) {
      this.title = "Izmeni predavača";
      this.actionBtn = "Sačuvaj izmene";
      this.predavacForm.controls['oznaka'].setValue(this.data.editData.oznaka);
      this.predavacForm.controls['ime'].setValue(this.data.editData.ime);
      this.predavacForm.controls['prezime'].setValue(this.data.editData.prezime);
      this.predavacForm.controls['titula'].setValue(this.data.editData.titula);
      this.predavacForm.controls['organizacijaFakulteta'].setValue(this.data.editData.organizacijaFakulteta);
      this.predavacForm.controls['dekanat'].setValue(this.data.editData.dekanat);
      this.predavacForm.controls['orgJedinica'].setValue(this.data.editData.orgJedinica);
    }

    this.filteredOptions = this.predavacForm.get('orgJedinica')!.valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value || '')),
    );
  }

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();
    return this.options.filter(option => option.toLowerCase().includes(filterValue));
  }

  add() {
    if (!this.data.editData) {
      if (this.predavacForm.valid) {
        let orgJedinica: string = this.predavacForm.value.orgJedinica
        this.predavacForm.value.orgJedinica = this.organizacioneJedinice.filter(o => o.naziv == orgJedinica).map(value => value.id)[0];
        this.api.post(this.predavacForm.value)
          .subscribe({
            next: () => {
              this.toastr.success('Novi predavač je uspešno dodat!', 'Uspešno!');
              this.predavacForm.reset();
              this.dialogRef.close('save');
            },
            error: (message) => {
              if (message.error.message) {
                this.toastr.error('Predavač sa oznakom <b>' + this.predavacForm.value.oznaka + '</b> već postoji!',
                  'Greška!', {
                  enableHtml: true,
                  closeButton: true,
                  timeOut: 10000
                });
                console.log(message);
              } else {
                this.toastr.error('Došlo je do greške prilikom dodavanja novog predavača!', 'Greška!');
              }
            }
          })
      }
    } else {
      this.update();
    }
  }

  update() {
    if (!this.predavacForm.valid) {
      return
    }
    let orgJedinica: string = this.predavacForm.value.orgJedinica
    this.predavacForm.value.orgJedinica = this.organizacioneJedinice.filter(o => o.naziv == orgJedinica).map(value => value.id)[0];
    this.api.put(this.predavacForm.value, this.data.editData.id)
      .subscribe({
        next: () => {
          this.toastr.success('Predavač je uspešno izmenjen!', 'Uspešno!');
          this.predavacForm.reset();
          this.dialogRef.close('update');
        },
        error: (message) => {
          if (message.error.message) {
            this.toastr.error('Predavač sa oznakom <b>' + this.predavacForm.value.oznaka + '</b> već postoji!',
              'Greška!', {
              enableHtml: true,
              closeButton: true,
              timeOut: 10000
            });
            console.log(message);
          } else {
            this.toastr.error('Došlo je do greške prilikom izmene predavača!', 'Greška!');
          }
        }
      })
  }
}
