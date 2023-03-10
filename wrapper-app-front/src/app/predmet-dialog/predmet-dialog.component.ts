import { Component, Inject, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { map, Observable, startWith } from 'rxjs';
import { StudijskiProgramDto } from '../dtos/StudijskiProgramDto';
import { ApiService } from '../services/api.service';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

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
  predmetForm!: FormGroup

  constructor(private formBuilder: FormBuilder, 
    private api: ApiService,
    @Inject(MAT_DIALOG_DATA) public editData : any, 
    private dialogRef: MatDialogRef<PredmetDialogComponent>) {}

  ngOnInit(): void {
    this.predmetForm = this.formBuilder.group({
      oznaka : ['', Validators.required],
      plan : ['', Validators.required],
      naziv: ['', Validators.required],
      godina: ['', Validators.required],
      brojCasovaPred : ['', Validators.required],
      studijskiProgram : ['', Validators.required],
      sifraStruke: [''],
      brojCasovaAud: ['', Validators.required],
      brojCasovaLab: ['', Validators.required],
      brojCasovaRac: ['', Validators.required]
    })

    if(this.editData) {
      this.title = "Izmeni predmet";
      this.actionBtn = "Sačuvaj izmene";
      this.predmetForm.controls['oznaka'].setValue(this.editData.oznaka);
      this.predmetForm.controls['plan'].setValue(this.editData.plan);
      this.predmetForm.controls['naziv'].setValue(this.editData.naziv);
      this.predmetForm.controls['godina'].setValue(this.editData.godina);
      this.predmetForm.controls['brojCasovaPred'].setValue(this.editData.brojCasovaPred);
      this.predmetForm.controls['studijskiProgram'].setValue(this.editData.studijskiProgram);
      this.predmetForm.controls['sifraStruke'].setValue(this.editData.sifraStruke);
      this.predmetForm.controls['brojCasovaAud'].setValue(this.editData.brojCasovaAud);
      this.predmetForm.controls['brojCasovaLab'].setValue(this.editData.brojCasovaLab);
      this.predmetForm.controls['brojCasovaRac'].setValue(this.editData.brojCasovaRac);
    }


    this.api.getAllStudijskiProgram()
    .subscribe({
      next: (res) => {
        this.studijskiProgrami = res
        res.forEach((element: StudijskiProgramDto) => {
          this.options.push(element.oznaka + ' ' + element.naziv)
        });
      }
    })
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
    if(!this.editData) {
      if(this.predmetForm.valid) {
        let studProg : string = this.predmetForm.value.studijskiProgram
        this.predmetForm.value.studijskiProgram = this.studijskiProgrami.filter(sp => sp.oznaka == studProg.split(' ')[0]).map(value => value.id)[0];
        this.api.post(this.predmetForm.value)
        .subscribe({
          next: () => {
            alert("Novi predmet je uspešno dodat!");
            this.predmetForm.reset();
            this.dialogRef.close('save');
          },
          error:() => {
            alert("Greška")
          }
        })
      }
    } else {
      this.update();
    }   
  }
  update() {
    if(!this.predmetForm.valid) {
      return
    }
    let studProg : string = this.predmetForm.value.studijskiProgram
    this.predmetForm.value.studijskiProgram = this.studijskiProgrami.filter(sp => sp.oznaka == studProg.split(' ')[0]).map(value => value.id)[0];
    this.api.put(this.predmetForm.value, this.editData.id)
    .subscribe({
      next: () => {
        alert("Predmet je uspešno izmenjen!");
        this.predmetForm.reset();
        this.dialogRef.close('update');
      },
      error: () => {
        alert("Greška!");
      }
    })
  }

}
