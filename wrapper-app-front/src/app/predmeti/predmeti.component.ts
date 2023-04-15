import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { PredmetDto } from '../dtos/PredmetDto';
import { ApiService } from '../services/api.service';
import { MatDialog } from '@angular/material/dialog';
import { PredmetDialogComponent } from '../predmet-dialog/predmet-dialog.component';

@Component({
  selector: 'app-predmeti',
  templateUrl: './predmeti.component.html',
  styleUrls: ['./predmeti.component.scss']
})
export class PredmetiComponent implements OnInit {
  
  displayedColumns: string[] = ['oznaka', 'plan', 'naziv', 'sifraStruke', 'godina', 'studijskiProgram', 
  'brojCasovaPred', 'brojCasovaAud', 'brojCasovaLab', 'brojCasovaRac', 'actions'];
  dataSource! : MatTableDataSource<PredmetDto>;

  @ViewChild(MatPaginator)
  paginator!: MatPaginator;

  windowScrolled = false;

  ngOnInit() {
    this.getAll();
    window.addEventListener('scroll', () => {
      this.windowScrolled = window.pageYOffset !== 0;
    });

  }

  constructor(private api: ApiService, public dialog: MatDialog) {}

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
    this.dialog.open(PredmetDialogComponent, {
      width: '40%'
    }).afterClosed().subscribe((val) => {
      if(val == 'save') {
        console.log('The dialog was closed');
        this.getAll();
      }
    });
  }

  edit(element : any) {
    this.dialog.open(PredmetDialogComponent, {
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
        alert("Predmet je uspešno obrisan!");
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

  scrollToTop(): void {
    window.scrollTo(0, 0);
  }
}


