import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PredavaciComponent } from './predavaci/predavaci.component';
import { PredmetiComponent } from './predmeti/predmeti.component';
import { ProstorijeComponent } from './prostorije/prostorije.component';
import { RealizacijaComponent } from './realizacija/realizacija.component';
import { StudentskeGrupeComponent } from './studentske-grupe/studentske-grupe.component';
import { StudijskiProgramiComponent } from './studijski-programi/studijski-programi.component';
import { NoviSemestarComponent } from './novi-semestar/novi-semestar.component';
import { GenerisanjeRasporedaComponent } from './generisanje-rasporeda/generisanje-rasporeda.component';
import { LoginComponent } from './login/login.component';
import { AuthGuard } from './auth-guard/auth.guard';

const routes: Routes = [
  { path: "", component: LoginComponent },
  { path: "predmeti", component: PredmetiComponent, canActivate: [AuthGuard] },
  { path: "predavaci", component: PredavaciComponent, canActivate: [AuthGuard] },
  { path: "prostorije", component: ProstorijeComponent, canActivate: [AuthGuard] },
  { path: "studijski-programi", component: StudijskiProgramiComponent, canActivate: [AuthGuard] },
  { path: "studentske-grupe", component: StudentskeGrupeComponent, canActivate: [AuthGuard] },
  { path: "realizacija", component: RealizacijaComponent, canActivate: [AuthGuard] },
  { path: "novi-semestar", component: NoviSemestarComponent, canActivate: [AuthGuard]},
  { path: "generisanje-rasporeda", component: GenerisanjeRasporedaComponent, canActivate: [AuthGuard]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
