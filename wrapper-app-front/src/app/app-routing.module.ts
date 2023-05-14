import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PredavaciComponent } from './predavaci/predavaci.component';
import { PredmetiComponent } from './predmeti/predmeti.component';
import { ProstorijeComponent } from './prostorije/prostorije.component';
import { RealizacijaComponent } from './realizacija/realizacija.component';
import { StudentskeGrupeComponent } from './studentske-grupe/studentske-grupe.component';
import { StudijskiProgramiComponent } from './studijski-programi/studijski-programi.component';
import { NoviSemestarComponent } from './novi-semestar/novi-semestar.component';

const routes: Routes = [
  { path: "", component: PredmetiComponent },
  { path: "predmeti", component: PredmetiComponent },
  { path: "predavaci", component: PredavaciComponent },
  { path: "prostorije", component: ProstorijeComponent },
  { path: "studijski-programi", component: StudijskiProgramiComponent },
  { path: "studentske-grupe", component: StudentskeGrupeComponent },
  { path: "realizacija", component: RealizacijaComponent },
  { path: "novi-semestar", component: NoviSemestarComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
