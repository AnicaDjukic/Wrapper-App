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
  
  dataSource = new MatTableDataSource<PredavacDto>();
  @ViewChild(MatPaginator, { static: true }) paginator!: MatPaginator;

  constructor(private api: PredavacService, public dialog: MatDialog) { }

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
    this.dialog.open(PredavacDialogComponent, {
      width: '40%'
    }).afterClosed().subscribe((val) => {
      if (val == 'save') {
        console.log('The dialog was closed');
        this.getAll(0, this.pageSize);
      }
    });
  }

  edit(element: any) {
    this.dialog.open(PredavacDialogComponent, {
      width: '40%',
      data: element
    }).afterClosed().subscribe((val) => {
      if (val == 'update') {
        console.log('The dialog was closed');
        this.getAll(0, this.pageSize);
      }
    });
  }

  delete(id: string) {
    this.api.delete(id)
      .subscribe({
        next: () => {
          alert("Predavac je uspešno obrisan!");
          this.getAll(0, this.pageSize)
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
