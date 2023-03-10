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
  displayedColumns: string[] = ['oznaka', 'godina', 'semestar', 'brojStudenata', 'studijskiProgram', 'actions'];
  dataSource! : MatTableDataSource<StudentskaGrupaDto>;

  @ViewChild(MatPaginator)
  paginator!: MatPaginator;

  ngOnInit() {
    this.getAll()
  }

  constructor(private api: StudentskaGrupaService, public dialog: MatDialog) {}

  getAll() {
    this.api.getAll()
    .subscribe({
      next:(res) => {
        this.dataSource = new MatTableDataSource(res);
        this.dataSource.paginator = this.paginator
      }
    })
  }

  openDialog(): void {
    this.dialog.open(StudentskaGrupaDialogComponent, {
      width: '30%'
    }).afterClosed().subscribe((val) => {
      if(val == 'save') {
        console.log('The dialog was closed');
        this.getAll();
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
        this.getAll();
      }
    });
  }
  
  delete(id : string) {
    this.api.delete(id)
    .subscribe({
      next: () => {
        alert("Studentska grupa je uspešno obrisana!");
        this.getAll()
      },
      error: () => {
        alert("Greška!");
      }
    })
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }
}
