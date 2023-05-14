import { Component, Inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormControl, FormArray } from '@angular/forms';
import { map, Observable, startWith, } from 'rxjs';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr'
import { RealizacijaService } from '../services/realizacija.service';
import { ApiService } from '../services/api.service';
import { PredmetDto } from '../dtos/PredmetDto';
import { PredavacDto } from '../dtos/PredavacDto';
import { autocompleteValidator } from '../validators/autocomplete-validator';

@Component({
  selector: 'app-realizacija-dialog',
  templateUrl: './realizacija-dialog.component.html',
  styleUrls: ['./realizacija-dialog.component.scss']
})
export class RealizacijaDialogComponent {
  [x: string]: any;
  title!: string;
  actionBtn: string = "Sačuvaj";
  studijskiProgramId!: string;
  predmeti: Set<PredmetDto> = new Set([]);
  predavaci: Set<PredavacDto> = new Set([]);
  predmetiOptions: Set<string> = new Set([]);
  filteredPredmetOptions!: Observable<string[]>;
  filteredProfesorOptions!: Observable<string[]>;
  filteredOstaliProfesoriOptions!: Observable<string[]>;
  filteredAsistentiOptions!: Observable<string[]>;
  predmetForm!: FormGroup;
  show = false;
  predavaciOptions!: Set<string>;
  glavniProfesorOptions!: string[];
  glavniProfesorValidOptions!: string[];
  options: string[][] = [[]];
  optionsAsistenti: string[][] = [[]];

  constructor(private formBuilder: FormBuilder,
    private api: RealizacijaService,
    private predmetApi: ApiService,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private dialogRef: MatDialogRef<RealizacijaDialogComponent>,
    private toastr: ToastrService) { }

  ngOnInit(): void {
    this.predmeti = new Set(this.data.predmeti);
    this.predmetiOptions = new Set(this.data.predmetiOptions);
    this.predavaciOptions = new Set(this.data.predavaciOptions);
    this.predavaci = new Set(this.data.predavaci);
    this.glavniProfesorValidOptions = Array.from(this.predavaciOptions);
    this.glavniProfesorValidOptions.push('');
    //this.glavniProfesorOptions = this.predavaciOptions;

    this.predmetForm = this.formBuilder.group({
      studijskiProgramId: new FormControl({ value: '', disabled: true }),
      predmetId: ['', Validators.required],
      godina: new FormControl({ value: '', disabled: true }),
      profesorId: ['', [autocompleteValidator(this.glavniProfesorValidOptions)]],
      ostaliProfesori: this.formBuilder.array([]),
      asistentZauzeca: this.formBuilder.array([])
    });

    if (this.data.studijskiProgramId) {
      this.prepareForAdd();
    } else {
      this.prepareForEdit();
    }

    this.filteredPredmetOptions = this.predmetForm.get('predmetId')!.valueChanges.pipe(
      startWith(''),
      map(value => this._filterPredmetiOptions(value || '')),
    );

    this.filteredProfesorOptions = this.predmetForm.get('profesorId')!.valueChanges.pipe(
      startWith(''),
      map(value => this._filterGlavniProfesorOptions(value || '')),
    );
  }

  populateOptions() {
    this.glavniProfesorOptions = this.data.predavaciOptions;
  }

  prepareForAdd() {
    this.title = "Dodavanje predmeta u realizaciju"
    console.log(this.data);
    this.setStudijskiProgram(this.data.studijskiProgramId);
  }

  setStudijskiProgram(studijskiProgramId: any) {
    this.studijskiProgramId = studijskiProgramId;
    this.api.get(studijskiProgramId)
      .subscribe({
        next: (res) => {
          console.log(res);
          this.predmetForm.controls['studijskiProgramId'].setValue(res.studijskiProgram);
        }
      });
  }

  prepareForEdit() {
    this.title = "Izmena predmeta u realizaciju"
    console.log(this.data);
    this.setStudijskiProgram(this.data.element.studijskiProgramId);
    this.predmetForm.controls['predmetId'].setValue('(' + this.data.element.predmetPlan + ') ' + this.data.element.predmetOznaka + ' ' + this.data.element.predmetNaziv);
    this.predmetForm.controls['predmetId'].disable();
    this.predmetForm.controls['godina'].setValue(this.data.element.predmetGodina);
    this.show = true;
    this.predmetForm.controls['godina'].disable();
    let profesori = Array.from(this.predavaciOptions);
    let profesor = profesori.filter(p => p.split("(")[0].trim() == this.data.element.profesor).map(value => value)[0];
    this.predmetForm.controls['profesorId'].setValue(profesor);
    if (this.data.element.ostaliProfesori) {
      for (let prof of this.data.element.ostaliProfesori) {
        let profesori = Array.from(this.predavaciOptions);
        let profesor = profesori.filter(p => p.split("(")[0].trim() == prof)
        this.ostaliProfesoriFieldsAsFormArray.push(this.formBuilder.control(profesor));
      }
    }
    if (this.data.element.asistentiZauzeca) {
      for (let asist of this.data.element.asistentiZauzeca) {
        let asistenti = Array.from(this.predavaciOptions);
        let asistent = asistenti.filter(p => p.split("(")[0].trim() == asist.asistent).map(value => value)[0];
        this.asistentZauzecaFieldsAsFormArray.push(this.asistentZauzece(asistent, asist.brojTermina));
      }
    }
    console.log(this.predmetForm.value);
  }

  get ostaliProfesoriFieldsAsFormArray(): any {
    return this.predmetForm.get('ostaliProfesori') as FormArray;
  }

  addControl(): void {
    this.options.push([]);
    this.ostaliProfesoriFieldsAsFormArray.push(this.formBuilder.control('', [Validators.required, autocompleteValidator(Array.from(this.predavaciOptions))]));
  }

  remove(i: number): void {
    this.ostaliProfesoriFieldsAsFormArray.removeAt(i);
    this.options.slice(i, i);
  }

  asistentZauzece(asistentId: string, brojTermina: number): any {
    return this.formBuilder.group({
      asistentId: this.formBuilder.control(asistentId,[Validators.required, autocompleteValidator(Array.from(this.predavaciOptions))]),
      brojTermina: this.formBuilder.control(brojTermina, [Validators.required, Validators.min(1)])
    });
  }

  newAsistentZauzece(): any {
    this.optionsAsistenti.push([]);
    return this.formBuilder.group({
      asistentId: this.formBuilder.control('', [Validators.required, autocompleteValidator(Array.from(this.predavaciOptions))]),
      brojTermina: this.formBuilder.control('', [Validators.required, Validators.min(1)])
    });
  }

  get asistentZauzecaFieldsAsFormArray(): any {
    return this.predmetForm.get('asistentZauzeca') as FormArray;
  }

  addAsistent() {
    this.asistentZauzecaFieldsAsFormArray.push(this.newAsistentZauzece())
  }

  removeAsistent(i: number): void {
    this.optionsAsistenti.slice(i, i);
    this.asistentZauzecaFieldsAsFormArray.removeAt(i);
  }

  private _filterPredmetiOptions(value: string): string[] {
    if (value == '')
      return Array.from(this.predmetiOptions);

    const filterValue = value.toLowerCase();
    let filterOptions = Array.from(this.predmetiOptions);
    return filterOptions.filter(option => option.toLowerCase().includes(filterValue));
  }

  private _filterGlavniProfesorOptions(value: string): string[] {
    if (value == '')
      return this.glavniProfesorOptions;
    const filterValue = value.toLowerCase();
    let all = this.glavniProfesorOptions;
    return all.filter(option => option.toLowerCase().includes(filterValue));
  }

  public _filterOstaliProfesoriOptions($event: any, index: number) {
    const value = $event?.target?.value;
    if (value == '' || !value) {
      this.options[index] = Array.from(this.predavaciOptions);
      return;
    }
    const filterValue = value.toLowerCase();
    let all = Array.from(this.predavaciOptions);
    let options = all.filter(option => option.toLowerCase().includes(filterValue));
    this.options[index] = options;
  }

  public _filterAsistentiOptions($event: any, index: number) {
    const value = $event?.target?.value;
    if (value == '' || !value) {
      this.optionsAsistenti[index] = Array.from(this.predavaciOptions);
      return;
    }
    const filterValue = value.toLowerCase();
    let all = Array.from(this.predavaciOptions);
    let options = all.filter(option => option.toLowerCase().includes(filterValue));
    this.optionsAsistenti[index] = options;
  }

  getGodina(predmet: string) {
    let predmeti = Array.from(this.predmeti);
    let predmetId = predmeti.filter(sp => sp.oznaka == predmet.split(' ')[1]).map(value => value.id)[0];
    this.predmetApi.get(predmetId)
      .subscribe({
        next: (res) => {
          this.show = true;
          this.predmetForm.controls['godina'].setValue(res.godina);
        }
      });
  }

  prepareData() {
    if (!this.predmetForm.valid) {
      return;
    }
    console.log(this.predmetForm.value);

    if(this.predmetForm.value.predmetId) {
      let predmeti = Array.from(this.predmeti);
      this.predmetForm.value.predmetId = predmeti.filter(sp => sp.oznaka == this.predmetForm.value.predmetId.split(' ')[1]).map(value => value.id)[0];
    }
    
    let profesori = Array.from(this.predavaci);
    this.predmetForm.value.profesorId = profesori.filter(p =>
      ((p.titula? p.titula : "") + " " + p.ime + " " + p.prezime + " (" + p.orgJedinica + ")").trim() == this.predmetForm.value.profesorId).map(value => value.id)[0];

    let izabraniProfesori = [];
    for (let prof of this.predmetForm.value.ostaliProfesori) {
      let ostaliProfesori = Array.from(this.predavaci);
      izabraniProfesori.push(ostaliProfesori.filter(p =>
        ((p.titula? p.titula : "") + " " + p.ime + " " + p.prezime + " (" + p.orgJedinica + ")").trim() == prof).map(value => value.id)[0]);
    }
    this.predmetForm.value.ostaliProfesori = izabraniProfesori;

    let asistentZauzeca = [];
    for (let zauzece of this.predmetForm.value.asistentZauzeca) {
      let asistenti = Array.from(this.predavaci);
      let asistentId = asistenti.filter(p =>
        ((p.titula? p.titula : "") + " " + p.ime + " " + p.prezime + " (" + p.orgJedinica + ")").trim() == zauzece.asistentId).map(value => value.id)[0];
      asistentZauzeca.push({ asistentId: asistentId, brojTermina: zauzece.brojTermina });
    }
    this.predmetForm.value.asistentZauzeca = asistentZauzeca;
    console.log(this.predmetForm.value);
  }

  add() {
    this.prepareData();

    this.api.addPredmet(this.studijskiProgramId, this.predmetForm.value)
      .subscribe({
        next: () => {
          this.toastr.success('Novi predmet je uspešno dodat u realizaciju!', 'Uspešno!');
          this.dialogRef.close('save');
        },
        error: () => {
          this.toastr.error('Došlo je do greške prilikom dodavanja predmeta u realizaciju!', 'Greška!');
        }
      });
  }

  update() {
    this.prepareData();

    this.api.updatePredmet(this.data.element.studijskiProgramId, this.data.element.predmetId, this.predmetForm.value)
    .subscribe({
      next: () => {
        this.toastr.success('Predmet u realizaciji je uspešno izmenjen!', 'Uspešno!');
        this.dialogRef.close('update');
      },
      error: () => {
        this.toastr.error('Došlo je do greške prilikom izmene predmeta u realizaciji!', 'Greška!');
      }
    });
  }
}
