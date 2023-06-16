import { Component } from '@angular/core';
import { DatabaseDto } from '../dtos/DatabaseDto';
import { DatabaseService } from '../services/database.service';

@Component({
  selector: 'app-generisanje-rasporeda',
  templateUrl: './generisanje-rasporeda.component.html',
  styleUrls: ['./generisanje-rasporeda.component.scss']
})
export class GenerisanjeRasporedaComponent {

  semesters: DatabaseDto[] = [];
  options: DatabaseDto[] = [];
  selectedSemester!: string;

  constructor(private databaseApi: DatabaseService) { }

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
    this.databaseApi.getAll().subscribe({
      next: (res) => {
        for(let option of res.reverse()) {
          if(!option.generationStarted || option.generationFinished) {
            this.options.push(option);
          }
        }
      }
    })
  }

  submit() {
    console.log(this.selectedSemester);
  }
}
