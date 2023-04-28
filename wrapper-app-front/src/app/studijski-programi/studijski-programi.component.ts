import { Component, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { StudijskiProgramDto } from '../dtos/StudijskiProgramDto';
import { StudijskiProgramService } from '../services/studijski-program.service';
import { StudijskiProgramDialogComponent } from '../studijski-program-dialog/studijski-program-dialog.component';

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
    this.getAll()
  }

  constructor(private api: StudijskiProgramService, public dialog: MatDialog) {}

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
        console.log('The dialog was closed');
        this.getAll();
      }
    });
  }

  edit(element : any) {
    this.dialog.open(StudijskiProgramDialogComponent, {
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
        alert("Studijski program je uspešno obrisan!");
        this.getAll()
      },
      error: () => {
        alert("Greška!");
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
