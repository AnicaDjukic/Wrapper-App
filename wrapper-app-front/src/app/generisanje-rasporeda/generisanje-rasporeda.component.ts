import { Component } from '@angular/core';
import { DatabaseDto } from '../dtos/DatabaseDto';
import { DatabaseService } from '../services/database.service';
import { RasporedService } from '../services/raspored.service';
import { ToastrService } from 'ngx-toastr';
import { tap } from 'rxjs';

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
        for(let sem of res) {
          if (sem.status != null && sem.status != 'NOT_STARTED' && sem.status != 'STOPPED') {
            this.semesters.push(sem);
          }
        }
        this.semesters = this.semesters.sort((a, b) => new Date(b.generationStarted).getTime() - new Date(a.generationStarted).getTime());
      }
    });
  }

  getAllOptions() {
    this.databaseApi.getAllUnblocked().subscribe({
      next: (res) => {
        for (let option of res.reverse()) {
          if (option.status != 'STARTED' && option.status != 'OPTIMIZING') {
            this.options.push(option);
          }
        }
      }
    });
  }

  isDisabled() {
    return !this.selectedSemester || this.semesters.find(s => s.status == 'STARTED' || s.status == 'OPTIMIZING');
  }

  isGenerating() {
    return this.semesters.find(s => s.status == 'STARTED' || s.status == 'OPTIMIZING');
  }

  submit() {
    this.submitted = true;
    this.rasporedApi.generate(this.selectedSemester)
      .subscribe();
    window.location.reload();
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
          setTimeout(() => {
            window.location.reload();
          }, 1000);
        },
        error: () => {
          this.toastr.error('Došlo je do greške! Pokušajte ponovo...', 'Greška!');
        }
      });
  }

  async refresh(id: string) {
    this.rasporedApi.generate(id).pipe(
      tap(() => {
        window.location.reload();
      })
    ).subscribe();
  }

}
