import { Component, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { StudijskiProgramDto } from '../dtos/StudijskiProgramDto';
import { StudijskiProgramService } from '../services/studijski-program.service';
import { StudijskiProgramDialogComponent } from '../studijski-program-dialog/studijski-program-dialog.component';
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog.component';
import { ToastrService } from 'ngx-toastr';
import { RealizacijaService } from '../services/realizacija.service';

@Component({
  selector: 'app-studijski-programi',
  templateUrl: './studijski-programi.component.html',
  styleUrls: ['./studijski-programi.component.scss']
})
export class StudijskiProgramiComponent {
  displayedColumns: string[] = ['oznaka', 'naziv', 'stepenStudija', 'actions'];
  dataSource! : MatTableDataSource<StudijskiProgramDto>;

  @ViewChild(MatPaginator)
  paginator!: MatPaginator;

  ngOnInit() {
    this.getAll();
  }

  constructor(private api: StudijskiProgramService,
    private realizacijaApi: RealizacijaService, 
    public dialog: MatDialog, 
    private toastr: ToastrService) {}

  oznaka = '';
  naziv = '';
  stepenStudija = 'SVE';

  getAll() {
    this.api.getAll()
    .subscribe({
      next:(res) => {
        this.dataSource = new MatTableDataSource(res);
        this.dataSource.paginator = this.paginator;
      }
    })
  }

  openDialog(): void {
    this.dialog.open(StudijskiProgramDialogComponent, {
      width: '30%'
    }).afterClosed().subscribe((val) => {
      if(val == 'save') {
        if(this.oznaka || this.naziv || this.stepenStudija != 'SVE') {
          this.applyFilter();
        } else {
          this.getAll();
        }
      }
    });
  }

  edit(element : any) {
    this.dialog.open(StudijskiProgramDialogComponent, {
      width: '30%',
      data: element
    }).afterClosed().subscribe((val) => {
      if(val == 'update') {
        if(this.oznaka || this.naziv || this.stepenStudija != 'SVE') {
          this.applyFilter();
        } else {
          this.getAll();
        }
      }
    });
  }

  openConfirmationDialog(element: any) {
    this.dialog.open(ConfirmationDialogComponent, {
      width: '40%',
      data: element
    }).afterClosed().subscribe((val) => {
      if(val) {
        this.delete(element.id);
      }
    });
  }
  
  delete(id : string) {
    this.realizacijaApi.deleteStudijskiProgram(id)
    .subscribe({
      next: () => {
        this.toastr.success('Studijski program je uspešno obrisan!', 'Uspešno!');
        if(this.oznaka || this.naziv || this.stepenStudija != 'SVE') {
          this.applyFilter();
        } else {
          this.getAll();
        }
      },
      error: () => {
        this.toastr.error('Greška prilikom brisanja studijskog programa!','Greška!');
      }
    })
  }

  applyFilter() {
    let studijskiProgramSearchDto = {
      oznaka: this.oznaka,
      naziv: this.naziv,
      stepenStudija: this.stepenStudija
    }
    
    this.api.search(studijskiProgramSearchDto).subscribe({
      next: (res) => {
        this.dataSource = new MatTableDataSource(res);
        this.dataSource.paginator = this.paginator;
      }
    });
  }
}
