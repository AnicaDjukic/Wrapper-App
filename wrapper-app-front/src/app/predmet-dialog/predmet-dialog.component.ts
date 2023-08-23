import { Component, Inject, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { map, Observable, startWith } from 'rxjs';
import { StudijskiProgramDto } from '../dtos/StudijskiProgramDto';
import { ApiService } from '../services/api.service';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { autocompleteValidator } from '../validators/autocomplete-validator';

@Component({
  selector: 'app-predmet-dialog',
  templateUrl: './predmet-dialog.component.html',
  styleUrls: ['./predmet-dialog.component.scss']
})
export class PredmetDialogComponent implements OnInit {

  title: string = "Novi predmet"
  actionBtn: string = "Sačuvaj"
  studijskiProgrami: StudijskiProgramDto[] = [];
  options: string[] = [];
  filteredOptions!: Observable<string[]>;
  predmetForm!: FormGroup;

  constructor(private formBuilder: FormBuilder,
    private api: ApiService,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private dialogRef: MatDialogRef<PredmetDialogComponent>,
    private toastr: ToastrService) { }

  ngOnInit() {

    this.options = this.data.options;
    this.studijskiProgrami = this.data.studijskiProgrami;

    this.predmetForm = this.formBuilder.group({
      oznaka: ['', Validators.required],
      plan: ['', [Validators.required, Validators.min(1)]],
      naziv: ['', Validators.required],
      godina: ['', [Validators.required, Validators.min(1), Validators.max(6)]],
      brojCasovaPred: ['', [Validators.required, Validators.min(0)]],
      studijskiProgram: ['', [Validators.required, autocompleteValidator(this.options)]],
      sifraStruke: [''],
      brojCasovaAud: ['', [Validators.required, Validators.min(0)]],
      brojCasovaLab: ['', [Validators.required, Validators.min(0)]],
      brojCasovaRac: ['', [Validators.required, Validators.min(0)]]
    })

    if (this.data.editData) {
      this.title = "Izmeni predmet";
      this.actionBtn = "Sačuvaj izmene";
      this.predmetForm.controls['oznaka'].setValue(this.data.editData.oznaka);
      this.predmetForm.controls['plan'].setValue(this.data.editData.plan);
      this.predmetForm.controls['naziv'].setValue(this.data.editData.naziv);
      this.predmetForm.controls['godina'].setValue(this.data.editData.godina);
      this.predmetForm.controls['brojCasovaPred'].setValue(this.data.editData.brojCasovaPred);
      this.predmetForm.controls['studijskiProgram'].setValue(this.data.editData.studijskiProgram.oznaka + ' ' + this.data.editData.studijskiProgram.naziv + ' (' + this.data.editData.studijskiProgram.stepenStudija + ')');
      this.predmetForm.controls['sifraStruke'].setValue(this.data.editData.sifraStruke);
      this.predmetForm.controls['brojCasovaAud'].setValue(this.data.editData.brojCasovaAud);
      this.predmetForm.controls['brojCasovaLab'].setValue(this.data.editData.brojCasovaLab);
      this.predmetForm.controls['brojCasovaRac'].setValue(this.data.editData.brojCasovaRac);
    }

    this.filteredOptions = this.predmetForm.get('studijskiProgram')!.valueChanges.pipe(
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
      if (this.predmetForm.valid) {
        let studProg: string = this.predmetForm.value.studijskiProgram
        this.predmetForm.value.studijskiProgram = this.studijskiProgrami.filter(sp => (sp.oznaka + ' ' + sp.naziv + ' (' + sp.stepenStudija + ')') == studProg).map(value => value.id)[0];
        this.api.post(this.predmetForm.value)
          .subscribe({
            next: () => {
              this.toastr.success('Novi predmet je uspešno dodat!', 'Uspešno!');
              this.predmetForm.reset();
              this.dialogRef.close('save');
            },
            error: (message) => {
              if (message.error.message) {
                this.toastr.error('Predmet sa oznakom <b>' + this.predmetForm.value.oznaka
                  + '</b> već postoji u studijskom programu <b>' + studProg
                  + '</b> pod planom <b>' + this.predmetForm.value.plan + '</b>!',
                  'Greška!', {
                  enableHtml: true,
                  closeButton: true,
                  timeOut: 10000
                });
                console.log(message);
              } else {
                this.toastr.error('Došlo je do greške prilikom dodavanja novog predmeta!', 'Greška!');
              }

            }
          })
      }
    } else {
      this.update();
    }
  }

  update() {
    if (!this.predmetForm.valid) {
      return
    }
    console.log(this.predmetForm.value);
    let studProg: string = this.predmetForm.value.studijskiProgram
    this.predmetForm.value.studijskiProgram = this.studijskiProgrami.filter(sp => (sp.oznaka + ' ' + sp.naziv + ' (' + sp.stepenStudija + ')') == studProg).map(value => value.id)[0];
    console.log(this.predmetForm.value);
    this.api.put(this.predmetForm.value, this.data.editData.id)
      .subscribe({
        next: () => {
          this.toastr.success('Predmet je uspešno izmenjen!', 'Uspešno!');
          this.predmetForm.reset();
          this.dialogRef.close('update');
        },
        error: (message) => {
          if (message.error.message) {
            this.toastr.error('Predmet sa oznakom <b>' + this.predmetForm.value.oznaka
              + '</b> već postoji u studijskom programu <b>' + studProg
              + '</b> pod planom <b>' + this.predmetForm.value.plan + '</b>!',
              'Greška!', {
              enableHtml: true,
              closeButton: true,
              timeOut: 10000
            });
            console.log(message);
          } else {
            this.toastr.error('Došlo je do greške prilikom izmene predmeta!', 'Greška!');
          }
        }
      })
  }

}
