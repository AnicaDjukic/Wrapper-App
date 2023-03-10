import { Component, Inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { StudijskiProgramService } from '../services/studijski-program.service';

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

  constructor(private formBuilder: FormBuilder, 
    private api: StudijskiProgramService,
    @Inject(MAT_DIALOG_DATA) public editData : any, 
    private dialogRef: MatDialogRef<StudijskiProgramDialogComponent>) {}

  ngOnInit(): void {
    this.studijskiProgramForm = this.formBuilder.group({
      oznaka : ['', Validators.required],
      stepen : ['', Validators.required],
      nivo: ['', Validators.required],
      naziv: ['', Validators.required]
    })

    if(this.editData) {
      this.title = "Izmeni studijski program";
      this.actionBtn = "Sačuvaj izmene";
      this.studijskiProgramForm.controls['oznaka'].setValue(this.editData.oznaka);
      this.studijskiProgramForm.controls['stepen'].setValue(this.editData.stepen);
      this.studijskiProgramForm.controls['nivo'].setValue(this.editData.nivo);
      this.studijskiProgramForm.controls['naziv'].setValue(this.editData.naziv);
    }
  }

  add() {
    if(!this.editData) {
      if(this.studijskiProgramForm.valid) {
        this.api.post(this.studijskiProgramForm.value)
        .subscribe({
          next: () => {
            alert("Novi studijski program je uspešno dodat!");
            this.studijskiProgramForm.reset();
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
    if(!this.studijskiProgramForm.valid) {
      return
    }

    this.api.put(this.studijskiProgramForm.value, this.editData.id)
    .subscribe({
      next: () => {
        alert("Studijski program je uspešno izmenjen!");
        this.studijskiProgramForm.reset();
        this.dialogRef.close('update');
      },
      error: () => {
        alert("Greška!");
      }
    })
  }
}
