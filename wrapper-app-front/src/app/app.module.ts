import { APP_INITIALIZER, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './material/material.module';
import { NavComponent } from './nav/nav.component';
import { LayoutModule } from '@angular/cdk/layout';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { PredmetiComponent } from './predmeti/predmeti.component';
import { PredavaciComponent } from './predavaci/predavaci.component';
import { ProstorijeComponent } from './prostorije/prostorije.component';
import { StudijskiProgramiComponent } from './studijski-programi/studijski-programi.component';
import { StudentskeGrupeComponent } from './studentske-grupe/studentske-grupe.component';
import { RealizacijaComponent } from './realizacija/realizacija.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { PredmetDialogComponent } from './predmet-dialog/predmet-dialog.component';
import { ReactiveFormsModule } from '@angular/forms';
import { PredavacDialogComponent } from './predavac-dialog/predavac-dialog.component';
import { StudijskiProgramDialogComponent } from './studijski-program-dialog/studijski-program-dialog.component';
import { StudentskaGrupaDialogComponent } from './studentska-grupa-dialog/studentska-grupa-dialog.component';
import { ProstorijaDialogComponent } from './prostorija-dialog/prostorija-dialog.component';
import { RouterModule } from '@angular/router';
import { ToastrModule, ToastrService } from 'ngx-toastr';
import { RealizacijaDialogComponent } from './realizacija-dialog/realizacija-dialog.component';
import { FormsModule } from '@angular/forms';
import { ConfirmationDialogComponent } from './confirmation-dialog/confirmation-dialog.component';
import { NoviSemestarComponent } from './novi-semestar/novi-semestar.component';
import { GenerisanjeRasporedaComponent } from './generisanje-rasporeda/generisanje-rasporeda.component';
import { LoginComponent } from './login/login.component';
import { AuthInterceptorService } from './services/auth-interceptor.service';
import { ConfigService } from './services/config.service';

export function loadConfig(configService: ConfigService) {
  return () => configService.loadConfig();
}

@NgModule({
  declarations: [
    AppComponent,
    NavComponent,
    PredmetiComponent,
    PredavaciComponent,
    ProstorijeComponent,
    StudijskiProgramiComponent,
    StudentskeGrupeComponent,
    RealizacijaComponent,
    PredmetDialogComponent,
    PredavacDialogComponent,
    StudijskiProgramDialogComponent,
    StudentskaGrupaDialogComponent,
    ProstorijaDialogComponent,
    RealizacijaDialogComponent,
    ConfirmationDialogComponent,
    NoviSemestarComponent,
    GenerisanjeRasporedaComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MaterialModule,
    LayoutModule,
    MatToolbarModule,
    MatButtonModule,
    MatSidenavModule,
    MatIconModule,
    MatListModule,
    HttpClientModule,
    ReactiveFormsModule,
    RouterModule,
    ToastrModule.forRoot(),
    FormsModule
  ],
  providers: [ToastrService, {
    provide: HTTP_INTERCEPTORS,
    useClass: AuthInterceptorService,
    multi: true
  }, ConfigService,
  {
    provide: APP_INITIALIZER,
    useFactory: loadConfig,
    deps: [ConfigService],
    multi: true
  }],
  bootstrap: [AppComponent]
})
export class AppModule { }
