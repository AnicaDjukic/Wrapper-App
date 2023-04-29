import { Component, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { StudentskaGrupaDto } from '../dtos/StudentskaGrupaDto';
import { StudentskaGrupaService } from '../services/studentska-grupa.service';
import { StudentskaGrupaDialogComponent } from '../studentska-grupa-dialog/studentska-grupa-dialog.component';

@Component({
  selector: 'app-studentske-grupe',
  templateUrl: './studentske-grupe.component.html',
  styleUrls: ['./studentske-grupe.component.scss']
})
export class StudentskeGrupeComponent {
  displayedColumns: string[] = ['oznaka', 'godina', 'brojStudenata', 'studijskiProgram', 'actions'];
  
  dataSource = new MatTableDataSource<StudentskaGrupaDto>();
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
  }

  constructor(private api: StudentskaGrupaService, public dialog: MatDialog) {}

  getAll(page: number, size: number) {
    this.api.getAll(page, size)
    .subscribe({
      next:(res) => {
        this.dataSource = new MatTableDataSource(res.content);
      this.totalElements = res.totalElements;
      this.pageIndex = res.pageable.pageNumber;
      }
    })
  }

  openDialog(): void {
    this.dialog.open(StudentskaGrupaDialogComponent, {
      width: '30%'
    }).afterClosed().subscribe((val) => {
      if(val == 'save') {
        console.log('The dialog was closed');
        this.getAll(0, this.pageSize);
      }
    });
  }

  edit(element : any) {
    this.dialog.open(StudentskaGrupaDialogComponent, {
      width: '30%',
      data: element
    }).afterClosed().subscribe((val) => {
      if(val == 'update') {
        console.log('The dialog was closed');
        this.getAll(0, this.pageSize);
      }
    });
  }
  
  delete(id : string) {
    this.api.delete(id)
    .subscribe({
      next: () => {
        alert("Studentska grupa je uspešno obrisana!");
        this.getAll(0, this.pageSize)
      },
      error: () => {
        alert("Greška!");
      }
    })
  }

  onInputChange(event: Event) {
    const inputValue = (event.target as HTMLInputElement)?.value || '';
    if(inputValue != '') {
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
