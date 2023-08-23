import { animate, state, style, transition, trigger } from '@angular/animations';
import { Component } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { ToastrService } from 'ngx-toastr';
import { Observable, lastValueFrom, of } from 'rxjs';
import { map, startWith, switchMap } from 'rxjs/operators';
import { PredmetPredavacDto } from '../dtos/PredmetPredavacDto';
import { StudijskiProgramDto } from '../dtos/StudijskiProgramDto';
import { RealizacijaDialogComponent } from '../realizacija-dialog/realizacija-dialog.component';
import { ApiService } from '../services/api.service';
import { RealizacijaService } from '../services/realizacija.service';
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog.component';
import { PredavacService } from '../services/predavac.service';
import { PredavacDto } from '../dtos/PredavacDto';
import { PredmetDto } from '../dtos/PredmetDto';
import { StudijskiProgramService } from '../services/studijski-program.service';

@Component({
  selector: 'app-realizacija',
  templateUrl: './realizacija.component.html',
  styleUrls: ['./realizacija.component.scss'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({ height: '0px', minHeight: '0' })),
      state('expanded', style({ height: '*' })),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ]
})
export class RealizacijaComponent {

  constructor(private api: ApiService,
    private realizacijaService: RealizacijaService,
    private studijskiProgramService: StudijskiProgramService,
    private predavacApi: PredavacService,
    public dialog: MatDialog,
    private toastr: ToastrService) { }

  studijskiProgrami: StudijskiProgramDto[] = [];
  options: string[] = [];
  filteredOptions!: Observable<string[]>;
  myControl = new FormControl('');
  show!: boolean
  selected!: string
  studijskiProgramId!: string
  predmeti: PredmetDto[] = [];
  predmetiOptions: string[] = [];
  predavaciOptions: string[] = [];
  predavaci: PredavacDto[] = [];

  dataSource!: MatTableDataSource<PredmetPredavacDto>;
  columnsToDisplay = ['planPredmeta', 'predmetOznaka', 'predmetNaziv', 'predmetGodina', 'profesor', 'ostaliProfesori', 'expand', 'actions'];
  columnsToDisplayWithExpand = [...this.columnsToDisplay];
  expandedElement!: PredmetPredavacDto;

  async ngOnInit() {
    this.getStudijskiProgrami();
    await this.getPredavaciOptions();
  }

  getStudijskiProgrami() {
    this.options = [];
    this.studijskiProgramService.getAll()
      .subscribe({
        next: (res) => {
          this.studijskiProgrami = res;
          res.forEach((element: StudijskiProgramDto) => {
            this.options.push(element.oznaka + ' ' + element.naziv + ' (' + element.stepenStudija + ')');
          });
        }
      })
    this.show = false;
    this.filteredOptions = this.myControl.valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value || '')),
    );
  }

  getColorClass(value: string): string {
    let studProgram = this.studijskiProgrami.filter(sp => (sp.oznaka + ' ' + sp.naziv + ' (' + sp.stepenStudija + ')') == value).map(value => value)[0];

    if (studProgram.block) {
      return 'block';
    }
    return '';
  }

  async openDialog() {
    let studijskiProgramId = this.studijskiProgrami.filter(sp => (sp.oznaka + ' ' + sp.naziv + ' (' + sp.stepenStudija + ')') == this.selected).map(value => value.id)[0];
    this.predmetiOptions = [];
    await this.getPredmetOptions(studijskiProgramId);
    if (this.predmetiOptions.length == 0) {
      this.toastr.warning('Svi predmeti izabranog studijskog programa su već dodati u realizaciju!');
      return;
    }
    this.dialog.open(RealizacijaDialogComponent, {
      width: '60%',
      data: {
        studijskiProgramId: studijskiProgramId,
        predmetiOptions: this.predmetiOptions,
        predmeti: this.predmeti,
        predavaciOptions: this.predavaciOptions,
        predavaci: this.predavaci
      }

    }).afterClosed().subscribe((val) => {
      if (val == 'save') {
        console.log('The dialog was closed');
        this.get(this.selected);
        this.getStudijskiProgrami();
      }
    });
  }

  async getPredmetOptions(studijskiProgramId: any) {
    return new Promise<void>((resolve, reject) => {
      this.api.getByStudijskiProgram(studijskiProgramId)
        .subscribe({
          next: (res) => {
            for (let predmet of res) {
              console.log(studijskiProgramId);
              this.predmetiOptions.push("(" + predmet.plan + ") " + predmet.oznaka + " " + predmet.naziv);
            }
            this.predmeti = res;
            resolve(); // Resolve the promise once the asynchronous operation is complete
          },
          error: (err) => {
            reject(err); // Reject the promise if an error occurs
          }
        });
    });
  }

  edit(element: any) {
    element.studijskiProgramId = this.studijskiProgramId;
    this.getPredmetOptions(this.studijskiProgramId);
    this.dialog.open(RealizacijaDialogComponent, {
      width: '60%',
      data: {
        element: element,
        predmetiOptions: this.predmetiOptions,
        predmeti: this.predmeti,
        predavaciOptions: this.predavaciOptions,
        predavaci: this.predavaci
      }
    }).afterClosed().subscribe((val) => {
      if (val == 'update') {
        console.log('The dialog was closed');
        this.get(this.selected);
        this.getStudijskiProgrami();
      }
    });
  }

  async getPredavaciOptions() {
    this.predavaciOptions = [];
    const predavaci = await lastValueFrom(this.getPredavaci(0, 200));
    for (let predavac of predavaci) {
      let opt = (predavac.titula ? predavac.titula : "") + " " + predavac.ime + " " + predavac.prezime + " (" + predavac.orgJedinica + ")";
      this.predavaciOptions.push(opt.trim());
    }
    this.predavaci = predavaci;
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

  openConfirmationDialog(element: any) {
    this.dialog.open(ConfirmationDialogComponent, {
      width: '40%',
      data: element
    }).afterClosed().subscribe((val) => {
      console.log(val);
      if (val) {
        this.delete(element.predmetId);
      }
    });
  }

  delete(id: string) {
    this.realizacijaService.delete(this.studijskiProgramId, id)
      .subscribe({
        next: () => {
          this.toastr.success("Predmet je uspešno izbrisan iz realizacije!", "Uspešno!");
          this.get(this.selected);
          this.getStudijskiProgrami();
          this.getPredmetOptions(this.selected);
        },
        error: () => {
          this.toastr.error("Došlo je do greške prilikom brisanja predmeta iz realizacije!", "Greška!");
        }
      })
  }

  private _filter(value: string): string[] {
    if (value == '')
      return this.options;
    const filterValue = value.toLowerCase();

    return this.options.filter(option => option.toLowerCase().includes(filterValue));
  }

  clearField() {
    this.myControl.setValue('');
  }

  get(studijskiProgram: string) {
    console.log(this.myControl);
    this.selected = studijskiProgram;
    this.studijskiProgramId = this.studijskiProgrami.filter(sp => sp.oznaka + ' ' + sp.naziv + ' (' + sp.stepenStudija + ")" == this.selected).map(value => value.id)[0];
    this.realizacijaService.get(this.studijskiProgramId)
      .subscribe({
        next: (res) => {
          console.log(res.predmetPredavaci);
          let response: PredmetPredavacDto[] = [];
          for (let predmetPredavac of res.predmetPredavaci) {
            this.fillGlavniProfesorInfo(predmetPredavac);
            this.fillOstaliProfesoriInfo(predmetPredavac);
            this.fillAsistentiInfo(predmetPredavac);
            response.push(predmetPredavac);
          }
          this.dataSource = new MatTableDataSource(response);
        }
      });
  }

  fillGlavniProfesorInfo(predmetPredavac: any) {
    if (predmetPredavac.profesor) {
      let profesor = (predmetPredavac.profesor.titula ? predmetPredavac.profesor.titula : "") + " " + predmetPredavac.profesor.ime + " " + predmetPredavac.profesor.prezime;
      profesor.trim();
      predmetPredavac.profesor = profesor;
    }
  }

  fillOstaliProfesoriInfo(predmetPredavac: any) {
    let ostaliProfesori = [];
    for (let prof of predmetPredavac.ostaliProfesori) {
      let profesor = (prof.titula ? prof.titula : "") + " " + prof.ime + " " + prof.prezime;
      profesor.trim();
      ostaliProfesori.push(profesor);
    }
    predmetPredavac.ostaliProfesori = ostaliProfesori;
  }

  fillAsistentiInfo(predmetPredavac: any) {
    let asistentZauzeca = [];
    for (let zazuece of predmetPredavac.asistentZauzeca) {
      if (zazuece.asistent) {
        let asistent = (zazuece.asistent.titula ? zazuece.asistent.titula : "") + " " + zazuece.asistent.ime + " " + zazuece.asistent.prezime;
        asistent.trim();
        zazuece.asistent = asistent;
        asistentZauzeca.push(zazuece);
      }
    }
    predmetPredavac.asistentZauzeca = asistentZauzeca;
  }

}

