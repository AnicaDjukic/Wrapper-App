import { Component, Inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { map, Observable, startWith } from 'rxjs';
import { StudijskiProgramDto } from '../dtos/StudijskiProgramDto';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { StudentskaGrupaService } from '../services/studentska-grupa.service';
import { autocompleteValidator } from '../validators/autocomplete-validator';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-studentska-grupa-dialog',
  templateUrl: './studentska-grupa-dialog.component.html',
  styleUrls: ['./studentska-grupa-dialog.component.scss']
})
export class StudentskaGrupaDialogComponent {
  
  title: string = "Dodavanje novih studentskih grupa"
  actionBtn: string = "Sačuvaj"
  studijskiProgrami: StudijskiProgramDto[] = [];
  options: string[] = [];
  filteredOptions!: Observable<string[]>;
  studentskaGrupaForm!: FormGroup

  constructor(private formBuilder: FormBuilder, 
    private api: StudentskaGrupaService,
    @Inject(MAT_DIALOG_DATA) public data : any, 
    private dialogRef: MatDialogRef<StudentskaGrupaDialogComponent>,
    private toastr: ToastrService) {}

  ngOnInit(): void {

    this.options = this.data.options;
    this.studijskiProgrami = this.data.studijskiProgrami;

    this.studentskaGrupaForm = this.formBuilder.group({
      godina : ['', [Validators.required, Validators.min(1), Validators.max(6)]],
      //semestar: ['', Validators.required],
      brojStudenata: ['', [Validators.required, Validators.min(1)]],
      studijskiProgramId: ['', [Validators.required, autocompleteValidator(this.options)]]
    })

    if(this.data.editData) {
      this.title = "Izmeni studentsku grupu";
      this.actionBtn = "Sačuvaj izmene";
      this.studentskaGrupaForm.addControl('oznaka', new FormControl('', [Validators.required, Validators.min(1)]));
      this.studentskaGrupaForm.controls['oznaka'].setValue(this.data.editData.oznaka);
      this.studentskaGrupaForm.controls['godina'].setValue(this.data.editData.godina);
      //this.studentskaGrupaForm.controls['semestar'].setValue(this.editData.semestar);
      this.studentskaGrupaForm.controls['brojStudenata'].setValue(this.data.editData.brojStudenata);
      this.studentskaGrupaForm.controls['studijskiProgramId'].setValue(this.data.editData.studijskiProgram.oznaka + ' ' + this.data.editData.studijskiProgram.naziv + ' (' + this.data.editData.studijskiProgram.stepenStudija + ')');
    } else {
      this.studentskaGrupaForm.addControl('brojStudentskihGrupa', new FormControl('', [Validators.required, Validators.min(1)]));
    }

    this.filteredOptions = this.studentskaGrupaForm.get('studijskiProgramId')!.valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value || '')),
    );
  }

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();
    return this.options.filter(option => option.toLowerCase().includes(filterValue));
  }

  add() {
    if(!this.data.editData) {
      if(this.studentskaGrupaForm.valid) {
        let studProg : string = this.studentskaGrupaForm.value.studijskiProgramId;
        this.studentskaGrupaForm.value.studijskiProgramId = this.studijskiProgrami.filter(sp => (sp.oznaka + ' ' + sp.naziv + ' (' + sp.stepenStudija + ')') == studProg).map(value => value.id)[0];
        this.studentskaGrupaForm.value.semestar = 'Z';
        this.api.post(this.studentskaGrupaForm.value)
        .subscribe({
          next: () => {
            this.toastr.success('Nove studentske grupe su uspešno dodate!', 'Uspešno!');
            this.studentskaGrupaForm.reset();
            this.dialogRef.close('save');
          },
          error:() => {
            this.toastr.error('Došlo je do greške prilikom dodavanja novih studentskih grupa!', 'Greška!');
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
    let studProg : string = this.studentskaGrupaForm.value.studijskiProgramId;
    this.studentskaGrupaForm.value.studijskiProgramId = this.studijskiProgrami.filter(sp => (sp.oznaka + ' ' + sp.naziv + ' (' + sp.stepenStudija + ')') == studProg).map(value => value.id)[0];
    this.studentskaGrupaForm.value.semestar = 'Z';
    this.api.put(this.studentskaGrupaForm.value, this.data.editData.id)
    .subscribe({
      next: () => {
        this.toastr.success('Studentska grupa je uspešno izmenjena!', 'Uspešno!');
        this.studentskaGrupaForm.reset();
        this.dialogRef.close('update');
      },
      error: (message) => {
        if (message.error.message) {
          this.toastr.error('Studentska grupa sa oznakom <b>' + this.studentskaGrupaForm.value.oznaka + '</b>'
          + ' na <b>' + this.studentskaGrupaForm.value.godina + '.</b> godini'
          // + ' u <b>' + 'zimskom' +'</b> semestru' 
          +' na studijskom programu <b>' + studProg + '</b> već postoji!',
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
