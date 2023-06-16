import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DatabaseService } from '../services/database.service';
import { DatabaseDto } from '../dtos/DatabaseDto';
import { ToastrService } from 'ngx-toastr';
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { skolskaGodinaValidator } from '../validators/skolska-godina-validator';

@Component({
  selector: 'app-novi-semestar',
  templateUrl: './novi-semestar.component.html',
  styleUrls: ['./novi-semestar.component.scss']
})
export class NoviSemestarComponent {

  semestarForm!: FormGroup;
  options: DatabaseDto[] = [];
  submitted: boolean = false;

  constructor(private formBuilder: FormBuilder,
    private databaseApi: DatabaseService,
    private toastr: ToastrService,
    public dialog: MatDialog) { }

  ngOnInit() {
    this.getDatabases();
    this.semestarForm = this.formBuilder.group({
      semestar: ['', [Validators.required]],
      godina: ['', [Validators.required, skolskaGodinaValidator]],
      firstSemestar: ['', [Validators.required]],
      secondSemestar: ['', [Validators.required]]
    });
  }

  getDatabases() {
    this.databaseApi.getAll()
      .subscribe({
        next: (res) => {
          this.options = res.reverse();
        }
      });
  }

  add() {
    let dto = this.prepareData();
    this.submitted = true;
    this.databaseApi.post(dto)
      .subscribe({
        next: (res) => {
          this.options.push(res);
          this.submitted = false;
          this.toastr.success('Novi semestar je uspešno napravljen!', 'Uspešno!');
          setTimeout(() => {
            window.location.reload();
          }, 2000);
        },
        error: (message) => {
          if (message.error.message) {
            this.submitted = false;
            this.openConfirmationDialog();
          } else {
            this.submitted = false;
            this.toastr.error('Došlo je do greške prilikom dodavanja novog semestra!', 'Greška!');
          }
        }
      });
  }

  openConfirmationDialog() {
    this.dialog.open(ConfirmationDialogComponent, {
      width: '40%',
      data: {
        database: true,
        semestar: this.semestarForm.value.semestar,
        godina: this.semestarForm.value.godina
      }
    }).afterClosed().subscribe((val) => {
      if (val) {
        this.update();
      }
    });
  }

  update() {
    let dto = this.prepareData();
    this.submitted = true;
    this.databaseApi.put(dto)
      .subscribe({
        next: (res) => {
          this.options.push(res);
          this.submitted = false;
          this.toastr.success('Semestar je uspešno napravljen!', 'Uspešno!');
          setTimeout(() => {
            window.location.reload();
          }, 2000);
        },
        error: () => {
          this.toastr.error('Došlo je do greške prilikom dodavanja novog semestra!', 'Greška!');
        }
      });

  }

  prepareData(): any {
    return {
      semestar: this.semestarForm.value.semestar,
      godina: this.semestarForm.value.godina,
      predmeti: this.semestarForm.value.firstSemestar,
      studijskiProgrami: this.semestarForm.value.firstSemestar,
      studentskeGrupe: this.semestarForm.value.firstSemestar,
      realizacija: this.semestarForm.value.firstSemestar,
      predavaci: this.semestarForm.value.secondSemestar,
      prostorije: this.semestarForm.value.secondSemestar
    }
  }


}
