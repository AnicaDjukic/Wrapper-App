import { animate, state, style, transition, trigger } from '@angular/animations';
import { Component } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { ToastrService } from 'ngx-toastr';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { PredmetPredavacDto } from '../dtos/PredmetPredavacDto';
import { StudijskiProgramDto } from '../dtos/StudijskiProgramDto';
import { RealizacijaDialogComponent } from '../realizacija-dialog/realizacija-dialog.component';
import { ApiService } from '../services/api.service';
import { RealizacijaService } from '../services/realizacija.service';

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


  constructor(private api: ApiService, private realizacijaService: RealizacijaService, public dialog: MatDialog, private toastr: ToastrService) { }

  studijskiProgrami: StudijskiProgramDto[] = [];
  options: string[] = [];
  filteredOptions!: Observable<string[]>;
  myControl = new FormControl('');
  show!: boolean
  selected!: string
  studijskiProgramId!: string

  dataSource!: MatTableDataSource<PredmetPredavacDto>;
  columnsToDisplay = ['planPredmeta', 'predmetOznaka', 'predmetNaziv', 'predmetGodina', 'profesor', 'ostaliProfesori', 'expand', 'actions'];
  columnsToDisplayWithExpand = [...this.columnsToDisplay];
  expandedElement!: PredmetPredavacDto;

  ngOnInit() {
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
  }

  openDialog(): void {
    let studijskiProgramId = this.studijskiProgrami.filter(sp => sp.oznaka == this.selected.split(' ')[0]).map(value => value.id)[0];
    this.dialog.open(RealizacijaDialogComponent, {
      width: '60%',
      data: studijskiProgramId
    }).afterClosed().subscribe((val) => {
      if (val == 'save') {
        console.log('The dialog was closed');
        this.get(this.selected);
      }
    });
  }

  edit(element: any) {
    element.studijskiProgramId = this.studijskiProgramId;
    this.dialog.open(RealizacijaDialogComponent, {
      width: '60%',
      data: element
    }).afterClosed().subscribe((val) => {
      if (val == 'update') {
        console.log('The dialog was closed');
        this.get(this.selected);
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
