import { Component, Inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { map, Observable, startWith } from 'rxjs';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { OrganizacionaJedinicaDto } from '../dtos/OrganizacionaJedinicaDto';
import { PredavacService } from '../services/predavac.service';

@Component({
  selector: 'app-predavac-dialog',
  templateUrl: './predavac-dialog.component.html',
  styleUrls: ['./predavac-dialog.component.scss']
})
export class PredavacDialogComponent {
  
  title: string = "Novi predavač"
  actionBtn: string = "Sačuvaj"
  organizacioneJedinice: OrganizacionaJedinicaDto[] = [];
  options: string[] = [];
  filteredOptions!: Observable<string[]>;
  predavacForm!: FormGroup

  constructor(private formBuilder: FormBuilder, 
    private api: PredavacService,
    @Inject(MAT_DIALOG_DATA) public editData : any, 
    private dialogRef: MatDialogRef<PredavacDialogComponent>) {}

  ngOnInit(): void {
    this.predavacForm = this.formBuilder.group({
      oznaka : ['', Validators.required],
      ime : ['', Validators.required],
      prezime: ['', Validators.required],
      titula: ['', Validators.required],
      organizacijaFakulteta : [false, Validators.required],
      dekanat : [false, Validators.required],
      orgJedinica: ['', Validators.required],
    })

    if(this.editData) {
      this.title = "Izmeni predavača";
      this.actionBtn = "Sačuvaj izmene";
      this.predavacForm.controls['oznaka'].setValue(this.editData.oznaka);
      this.predavacForm.controls['ime'].setValue(this.editData.ime);
      this.predavacForm.controls['prezime'].setValue(this.editData.prezime);
      this.predavacForm.controls['titula'].setValue(this.editData.titula);
      this.predavacForm.controls['organizacijaFakulteta'].setValue(this.editData.organizacijaFakulteta);
      this.predavacForm.controls['dekanat'].setValue(this.editData.dekanat);
      this.predavacForm.controls['orgJedinica'].setValue(this.editData.orgJedinica);
    }


    this.api.getAllKatedra()
    .subscribe({
      next: (res) => {
        this.organizacioneJedinice = res;
        res.forEach((element: OrganizacionaJedinicaDto) => {
          this.options.push(element.naziv)
        });
      }
    })

    this.api.getAllDepartman()
    .subscribe({
      next: (res) => {
        this.organizacioneJedinice.push(...res);
        res.forEach((element: OrganizacionaJedinicaDto) => {
          this.options.push(element.naziv)
        });
      }
    })

    this.filteredOptions = this.predavacForm.get('orgJedinica')!.valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value || '')),
    );
  }

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();

    return this.options.filter(option => option.toLowerCase().includes(filterValue));
  }

  add() {
    if(!this.editData) {
      if(this.predavacForm.valid) {
        let orgJedinica : string = this.predavacForm.value.orgJedinica
        this.predavacForm.value.orgJedinica = this.organizacioneJedinice.filter(o => o.naziv == orgJedinica).map(value => value.id)[0];
        this.api.post(this.predavacForm.value)
        .subscribe({
          next: () => {
            alert("Novi predavač je uspešno dodat!");
            this.predavacForm.reset();
            this.dialogRef.close('save');
          },
          error:() => {
            alert("Greška")
          }
        })
      }
    } else {
      this.update();
    }   
  }
  update() {
    if(!this.predavacForm.valid) {
      return
    }
    let orgJedinica : string = this.predavacForm.value.orgJedinica
    this.predavacForm.value.orgJedinica = this.organizacioneJedinice.filter(o => o.naziv == orgJedinica).map(value => value.id)[0];
    this.api.put(this.predavacForm.value, this.editData.id)
    .subscribe({
      next: () => {
        alert("Predavač je uspešno izmenjen!");
        this.predavacForm.reset();
        this.dialogRef.close('update');
      },
      error: () => {
        alert("Greška!");
      }
    })
  }
}
