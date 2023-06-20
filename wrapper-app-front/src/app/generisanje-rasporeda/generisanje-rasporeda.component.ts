import { Component } from '@angular/core';
import { DatabaseDto } from '../dtos/DatabaseDto';
import { DatabaseService } from '../services/database.service';
import { RasporedService } from '../services/raspored.service';
import { saveAs } from 'file-saver';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-generisanje-rasporeda',
  templateUrl: './generisanje-rasporeda.component.html',
  styleUrls: ['./generisanje-rasporeda.component.scss']
})
export class GenerisanjeRasporedaComponent {

  semesters: DatabaseDto[] = [];
  options: DatabaseDto[] = [];
  selectedSemester!: string;
  submitted!: boolean;

  path = "http://localhost:8080/api/v1/raspored/download/"

  constructor(private databaseApi: DatabaseService,
    private rasporedApi: RasporedService,
    private toastr: ToastrService) { }

  ngOnInit() {
    this.getAllSemesters();
    this.getAllOptions();
  }

  getAllSemesters() {
    this.databaseApi.getAll().subscribe({
      next: (res) => {
        this.semesters = res;
      }
    });
  }

  getAllOptions() {
    this.databaseApi.getAllUnblocked().subscribe({
      next: (res) => {
        for (let option of res.reverse()) {
          if (!option.generationStarted || option.generationFinished) {
            this.options.push(option);
          }
        }
      }
    })
  }

  submit() {
    this.submitted = true;
    this.rasporedApi.generate(this.selectedSemester)
      .subscribe({
        next: () => {
          this.submitted = false;
          this.selectedSemester = "";
          this.semesters = [];
          this.options = [];
          this.getAllSemesters();
          this.getAllOptions();
        },
        error: () => {
          this.submitted = false;
          this.toastr.error('Došlo je do greške prilikom dodavanja novog semestra!', 'Greška!');

        }
      });
  }

  download(filename: string): void {
    this.rasporedApi
      .download(filename)
      .subscribe(blob => saveAs(blob, filename));
  }
}
