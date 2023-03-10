import { Component, Inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { map, Observable, startWith } from 'rxjs';
import { StudijskiProgramDto } from '../dtos/StudijskiProgramDto';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { StudentskaGrupaService } from '../services/studentska-grupa.service';
import { StudijskiProgramService } from '../services/studijski-program.service';

@Component({
  selector: 'app-studentska-grupa-dialog',
  templateUrl: './studentska-grupa-dialog.component.html',
  styleUrls: ['./studentska-grupa-dialog.component.scss']
})
export class StudentskaGrupaDialogComponent {
  
  title: string = "Nova studentska grupa"
  actionBtn: string = "Sačuvaj"
  studijskiProgrami: StudijskiProgramDto[] = [];
  options: string[] = [];
  filteredOptions!: Observable<string[]>;
  studentskaGrupaForm!: FormGroup

  constructor(private formBuilder: FormBuilder, 
    private api: StudentskaGrupaService,
    private studijskiProgramApi: StudijskiProgramService,
    @Inject(MAT_DIALOG_DATA) public editData : any, 
    private dialogRef: MatDialogRef<StudentskaGrupaDialogComponent>) {}

  ngOnInit(): void {
    this.studentskaGrupaForm = this.formBuilder.group({
      oznaka : ['', Validators.required],
      godina : ['', Validators.required],
      semestar: ['', Validators.required],
      brojStudenata: ['', Validators.required],
      studijskiProgram: ['', Validators.required]
    })

    if(this.editData) {
      this.title = "Izmeni studentsku grupu";
      this.actionBtn = "Sačuvaj izmene";
      this.studentskaGrupaForm.controls['oznaka'].setValue(this.editData.oznaka);
      this.studentskaGrupaForm.controls['godina'].setValue(this.editData.godina);
      this.studentskaGrupaForm.controls['semestar'].setValue(this.editData.semestar);
      this.studentskaGrupaForm.controls['brojStudenata'].setValue(this.editData.brojStudenata);
      this.studentskaGrupaForm.controls['studijskiProgram'].setValue(this.editData.studijskiProgram);
    }


    this.studijskiProgramApi.getAll()
    .subscribe({
      next: (res) => {
        this.studijskiProgrami = res
        res.forEach((element: StudijskiProgramDto) => {
          this.options.push(element.oznaka + ' ' + element.naziv)
        });
      }
    })
    this.filteredOptions = this.studentskaGrupaForm.get('studijskiProgram')!.valueChanges.pipe(
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
      if(this.studentskaGrupaForm.valid) {
        let studProg : string = this.studentskaGrupaForm.value.studijskiProgram
        this.studentskaGrupaForm.value.studijskiProgram = this.studijskiProgrami.filter(sp => sp.oznaka == studProg.split(' ')[0]).map(value => value.id)[0];
        this.api.post(this.studentskaGrupaForm.value)
        .subscribe({
          next: () => {
            alert("Nova studentska grupa je uspešno dodata!");
            this.studentskaGrupaForm.reset();
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
    if(!this.studentskaGrupaForm.valid) {
      return
    }
    let studProg : string = this.studentskaGrupaForm.value.studijskiProgram
    this.studentskaGrupaForm.value.studijskiProgram = this.studijskiProgrami.filter(sp => sp.oznaka == studProg.split(' ')[0]).map(value => value.id)[0];
    this.api.put(this.studentskaGrupaForm.value, this.editData.id)
    .subscribe({
      next: () => {
        alert("Studentska grupa je uspešno izmenjena!");
        this.studentskaGrupaForm.reset();
        this.dialogRef.close('update');
      },
      error: () => {
        alert("Greška!");
      }
    })
  }
}
