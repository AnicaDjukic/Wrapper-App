import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DatabaseService } from '../services/database.service';
import { DatabaseDto } from '../dtos/DatabaseDto';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-novi-semestar',
  templateUrl: './novi-semestar.component.html',
  styleUrls: ['./novi-semestar.component.scss']
})
export class NoviSemestarComponent {

  semestarForm!: FormGroup;
  options: DatabaseDto[] = [];

  constructor(private formBuilder: FormBuilder,
    private databaseApi: DatabaseService,
    private toastr: ToastrService) { }

  ngOnInit() {
    this.getDatabases();
    this.semestarForm = this.formBuilder.group({
      semestar: ['', [Validators.required]],
      godina: ['', [Validators.required]],
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

  submit() {
    let dto = {
      semestar: this.semestarForm.value.semestar,
      godina: this.semestarForm.value.godina,
      predmeti: this.semestarForm.value.firstSemestar,
      studijskiProgrami: this.semestarForm.value.firstSemestar,
      studentskeGrupe: this.semestarForm.value.firstSemestar,
      realizacija: this.semestarForm.value.firstSemestar,
      predavaci: this.semestarForm.value.secondSemestar,
      prostorije: this.semestarForm.value.secondSemestar
    }

    this.databaseApi.post(dto)
      .subscribe({
        next: (res) => {
          this.options.push(res);
          this.toastr.success('Novi semestar je uspešno napravljen!', 'Uspešno!');
          setTimeout(() => {
            window.location.reload();
          }, 2000);
        },
        error: (message) => {
          if (message.error.message) {

            console.log(message);
          } else {
            this.toastr.error('Došlo je do greške prilikom dodavanja novog semestra!', 'Greška!');
          }

        }
      });
  }


}
