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
  
  dataSource = new MatTableDataSource<ProstorijaDto>();
  @ViewChild(MatPaginator)
  paginator!: MatPaginator;

  ngOnInit() {
    this.dataSource.paginator = this.paginator;
    this.getAll(0, this.pageSize);
  }

  constructor(private api: ProstorijaService, public dialog: MatDialog) {}

  oznaka = '';
  tip = 'SVE';
  kapacitet = 0;
  kapacitetStr = '';
  orgJedinica = '';

  pageSize = 10;
  totalElements = 0;
  pageIndex = 0;

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
    this.dialog.open(ProstorijaDialogComponent, {
      width: '40%'
    }).afterClosed().subscribe((val) => {
      if(val == 'save') {
        console.log('The dialog was closed');
        this.getAll(0, this.pageSize);
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
        this.getAll(0, this.pageSize);
      }
    });
  }
  
  delete(id : string) {
    this.api.delete(id)
    .subscribe({
      next: () => {
        alert("Prostorija je uspešno obrisana!");
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
      this.kapacitet = Number(inputValue);
    }
    this.kapacitetStr = inputValue;
  }

  applyFilter(page: number, size: number) {
    let prostorijaSearchDto = {
      oznaka: this.oznaka,
      tip: this.tip != 'SVE' ? this.tip : '',
      kapacitet: this.kapacitetStr,
      orgJedinica: this.orgJedinica
    }
    this.api.search(page, size, prostorijaSearchDto).subscribe({
      next: (res) => {
        this.dataSource = new MatTableDataSource(res.content);
        this.totalElements = res.totalElements;
        this.pageIndex = res.pageable.pageNumber;
      }
    });
  }
}
