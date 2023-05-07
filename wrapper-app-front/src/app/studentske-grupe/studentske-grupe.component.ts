import { Component, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { StudentskaGrupaDto } from '../dtos/StudentskaGrupaDto';
import { StudentskaGrupaService } from '../services/studentska-grupa.service';
import { StudentskaGrupaDialogComponent } from '../studentska-grupa-dialog/studentska-grupa-dialog.component';
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog.component';
import { ToastrService } from 'ngx-toastr';
import { StudijskiProgramService } from '../services/studijski-program.service';
import { StudijskiProgramDto } from '../dtos/StudijskiProgramDto';

@Component({
  selector: 'app-studentske-grupe',
  templateUrl: './studentske-grupe.component.html',
  styleUrls: ['./studentske-grupe.component.scss']
})
export class StudentskeGrupeComponent {
  displayedColumns: string[] = ['oznaka', 'godina', 'brojStudenata', 'studijskiProgram', 'actions'];

  dataSource = new MatTableDataSource<StudentskaGrupaDto>();
  studijskiProgrami: StudijskiProgramDto[] = [];
  options: string[] = [];
  @ViewChild(MatPaginator)
  paginator!: MatPaginator;

  oznaka = '';
  godina = 'SVE';
  brojStudenata = 0;
  brojStudenataStr = '';
  studijskiProgram = '';

  pageSize = 10;
  totalElements = 0;
  pageIndex = 0;

  ngOnInit() {
    this.dataSource.paginator = this.paginator;
    this.getAll(0, this.pageSize);
    this.getStudijskiProgramiOptions();
  }

  constructor(private api: StudentskaGrupaService,
    private studijskiProgramApi: StudijskiProgramService, 
    public dialog: MatDialog, 
    private toastr: ToastrService) { }

  getAll(page: number, size: number) {
    this.api.getAll(page, size)
      .subscribe({
        next: (res) => {
          this.dataSource = new MatTableDataSource(res.content);
          this.totalElements = res.totalElements;
          this.pageIndex = res.pageable.pageNumber;
        }
      })
  }

  getStudijskiProgramiOptions() {
    this.studijskiProgramApi.getAll()
      .subscribe({
        next: (res) => {
          this.studijskiProgrami = res;
          res.forEach((element: StudijskiProgramDto) => {
            this.options.push(element.oznaka + ' ' + element.naziv);
          });
        }
      });
  }

  openDialog(): void {
    this.dialog.open(StudentskaGrupaDialogComponent, {
      width: '30%',
      data: {
        options: this.options,
        studijskiProgrami: this.studijskiProgrami
      }
    }).afterClosed().subscribe((val) => {
      if (val == 'save') {
        if(this.oznaka || this.godina != 'SVE' || this.brojStudenata || this.studijskiProgram) {
          this.applyFilter(this.pageIndex, this.pageSize);
        } else {
          this.getAll(this.pageIndex, this.pageSize);
        }
      }
    });
  }

  edit(element: any) {
    this.dialog.open(StudentskaGrupaDialogComponent, {
      width: '30%',
      data: {
        editData: element,
        options: this.options,
        studijskiProgrami: this.studijskiProgrami
      }
    }).afterClosed().subscribe((val) => {
      if (val == 'update') {
        if(this.oznaka || this.godina != 'SVE' || this.brojStudenata || this.studijskiProgram) {
          this.applyFilter(this.pageIndex, this.pageSize);
        } else {
          this.getAll(this.pageIndex, this.pageSize);
        }
      }
    });
  }

  openConfirmationDialog(element: any) {
    this.dialog.open(ConfirmationDialogComponent, {
      width: '40%',
      data: element
    }).afterClosed().subscribe((val) => {
      console.log(val);
      if (val) {
        this.delete(element.id);
      }
    });
  }

  delete(id: string) {
    this.api.delete(id)
      .subscribe({
        next: () => {
          this.toastr.success('Studentska grupa je uspešno obrisana!', 'Uspešno!');
          if(this.oznaka || this.godina != 'SVE' || this.brojStudenata || this.studijskiProgram) {
            this.applyFilter(this.pageIndex, this.pageSize);
          } else {
            this.getAll(this.pageIndex, this.pageSize);
          }
        },
        error: () => {
          this.toastr.error('Greška prilikom brisanja studentske grupe!', 'Greška!');
        }
      })
  }

  onInputChange(event: Event) {
    const inputValue = (event.target as HTMLInputElement)?.value || '';
    if (inputValue != '') {
      this.brojStudenata = Number(inputValue);
    }
    this.brojStudenataStr = inputValue;
  }

  applyFilter(page: number, size: number) {
    let studentskaGrupaSearchDto = {
      oznaka: this.oznaka,
      godina: this.godina != 'SVE' ? this.godina : '',
      brojStudenata: this.brojStudenataStr,
      studijskiProgram: this.studijskiProgram
    }

    this.api.search(page, size, studentskaGrupaSearchDto).subscribe({
      next: (res) => {
        this.dataSource = new MatTableDataSource(res.content);
        this.totalElements = res.totalElements;
        this.pageIndex = res.pageable.pageNumber;
      }
    });
  }
}
