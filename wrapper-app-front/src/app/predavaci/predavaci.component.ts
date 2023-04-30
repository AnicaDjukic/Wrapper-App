import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { PredavacDto } from '../dtos/PredavacDto';
import { PredavacDialogComponent } from '../predavac-dialog/predavac-dialog.component';
import { PredavacService } from '../services/predavac.service';
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog.component';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-predavaci',
  templateUrl: './predavaci.component.html',
  styleUrls: ['./predavaci.component.scss']
})
export class PredavaciComponent implements OnInit {
  displayedColumns: string[] = ['oznaka', 'ime', 'prezime', 'titula', 'organizacijaFakulteta', 'dekanat', 'orgJedinica', 'actions'];

  dataSource = new MatTableDataSource<PredavacDto>();
  @ViewChild(MatPaginator, { static: true }) paginator!: MatPaginator;

  constructor(private api: PredavacService, public dialog: MatDialog, private toastr: ToastrService) { }

  oznaka = '';
  ime = '';
  prezime = '';
  orgJedinica = '';

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
          this.toastr.success('Predavač je uspešno obrisan!', 'Uspešno!');
          this.getAll(0, this.pageSize)
        },
        error: () => {
          alert("Greška!");
        }
      })
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

  applyFilter(page: number, size: number) {
    let predavacSearchDto = {
      oznaka: this.oznaka,
      ime: this.ime,
      prezime: this.prezime,
      orgJedinica: this.orgJedinica
    }
    this.api.search(page, size, predavacSearchDto).subscribe({
      next: (res) => {
        this.dataSource = new MatTableDataSource(res.content);
        this.totalElements = res.totalElements;
        this.pageIndex = res.pageable.pageNumber;
      }
    });
  }
}
