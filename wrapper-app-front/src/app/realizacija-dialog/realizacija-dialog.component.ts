import { Component, Inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormControl, FormArray } from '@angular/forms';
import { map, Observable, startWith } from 'rxjs';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr'
import { RealizacijaService } from '../services/realizacija.service';
import { ApiService } from '../services/api.service';
import { PredmetDto } from '../dtos/PredmetDto';
import { PredavacService } from '../services/predavac.service';

@Component({
  selector: 'app-realizacija-dialog',
  templateUrl: './realizacija-dialog.component.html',
  styleUrls: ['./realizacija-dialog.component.scss']
})
export class RealizacijaDialogComponent {
  title!: string;
  actionBtn: string = "Sačuvaj"
  predmeti: PredmetDto[] = [];
  options: string[] = [];
  options2: string[] = [];
  filteredOptions!: Observable<string[]>;
  filteredOptions2!: Observable<string[]>;
  filteredOptions3!: Observable<string[]>;
  filteredOptions4!: Observable<string[]>;
  predmetForm!: FormGroup
  show = false

  constructor(private formBuilder: FormBuilder, 
    private api: RealizacijaService,
    private predmetApi: ApiService,
    private predavacApi: PredavacService,
    @Inject(MAT_DIALOG_DATA) public editData : any, 
    private dialogRef: MatDialogRef<RealizacijaDialogComponent>,
    private toastr: ToastrService) {}

  ngOnInit(): void {
    this.predmetForm = this.formBuilder.group({
      studijskiProgram: new FormControl({value: '', disabled: true}),
      predmet : ['', Validators.required],
      godina: new FormControl({value: '', disabled: true}),
      profesor: ['', Validators.required],
      ostaliProfesori: this.formBuilder.array([]),
      asistentZauzeca: this.formBuilder.array([])
    })

    if(this.editData) {
      this.title = "Dodavanje predmeta u realizaciju"
      console.log(this.editData);
      this.api.get(this.editData)
      .subscribe({
        next: (res) => {
          console.log(res);
          this.predmetForm.controls['studijskiProgram'].setValue(res.studijskiProgram);
        }
      })

      this.predmetApi.getByStudijskiProgram(this.editData)
      .subscribe({
        next: (res) => {
          console.log(res);
          for(let predmet of res) {
            this.options.push(predmet.oznaka + " " + predmet.naziv);
          }
          this.predmeti = res;
        }
      })

      this.predavacApi.getAll()
      .subscribe({
        next: (res) => {
          console.log(res);
          for(let predavac of res) {
            let opt = predavac.titula + " " + predavac.ime + " " + predavac.prezime + " (" + predavac.orgJedinica + ")";
            this.options2.push(opt.trim());
          }
        }
      })
    }

    // if(this.editData) {
    //   this.title = "Izmeni predmet";
    //   this.actionBtn = "Sačuvaj izmene";
    //   this.predmetForm.controls['oznaka'].setValue(this.editData.oznaka);
    //   this.predmetForm.controls['plan'].setValue(this.editData.plan);
    //   this.predmetForm.controls['naziv'].setValue(this.editData.naziv);
    //   this.predmetForm.controls['godina'].setValue(this.editData.godina);
    // }

    this.filteredOptions = this.predmetForm.get('predmet')!.valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value || '')),
    );

    this.filteredOptions2 = this.predmetForm.get('profesor')!.valueChanges.pipe(
      startWith(''),
      map(value => this._filter2(value || '')),
    );

    this.filteredOptions3 = this.predmetForm.get('ostaliProfesori')!.valueChanges.pipe(
      startWith(''),
      map(value => this._filter3(value || '', this.ostaliProfesoriFieldsAsFormArray.length - 1)),
    );

    this.filteredOptions4 = this.predmetForm.get('ostaliProfesori')!.valueChanges.pipe(
      startWith(''),
      map(value => this._filter3(value || '', this.ostaliProfesoriFieldsAsFormArray.length - 1)),
    );
  }

  get ostaliProfesoriFieldsAsFormArray(): any {
    return this.predmetForm.get('ostaliProfesori') as FormArray;
  }

  addControl(): void {
    this.ostaliProfesoriFieldsAsFormArray.push(this.formBuilder.control(''));
  }

  remove(i: number): void {
    this.ostaliProfesoriFieldsAsFormArray.removeAt(i);
  }

  asistentZauzece(): any {
    return this.formBuilder.group({
      asistentId: this.formBuilder.control(''),
      brojTermina: this.formBuilder.control('')
    });
  }

  get asistentZauzecaFieldsAsFormArray(): any {
    return this.predmetForm.get('asistentZauzeca') as FormArray;
  }

  addAsistent() {
    this.asistentZauzecaFieldsAsFormArray.push(this.asistentZauzece())
  }

  removeAsistent(i: number): void {
    this.asistentZauzecaFieldsAsFormArray.removeAt(i);
  }

  private _filter(value: string): string[] {
    if(value == '')
      return this.options;

    const filterValue = value.toLowerCase();

    return this.options.filter(option => option.toLowerCase().includes(filterValue));
  }

  private _filter2(value: string): string[] {
    if(value == '')
      return this.options2;

    const filterValue = value.toLowerCase();
    let all = this.options2;
    return all.filter(option => option.toLowerCase().includes(filterValue));
  }

  private _filter3(value: string, index: number): string[] {
    if (value == '')
      return this.options2;
    const filterValue = value[index].toLowerCase();
    let all = this.options2;
    return all.filter(option => option.toLowerCase().includes(filterValue));
  }

  get(predmet : string) {
    // this.selected = studijskiProgram;
    let predmetId = this.predmeti.filter(sp => sp.oznaka == predmet.split(' ')[0]).map(value => value.id)[0];
    this.predmetApi.get(predmetId)
    .subscribe({
      next:(res) => {
        console.log(res);
        this.show = true;
        this.predmetForm.controls['godina'].setValue(res.godina);
        // console.log(res.predmetPredavaci);
        // this.dataSource = res.predmetPredavaci;
      }
    });
  }

  add() {
    console.log(this.predmetForm.value);
    // if(!this.editData) {
    //   if(this.predmetForm.valid) {
    //     let studProg : string = this.predmetForm.value.studijskiProgram
    //     this.predmetForm.value.studijskiProgram = this.predmeti.filter(sp => sp.oznaka == studProg.split(' ')[0]).map(value => value.id)[0];
    //     this.api.post(this.predmetForm.value)
    //     .subscribe({
    //       next: () => {
    //         this.toastr.success('Novi predmet je uspešno dodat!', 'Uspešno!');
    //         this.predmetForm.reset();
    //         this.dialogRef.close('save');
    //       },
    //       error:() => {
    //         this.toastr.error('Došlo je do greške prilikom brisanja predmeta!', 'Greška!');
    //       }
    //     })
    //   }
    // } else {
    //   this.update();
    // }   
  }

  update() {
    if(!this.predmetForm.valid) {
      return
    }
    let studProg : string = this.predmetForm.value.studijskiProgram
    this.predmetForm.value.studijskiProgram = this.predmeti.filter(sp => sp.oznaka == studProg.split(' ')[0]).map(value => value.id)[0];
    // this.api.put(this.predmetForm.value, this.editData.id)
    // .subscribe({
    //   next: () => {
    //     this.toastr.success('Predmet je uspešno izmenjen!', 'Uspešno!');
    //     this.predmetForm.reset();
    //     this.dialogRef.close('update');
    //   },
    //   error: () => {
    //     this.toastr.error('Došlo je do greške prilikom izmene predmeta!', 'Greška!');
    //   }
    // })
  }
}
