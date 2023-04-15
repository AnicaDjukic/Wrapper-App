import { Component, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { ProstorijaDto } from '../dtos/ProstorijaDto';
import { ProstorijaDialogComponent } from '../prostorija-dialog/prostorija-dialog.component';
import { ProstorijaService } from '../services/prostorija.service';

@Component({
  selector: 'app-prostorije',
  templateUrl: './prostorije.component.html',
  styleUrls: ['./prostorije.component.scss']
})
export class ProstorijeComponent {
  displayedColumns: string[] = ['oznaka', 'tip', 'kapacitet', 'orgJedinica', 'actions'];
  dataSource! : MatTableDataSource<ProstorijaDto>;

  @ViewChild(MatPaginator)
  paginator!: MatPaginator;

  ngOnInit() {
    this.getAll()
  }

  constructor(private api: ProstorijaService, public dialog: MatDialog) {}

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
    this.dialog.open(ProstorijaDialogComponent, {
      width: '40%'
    }).afterClosed().subscribe((val) => {
      if(val == 'save') {
        console.log('The dialog was closed');
        this.getAll();
      }
    });
  }

  edit(element : any) {
    this.dialog.open(ProstorijaDialogComponent, {
      width: '40%',
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
        alert("Prostorija je uspešno obrisana!");
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
