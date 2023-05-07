import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { PredavacDto } from '../dtos/PredavacDto';
import { PredavacDialogComponent } from '../predavac-dialog/predavac-dialog.component';
import { PredavacService } from '../services/predavac.service';
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog.component';
import { ToastrService } from 'ngx-toastr';
import { RealizacijaService } from '../services/realizacija.service';
import { OrganizacionaJedinicaDto } from '../dtos/OrganizacionaJedinicaDto';
import { OrgJedinicaService } from '../services/org-jedinica.service';

@Component({
  selector: 'app-predavaci',
  templateUrl: './predavaci.component.html',
  styleUrls: ['./predavaci.component.scss']
})
export class PredavaciComponent implements OnInit {
  displayedColumns: string[] = ['oznaka', 'ime', 'prezime', 'titula', 'organizacijaFakulteta', 'dekanat', 'orgJedinica', 'actions'];

  dataSource = new MatTableDataSource<PredavacDto>();
  organizacioneJedinice: OrganizacionaJedinicaDto[] = [];
  options: string[] = [];
  @ViewChild(MatPaginator, { static: true }) paginator!: MatPaginator;

  constructor(private api: PredavacService,
    private orgjedinicaApi: OrgJedinicaService,
    private realizacijaApi: RealizacijaService, 
    public dialog: MatDialog, 
    private toastr: ToastrService) { }

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
    this.getOrganizacioneJediniceOptions();
  }

  getAll(page: number, size: number) {
    this.api.getAll(page, size).subscribe(res => {
      this.dataSource = new MatTableDataSource(res.content);
      this.totalElements = res.totalElements;
      this.pageIndex = res.pageable.pageNumber;
    });
  }

  getOrganizacioneJediniceOptions() {
    this.orgjedinicaApi.getAllKatedra()
    .subscribe({
      next: (res) => {
        this.organizacioneJedinice = res;
        res.forEach((element: OrganizacionaJedinicaDto) => {
          this.options.push(element.naziv)
        });
      }
    })

    this.orgjedinicaApi.getAllDepartman()
    .subscribe({
      next: (res) => {
        this.organizacioneJedinice.push(...res);
        res.forEach((element: OrganizacionaJedinicaDto) => {
          this.options.push(element.naziv)
        });
      }
    })
  }

  openDialog(): void {
    this.dialog.open(PredavacDialogComponent, {
      width: '40%',
      data: {
        options: this.options,
        organizacioneJedinice: this.organizacioneJedinice
      }
    }).afterClosed().subscribe((val) => {
      if (val == 'save') {
        if(this.oznaka || this.ime || this.prezime || this.orgJedinica) {
          this.applyFilter(this.pageIndex, this.pageSize);
        } else {
          this.getAll(this.pageIndex, this.pageSize);
        }
      }
    });
  }

  edit(element: any) {
    this.dialog.open(PredavacDialogComponent, {
      width: '40%',
      data: {
        editData: element,
        options: this.options,
        organizacioneJedinice: this.organizacioneJedinice
      }
    }).afterClosed().subscribe((val) => {
      if (val == 'update') {
        if(this.oznaka || this.ime || this.prezime || this.orgJedinica) {
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
      if(val) {
        this.delete(element.id);
      }
    });
  }

  delete(id: string) {
    this.realizacijaApi.deletePredavac(id)
      .subscribe({
        next: () => {
          this.toastr.success('Predavač je uspešno obrisan!', 'Uspešno!');
          if(this.oznaka || this.ime || this.prezime || this.orgJedinica) {
            this.applyFilter(this.pageIndex, this.pageSize);
          } else {
            this.getAll(this.pageIndex, this.pageSize);
          }
        },
        error: () => {
          this.toastr.error('Greška prilikom brisanja predavača!', 'Greška!');
        }
      })
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
