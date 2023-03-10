import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { PredavacDto } from '../dtos/PredavacDto';
import { PredavacDialogComponent } from '../predavac-dialog/predavac-dialog.component';
import { PredavacService } from '../services/predavac.service';

@Component({
  selector: 'app-predavaci',
  templateUrl: './predavaci.component.html',
  styleUrls: ['./predavaci.component.scss']
})
export class PredavaciComponent implements OnInit {
  displayedColumns: string[] = ['oznaka', 'ime', 'prezime', 'titula', 'organizacijaFakulteta', 'dekanat', 'orgJedinica', 'actions'];
  dataSource! : MatTableDataSource<PredavacDto>;

  @ViewChild(MatPaginator)
  paginator!: MatPaginator;

  ngOnInit() {
    this.getAll()
  }

  constructor(private api: PredavacService, public dialog: MatDialog) {}

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
    this.dialog.open(PredavacDialogComponent, {
      width: '30%'
    }).afterClosed().subscribe((val) => {
      if(val == 'save') {
        console.log('The dialog was closed');
        this.getAll();
      }
    });
  }

  edit(element : any) {
    this.dialog.open(PredavacDialogComponent, {
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
        alert("Predavac je uspešno obrisan!");
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
