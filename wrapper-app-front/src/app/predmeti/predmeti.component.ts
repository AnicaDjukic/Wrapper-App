import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { PredmetDto } from '../dtos/PredmetDto';
import { ApiService } from '../services/api.service';
import { MatDialog } from '@angular/material/dialog';
import { PredmetDialogComponent } from '../predmet-dialog/predmet-dialog.component';
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog.component';
import { ToastrService } from 'ngx-toastr';
import { RealizacijaService } from '../services/realizacija.service';

@Component({
  selector: 'app-predmeti',
  templateUrl: './predmeti.component.html',
  styleUrls: ['./predmeti.component.scss']
})
export class PredmetiComponent implements OnInit {

  displayedColumns: string[] = ['oznaka', 'plan', 'naziv', 'godina', 'studijskiProgram',
    'brojCasovaPred', 'brojCasovaAud', 'brojCasovaLab', 'brojCasovaRac', 'actions'];

  constructor(private api: ApiService,
    private rrealizacijaApi: RealizacijaService, 
    public dialog: MatDialog, 
    private toastr: ToastrService) { }

  dataSource = new MatTableDataSource<PredmetDto>();
  @ViewChild(MatPaginator, { static: true }) paginator!: MatPaginator;

  oznaka = '';
  naziv = '';
  studijskiProgram = '';

  pageSize = 10;
  totalElements = 0;
  pageIndex = 0;

  ngOnInit() {
    this.dataSource.paginator = this.paginator;
    this.getAll(0, this.pageSize);
  }

  getAll(page: number, size: number) {
    this.api.getAll(page, size).subscribe(res => {
      this.dataSource = new MatTableDataSource(res.content);
      this.totalElements = res.totalElements;
      this.pageIndex = res.pageable.pageNumber;
    });
  }

  openDialog(): void {
    this.dialog.open(PredmetDialogComponent, {
      width: '40%'
    }).afterClosed().subscribe((val) => {
      if (val == 'save') {
        console.log('The dialog was closed');
        this.getAll(this.pageIndex, this.pageSize);
      }
    });
  }

  edit(element: any) {
    this.dialog.open(PredmetDialogComponent, {
      width: '40%',
      data: element
    }).afterClosed().subscribe((val) => {
      if (val == 'update') {
        console.log('The dialog was closed');
        this.getAll(this.pageIndex, this.pageSize);
      }
    });
  }

  openConfirmationDialog(element: any) {
    this.dialog.open(ConfirmationDialogComponent, {
      width: '40%',
      data: element
    }).afterClosed().subscribe((val) => {
      console.log(val);
      if(val) {
        this.delete(element.id);
      }
    });
  }

  delete(id: string) {
    this.rrealizacijaApi.deletePredmet(id)
      .subscribe({
        next: () => {
          this.toastr.success('Predmet je uspešno obrisan!', 'Uspešno!');
          this.getAll(this.pageIndex, this.pageSize)
        },
        error: () => {
          this.toastr.error('Greška prilikom brisanja predmeta!', 'Greška!');
        }
      })
  }

  applyFilter(page: number, size: number) {
    let predmetSearchDto = {
      oznaka: this.oznaka,
      naziv: this.naziv,
      studijskiProgram: this.studijskiProgram
    }
    this.api.search(page, size, predmetSearchDto).subscribe({
      next: (res) => {
        this.dataSource = new MatTableDataSource(res.content);
        this.totalElements = res.totalElements;
        this.pageIndex = res.pageable.pageNumber;
      }
    });
  }

  scrollToTop(): void {
    window.scrollTo(0, 0);
  }
}


