import { animate, state, style, transition, trigger } from '@angular/animations';
import { Component } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatTableDataSource } from '@angular/material/table';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators'
import { AsistentZauzeceDto } from '../dtos/AsistentZauzeceDto';
import { PredmetPredavacDto } from '../dtos/PredmetPredavacDto';
import { StudijskiProgramDto } from '../dtos/StudijskiProgramDto';
import { ApiService } from '../services/api.service';
import { RealizacijaService } from '../services/realizacija.service';

@Component({
  selector: 'app-realizacija',
  templateUrl: './realizacija.component.html',
  styleUrls: ['./realizacija.component.scss'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class RealizacijaComponent {


  constructor(private api: ApiService, private realizacijaService: RealizacijaService){}

  studijskiProgrami: StudijskiProgramDto[] = [];
  options: string[] = [];
  filteredOptions!: Observable<string[]>;
  myControl = new FormControl('');
  show!:boolean

  dataSource!: MatTableDataSource<PredmetPredavacDto>;
  columnsToDisplay = ['predmetOznaka', 'predmetNaziv', 'predmetGodina', 'profesor', 'ostaliProfesori', 'expand', 'actions'];
  columnsToDisplayWithExpand = [...this.columnsToDisplay];
  expandedElement!: PredmetPredavacDto;

  ngOnInit() {
    this.api.getAllStudijskiProgram()
    .subscribe({
      next: (res) => {
        this.studijskiProgrami = res
        res.forEach((element: StudijskiProgramDto) => {
          this.options.push(element.oznaka + ' ' + element.naziv)
        });
      }
    })
    this.show = false;
    this.filteredOptions = this.myControl.valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value || '')),
    );
  }

  private _filter(value: string): string[] {
    if (value == '')
      return this.options;
    const filterValue = value.toLowerCase();

    return this.options.filter(option => option.toLowerCase().includes(filterValue));
  }

  get(studijskiProgram : string) {
    console.log(this.myControl);
    this.show = true;
    let studijskiProgramId = this.studijskiProgrami.filter(sp => sp.oznaka == studijskiProgram.split(' ')[0]).map(value => value.id)[0];
    this.realizacijaService.get(studijskiProgramId)
    .subscribe({
      next:(res) => {
        console.log(res.predmetPredavaci);
        this.dataSource = res.predmetPredavaci;
      }
    });
  }
}
