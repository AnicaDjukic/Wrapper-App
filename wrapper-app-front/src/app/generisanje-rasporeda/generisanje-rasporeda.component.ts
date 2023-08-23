import { Component } from '@angular/core';
import { DatabaseDto } from '../dtos/DatabaseDto';
import { DatabaseService } from '../services/database.service';
import { RasporedService } from '../services/raspored.service';
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
        this.semesters = this.semesters.sort((a, b) => new Date(b.generationStarted).getTime() - new Date(a.generationStarted).getTime());
      }
    });
  }

  getAllOptions() {
    // this.databaseApi.getAllUnblocked().subscribe({
    //   next: (res) => {
    //     for (let option of res.reverse()) {
    //       if (option.status == 'NOT_STARTED' || option.status == 'STOPPED' || option.status == 'FINISHED') {
    //         this.options.push(option);
    //       }
    //     }
    //   }
    // })
    this.databaseApi.getAll().subscribe({
      next: (res) => {
        for (let option of res.reverse()) {
          if (option.status != 'STARTED' && option.status != 'OPTIMIZING') {
            this.options.push(option);
          }
        }
      }
    });
  }

  submit() {
    this.submitted = true;
    this.rasporedApi.generate(this.selectedSemester)
      .subscribe({
        next: () => {
          window.location.reload();
        },
        error: () => {
          this.submitted = false;
          this.toastr.error('Došlo je do greške prilikom dodavanja novog semestra!', 'Greška!');
        }
      });
  }

  send(id: string) {
    this.toastr.warning('Uskoro ćete dobiti raspored na mejl', 'Obaveštenje!')
    this.rasporedApi.send(id)
      .subscribe({
        next: () => {
          this.toastr.success('Raspored je uspešno poslat na mejl!', 'Uspešno!');
        },
        error: () => {
          this.toastr.error('Došlo je do greške prilikom slanja rasporeda! Pokušajte ponovo...', 'Greška!');
        }
      });
  }

  stop(): void {
    this.rasporedApi.stop()
      .subscribe({
        next: () => {
          this.toastr.success('Generisanje je zaustavljeno!', 'Uspešno!');
          this.submitted = false;
          this.selectedSemester = "";
          this.semesters = [];
          this.options = [];
          this.getAllSemesters();
          this.getAllOptions();
        },
        error: () => {
          this.toastr.error('Došlo je do greške! Pokušajte ponovo...', 'Greška!');
        }
      });
  }

  refresh(id: string) {
    this.rasporedApi.generate(id)
      .subscribe({
        next: () => {
          window.location.reload();
        },
        error: () => {
          this.submitted = false;
          this.toastr.error('Došlo je do greške prilikom dodavanja novog semestra!', 'Greška!');
        }
      });
  }

}
