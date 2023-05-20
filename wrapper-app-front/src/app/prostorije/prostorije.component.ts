import { Component, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { ProstorijaDto } from '../dtos/ProstorijaDto';
import { ProstorijaDialogComponent } from '../prostorija-dialog/prostorija-dialog.component';
import { ProstorijaService } from '../services/prostorija.service';
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog.component';
import { ToastrService } from 'ngx-toastr';
import { OrganizacionaJedinicaDto } from '../dtos/OrganizacionaJedinicaDto';
import { OrgJedinicaService } from '../services/org-jedinica.service';

@Component({
  selector: 'app-prostorije',
  templateUrl: './prostorije.component.html',
  styleUrls: ['./prostorije.component.scss']
})
export class ProstorijeComponent {
  displayedColumns: string[] = ['oznaka', 'tip', 'kapacitet', 'orgJedinica', 'actions'];

  dataSource = new MatTableDataSource<ProstorijaDto>();
  organizacioneJedinice: OrganizacionaJedinicaDto[] = [];
  options: string[] = [];
  @ViewChild(MatPaginator)
  paginator!: MatPaginator;

  ngOnInit() {
    this.dataSource.paginator = this.paginator;
    this.getAll(0, this.pageSize);
    this.getOrganizacioneJediniceOptions();
  }

  constructor(private api: ProstorijaService,
    public dialog: MatDialog,
    private orgjedinicaApi: OrgJedinicaService,
    private toastr: ToastrService) { }

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
        next: (res) => {
          this.dataSource = new MatTableDataSource(res.content);
          this.totalElements = res.totalElements;
          this.pageIndex = res.pageable.pageNumber;
        }
      })
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

    // this.orgjedinicaApi.getAllDepartman()
    //   .subscribe({
    //     next: (res) => {
    //       this.organizacioneJedinice.push(...res);
    //       res.forEach((element: OrganizacionaJedinicaDto) => {
    //         this.options.push(element.naziv)
    //       });
    //     }
    //   })
  }

  openDialog(): void {
    this.dialog.open(ProstorijaDialogComponent, {
      width: '40%',
      data: {
        options: this.options,
        organizacioneJedinice: this.organizacioneJedinice
      }
    }).afterClosed().subscribe((val) => {
      if (val == 'save') {
        if(this.oznaka || this.tip != 'SVE' || this.kapacitet || this.orgJedinica) {
          this.applyFilter(this.pageIndex, this.pageSize);
        } else {
          this.getAll(this.pageIndex, this.pageSize);
        }
      }
    });
  }

  edit(element: any) {
    this.dialog.open(ProstorijaDialogComponent, {
      width: '40%',
      data: {
        editData: element,
        options: this.options,
        organizacioneJedinice: this.organizacioneJedinice
      }
    }).afterClosed().subscribe((val) => {
      if (val == 'update') {
        if(this.oznaka || this.tip != 'SVE' || this.kapacitet || this.orgJedinica) {
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
      if (val) {
        this.delete(element.id);
      }
    });
  }

  delete(id: string) {
    this.api.delete(id)
      .subscribe({
        next: () => {
          this.toastr.success('Prostorija je uspešno obrisana!', 'Uspešno!');
          if(this.oznaka || this.tip != 'SVE' || this.kapacitet || this.orgJedinica) {
            this.applyFilter(this.pageIndex, this.pageSize);
          } else {
            this.getAll(this.pageIndex, this.pageSize);
          }
        },
        error: () => {
          this.toastr.error('Greška prilikom brisanja prostorije!', 'Greška!');
        }
      })
  }

  onInputChange(event: Event) {
    const inputValue = (event.target as HTMLInputElement)?.value || '';
    if (inputValue != '') {
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
