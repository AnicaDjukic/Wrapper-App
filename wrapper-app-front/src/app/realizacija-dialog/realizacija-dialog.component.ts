import { Component, Inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormControl, FormArray } from '@angular/forms';
import { concatMap, map, Observable, of, startWith, switchMap, tap } from 'rxjs';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr'
import { RealizacijaService } from '../services/realizacija.service';
import { ApiService } from '../services/api.service';
import { PredmetDto } from '../dtos/PredmetDto';
import { PredavacService } from '../services/predavac.service';
import { PredavacDto } from '../dtos/PredavacDto';

@Component({
  selector: 'app-realizacija-dialog',
  templateUrl: './realizacija-dialog.component.html',
  styleUrls: ['./realizacija-dialog.component.scss']
})
export class RealizacijaDialogComponent {
  title!: string;
  actionBtn: string = "Sačuvaj";
  studijskiProgramId!: string;
  predmeti: PredmetDto[] = [];
  predavaci: PredavacDto[] = [];
  predmetiOptions: string[] = [];
  predavaciOptions: string[] = [];
  filteredPredmetOptions!: Observable<string[]>;
  filteredProfesorOptions!: Observable<string[]>;
  filteredOstaliProfesoriOptions!: Observable<string[]>;
  filteredAsistentiOptions!: Observable<string[]>;
  predmetForm!: FormGroup;
  show = false;

  constructor(private formBuilder: FormBuilder,
    private api: RealizacijaService,
    private predmetApi: ApiService,
    private predavacApi: PredavacService,
    @Inject(MAT_DIALOG_DATA) public editData: any,
    private dialogRef: MatDialogRef<RealizacijaDialogComponent>,
    private toastr: ToastrService) { }

  ngOnInit(): void {
    this.predmetForm = this.formBuilder.group({
      studijskiProgramId: new FormControl({ value: '', disabled: true }),
      predmetId: ['', Validators.required],
      godina: new FormControl({ value: '', disabled: true }),
      profesorId: [''],
      ostaliProfesori: this.formBuilder.array([]),
      asistentZauzeca: this.formBuilder.array([])
    })

    this.getPredavaciOptions();

    if (this.editData && typeof this.editData === 'string') {
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

    this.filteredOstaliProfesoriOptions = this.predmetForm.get('ostaliProfesori')!.valueChanges.pipe(
      startWith(''),
      map(value => this._filterOstaliProfesoriOptions(value || '', this.ostaliProfesoriFieldsAsFormArray.length - 1)),
    );

    this.filteredAsistentiOptions = this.predmetForm.get('asistentZauzeca')!.valueChanges.pipe(
      startWith(''),
      map(value => this._filterAsistentiOptions(value || '', this.asistentZauzecaFieldsAsFormArray.length - 1)),
    );
  }

  getPredavaciOptions() {
    this.getPredavaci(0, 100)
      .subscribe(res => {
        for (let predavac of res) {
          let opt = predavac.titula + " " + predavac.ime + " " + predavac.prezime + " (" + predavac.orgJedinica + ")";
          this.predavaciOptions.push(opt.trim());
        }
        this.predavaci = res;
      });
  }

  getPredavaci(page: number, size: number, data: any[] = []): Observable<any[]> {
    return this.predavacApi.getAll(page, size).pipe(
      switchMap((res: any) => {
        data.push(...res.content);
        if (res.pageable.pageNumber + 1 < res.totalPages) {
          return this.getPredavaci(res.pageable.pageNumber + 1, size, data);
        } else {
          return of(data);
        }
      })
    );
  }

  prepareForAdd() {
    this.title = "Dodavanje predmeta u realizaciju"
    console.log(this.editData);
    this.setStudijskiProgram(this.editData);
    this.getPredmetOptions(this.editData)
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

  getPredmetOptions(studijskiProgramId: any) {
    this.predmetApi.getByStudijskiProgram(studijskiProgramId)
      .subscribe({
        next: (res) => {
          console.log(res);
          for (let predmet of res) {
            this.predmetiOptions.push("(20) " + predmet.oznaka + " " + predmet.naziv);
          }
          this.predmeti = res;
        }
      });
  }

  prepareForEdit() {
    this.title = "Izmena predmeta u realizaciju"
    console.log(this.editData);
    this.setStudijskiProgram(this.editData.studijskiProgramId);
    this.predmetForm.controls['predmetId'].setValue("(20) " + this.editData.predmetOznaka + " " + this.editData.predmetNaziv);
    this.predmetForm.controls['predmetId'].disable();
    this.predmetForm.controls['godina'].setValue(this.editData.predmetGodina);
    this.show = true;
    this.predmetForm.controls['godina'].disable();
    let profesori = this.predavaciOptions; // ovdee
    let profesor = profesori.filter(p => p.split("(")[0].trim() == this.editData.profesor).map(value => value)[0];
    this.predmetForm.controls['profesorId'].setValue(profesor);
    if (this.editData.ostaliProfesori) {
      for (let prof of this.editData.ostaliProfesori) {
        let profesori = this.predavaciOptions;
        let profesor = profesori.filter(p => p.split("(")[0].trim() == prof)
        this.ostaliProfesoriFieldsAsFormArray.push(this.formBuilder.control(profesor));
      }
    }
    if (this.editData.asistentiZauzeca) {
      for (let asist of this.editData.asistentiZauzeca) {
        this.asistentZauzecaFieldsAsFormArray.push(this.asistentZauzece(asist.asistent, asist.brojTermina));
      }
    }
    console.log(this.predmetForm.value);
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

  asistentZauzece(asistentId: string, brojTermina: number): any {
    return this.formBuilder.group({
      asistentId: this.formBuilder.control(asistentId),
      brojTermina: this.formBuilder.control(brojTermina)
    });
  }

  newAsistentZauzece(): any {
    return this.formBuilder.group({
      asistentId: this.formBuilder.control(''),
      brojTermina: this.formBuilder.control('')
    });
  }

  get asistentZauzecaFieldsAsFormArray(): any {
    return this.predmetForm.get('asistentZauzeca') as FormArray;
  }

  addAsistent() {
    this.asistentZauzecaFieldsAsFormArray.push(this.newAsistentZauzece())
  }

  removeAsistent(i: number): void {
    this.asistentZauzecaFieldsAsFormArray.removeAt(i);
  }

  private _filterPredmetiOptions(value: string): string[] {
    if (value == '')
      return this.predmetiOptions;

    const filterValue = value.toLowerCase();

    return this.predmetiOptions.filter(option => option.toLowerCase().includes(filterValue));
  }

  private _filterGlavniProfesorOptions(value: string): string[] {
    if (value == '')
      return this.predavaciOptions;
    const filterValue = value.toLowerCase();
    let all = this.predavaciOptions;
    return all.filter(option => option.toLowerCase().includes(filterValue));
  }

  private _filterOstaliProfesoriOptions(value: string, index: number): string[] {
    if (value == '')
      return this.predavaciOptions;
    const filterValue = value[index].toLowerCase();
    let all = this.predavaciOptions;
    return all.filter(option => option.toLowerCase().includes(filterValue));
  }

  private _filterAsistentiOptions(value: any, index: number): string[] {
    if (value == '' || value[index].asistentId == '')
      return this.predavaciOptions;
    const filterValue = value[index].asistentId.toLowerCase();
    let all = this.predavaciOptions;
    return all.filter(option => option.toLowerCase().includes(filterValue));
  }

  getGodina(predmet: string) {
    let predmetId = this.predmeti.filter(sp => sp.oznaka == predmet.split(' ')[0]).map(value => value.id)[0];
    this.predmetApi.get(predmetId)
      .subscribe({
        next: (res) => {
          console.log(res);
          this.show = true;
          this.predmetForm.controls['godina'].setValue(res.godina);
        }
      });
  }

  add() {
    console.log(this.predmetForm.value);

    this.predmetForm.value.predmetId = this.predmeti.filter(sp => sp.oznaka == this.predmetForm.value.predmetId.split(' ')[1]).map(value => value.id)[0];

    let profesori = this.predavaci;
    this.predmetForm.value.profesorId = profesori.filter(p =>
      (p.titula + " " + p.ime + " " + p.prezime + " (" + p.orgJedinica + ")").trim() == this.predmetForm.value.profesorId).map(value => value.id)[0];

    let izabraniProfesori = [];
    for (let prof of this.predmetForm.value.ostaliProfesori) {
      let ostaliProfesori = this.predavaci;
      izabraniProfesori.push(ostaliProfesori.filter(p =>
        (p.titula + " " + p.ime + " " + p.prezime + " (" + p.orgJedinica + ")").trim() == prof).map(value => value.id)[0]);
    }
    this.predmetForm.value.ostaliProfesori = izabraniProfesori;

    let asistentZauzeca = [];
    for (let zauzece of this.predmetForm.value.asistentZauzeca) {
      let asistenti = this.predavaci;
      let asistentId = asistenti.filter(p =>
        (p.titula + " " + p.ime + " " + p.prezime + " (" + p.orgJedinica + ")").trim() == zauzece.asistentId).map(value => value.id)[0];
      asistentZauzeca.push({ asistentId: asistentId, brojTermina: zauzece.brojTermina });
    }
    this.predmetForm.value.asistentZauzeca = asistentZauzeca;
    console.log(this.predmetForm.value);

    this.api.post(this.studijskiProgramId, this.predmetForm.value)
      .subscribe({
        next: () => {
          this.toastr.success('Novi predmet je uspešno dodat u realizaciju!', 'Uspešno!');
          this.dialogRef.close('save');
        },
        error: () => {
          this.toastr.error('Došlo je do greške prilikom dodavanja predmeta u realizaciju!', 'Greška!');
        }
      })
  }

  update() {
    if (!this.predmetForm.valid) {
      return
    }
    let studProg: string = this.predmetForm.value.studijskiProgram
    this.predmetForm.value.studijskiProgram = this.predmeti.filter(sp => sp.oznaka == studProg.split(' ')[0]).map(value => value.id)[0];
  }
}
