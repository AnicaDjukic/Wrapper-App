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
    this.api.getAllStudijskiProgram()
      .subscribe({
        next: (res) => {
          this.studijskiProgrami = res
          res.forEach((element: StudijskiProgramDto) => {
            this.options.push(element.oznaka + ' ' + element.naziv + ' (' + element.stepenStudija + ")")
          });
        }
      })
    this.show = false;
    this.filteredOptions = this.myControl.valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value || '')),
    );
    await this.getPredavaciOptions();
  }

  openDialog() {
    let studijskiProgramId = this.studijskiProgrami.filter(sp => sp.oznaka == this.selected.split(' ')[0]).map(value => value.id)[0];
    this.getPredmetOptions(studijskiProgramId);
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
      }
    });
  }

  getPredmetOptions(studijskiProgramId: any) {
    this.api.getByStudijskiProgram(studijskiProgramId)
      .subscribe({
        next: (res) => {
          console.log(res);
          for (let predmet of res) {
            this.predmetiOptions.push("(" + predmet.plan + ") " + predmet.oznaka + " " + predmet.naziv);
          }
          this.predmeti = res;
        }
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
      }
    });
  }

  async getPredavaciOptions() {
    this.predavaciOptions = [];
    const predavaci = await lastValueFrom(this.getPredavaci(0, 200));
    for (let predavac of predavaci) {
      let opt = predavac.titula + " " + predavac.ime + " " + predavac.prezime + " (" + predavac.orgJedinica + ")";
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

  get(studijskiProgram: string) {
    console.log(this.myControl);
    this.selected = studijskiProgram;
    this.studijskiProgramId = this.studijskiProgrami.filter(sp => sp.oznaka == studijskiProgram.split(' ')[0]).map(value => value.id)[0];
    this.realizacijaService.get(this.studijskiProgramId)
      .subscribe({
        next: (res) => {
          console.log(res.predmetPredavaci);
          this.dataSource = res.predmetPredavaci;
        }
      });
  }
}
