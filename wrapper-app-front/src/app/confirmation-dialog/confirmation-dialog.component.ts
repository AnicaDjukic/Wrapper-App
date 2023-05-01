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
    
    text = '';

  ngOnInit(): void {
    if(this.data.plan) {
      this.title = 'Brisanje predmeta';
      this.message = '(' + this.data.plan + ') ' + this.data.oznaka + ' ' + this.data.naziv; 
      this.warn = 'Obrisan predmet se briše i iz realizacije!';
    } else if (this.data.ime) {
      this.title = 'Brisanje predavača';
      this.message = this.data.oznaka + ' ' + this.data.ime + ' ' + this.data.prezime;
      this.warn = 'Obrisan predavač se briše i iz realizacije!';
    } else if (this.data.kapacitet){
      this.title = 'Brisanje prostorije';
      this.message = this.data.oznaka;
    } else if (this.data.stepenStudija) {
      this.title = 'Brisanje studijskog programa';
      this.message = this.data.oznaka + ' ' + this.data.naziv;
      this.warn = 'Obrisan studijski program se briše i iz realizacije, a samim tim se brišu i svi njegovi predmeti!';
    } else if(this.data.brojStudenata) {
      this.title = 'Brisanje studentske grupe';
      this.message = this.data.oznaka + '. grupu na ' + this.data.godina + '. godini na studijskom programu \"' + this.data.studijskiProgram + '\"';
    } else {
      this.title = 'Uklanjanje predmeta iz realizacije';
      this.message = '(' + this.data.predmetPlan + ') ' + this.data.predmetOznaka + ' ' + this.data.predmetNaziv;
    }
  }

}
