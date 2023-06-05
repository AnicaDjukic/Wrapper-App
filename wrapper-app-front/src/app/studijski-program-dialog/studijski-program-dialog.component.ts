import { Component, Inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { StudijskiProgramService } from '../services/studijski-program.service';
import { ToastrService } from 'ngx-toastr';
import { RealizacijaService } from '../services/realizacija.service';

@Component({
  selector: 'app-studijski-program-dialog',
  templateUrl: './studijski-program-dialog.component.html',
  styleUrls: ['./studijski-program-dialog.component.scss']
})
export class StudijskiProgramDialogComponent {
  title: string = "Novi studijski program"
  actionBtn: string = "Sačuvaj"
  options: string[] = [];
  studijskiProgramForm!: FormGroup
  submitted!: boolean

  constructor(private formBuilder: FormBuilder,
    private studijskiProgramApi: StudijskiProgramService,
    private realizacijaApi: RealizacijaService,
    @Inject(MAT_DIALOG_DATA) public editData: any,
    private dialogRef: MatDialogRef<StudijskiProgramDialogComponent>,
    private toastr: ToastrService) { }

  ngOnInit(): void {
    this.studijskiProgramForm = this.formBuilder.group({
      oznaka: ['', Validators.required],
      stepen: ['', Validators.required],
      nivo: [''],
      naziv: ['', Validators.required]
    })

    if (this.editData) {
      this.title = "Izmeni studijski program";
      this.actionBtn = "Sačuvaj izmene";
      this.studijskiProgramForm.controls['oznaka'].setValue(this.editData.oznaka);
      this.studijskiProgramForm.controls['stepen'].setValue(this.editData.stepenStudija);
      this.studijskiProgramForm.controls['nivo'].setValue('');
      this.studijskiProgramForm.controls['naziv'].setValue(this.editData.naziv);
    }
  }

  add() {
    this.submitted = true;
    if (!this.editData) {
      if (this.studijskiProgramForm.valid) {
        this.fillStepenAndNivo();
        this.realizacijaApi.addStudijskiProgram(this.studijskiProgramForm.value)
          .subscribe({
            next: () => {
              this.toastr.success('Novi studijski program je uspešno dodat!', 'Uspešno!');
              this.studijskiProgramForm.reset();
              this.submitted = false;
              this.dialogRef.close('save');
            },
            error: (message) => {
              if (message.error.message) {
                this.toastr.error('Studijski program sa oznakom <b>' + this.studijskiProgramForm.value.oznaka + '</b> ' +
                'na <b>' + this.studijskiProgramForm.value.nivo + '</b> već postoji!',
                  'Greška!', {
                  enableHtml: true,
                  closeButton: true,
                  timeOut: 10000
                });
                console.log(message);
              } else {
                this.toastr.error('Došlo je do greške prilikom dodavanja novog studijskog programa!', 'Greška!');
              }
            }
          })
      }
    } else {
      this.update();
    }
  }
  update() {
    if (!this.studijskiProgramForm.valid) {
      return
    }

    this.fillStepenAndNivo();

    this.studijskiProgramApi.put(this.studijskiProgramForm.value, this.editData.id)
      .subscribe({
        next: () => {
          this.toastr.success('Studijski program je uspešno izmenjen!', 'Uspešno!');
          this.studijskiProgramForm.reset();
          this.submitted = false;
          this.dialogRef.close('update');
        },
        error: (message) => {
          if (message.error.message) {
            this.toastr.error('Studijski program sa oznakom <b>' + this.studijskiProgramForm.value.oznaka + '</b> već postoji!',
              'Greška!', {
              enableHtml: true,
              closeButton: true,
              timeOut: 10000
            });
            console.log(message);
          } else {
            this.toastr.error('Došlo je do greške prilikom izmene studijskog programa!', 'Greška!');
          }
        }
      })
  }

  fillStepenAndNivo() {
    if (this.studijskiProgramForm.value.stepen == 'OSNOVNE AKADEMSKE STUDIJE') {
      this.studijskiProgramForm.value.stepen = 1;
      this.studijskiProgramForm.value.nivo = 1;
    } else if (this.studijskiProgramForm.value.stepen == 'OSNOVNE STRUKOVNE STUDIJE') {
      this.studijskiProgramForm.value.stepen = 1;
      this.studijskiProgramForm.value.nivo = 2;
    } else if (this.studijskiProgramForm.value.stepen == 'MASTER AKADEMSKE STUDIJE') {
      this.studijskiProgramForm.value.stepen = 2;
      this.studijskiProgramForm.value.nivo = 1;
    } else {
      this.studijskiProgramForm.value.stepen = 2;
      this.studijskiProgramForm.value.nivo = 5;
    }
  }
}
