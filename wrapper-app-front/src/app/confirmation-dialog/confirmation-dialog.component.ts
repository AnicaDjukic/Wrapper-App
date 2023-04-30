import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-confirmation-dialog',
  templateUrl: './confirmation-dialog.component.html',
  styleUrls: ['./confirmation-dialog.component.scss']
})
export class ConfirmationDialogComponent implements OnInit {
  
  constructor(@Inject(MAT_DIALOG_DATA) public data : any,
    public dialogRef: MatDialogRef<ConfirmationDialogComponent>) {}

    public title!: string;
    public message!: string;
    public warn!: string;

  ngOnInit(): void {
    if(this.data.plan) {
      this.title = 'predmeta';
      this.message = '(' + this.data.plan + ') ' + this.data.oznaka + ' ' + this.data.naziv; 
      this.warn = 'Obrisan predmet se briše i iz realizacije!';
    } else if (this.data.ime) {
      this.title = 'predavača';
      this.message = this.data.oznaka + ' ' + this.data.ime + ' ' + this.data.prezime;
      this.warn = 'Obrisan predavač se briše i iz realizacije!';
    } else if (this.data.kapacitet){
      this.title = 'prostorije';
      this.message = this.data.oznaka;
    } else if (this.data.stepenStudija) {
      this.title = 'studijskog programa';
      this.message = this.data.oznaka + ' ' + this.data.naziv;
      this.warn = 'Obrisan studijski program se briše iz realizacije, a samim tim se brišu i svi njegovi predmeti';
    } else if(this.data.brojStudenata) {
      this.title = 'studentske grupe';
      this.message = this.data.oznaka + '. grupu na ' + this.data.godina + '. godini na studijskom programu \"' + this.data.studijskiProgram + '\"';
    }
  }

}
