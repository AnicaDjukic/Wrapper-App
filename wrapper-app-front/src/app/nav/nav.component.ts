import { Component, OnInit } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';
import { Router } from '@angular/router';
import { DatabaseDto } from '../dtos/DatabaseDto';
import { DatabaseService } from '../services/database.service';
import { StorageService } from '../services/storage.service';


@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.scss']
})
export class NavComponent implements OnInit {

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

  database = '';
  options: DatabaseDto[] = [];

  constructor(private breakpointObserver: BreakpointObserver,
    public router: Router,
    private databaseApi: DatabaseService,
    private storageService: StorageService) { }

  ngOnInit(): void {
    if (this.storageService.getToken()) {
      this.getDatabases();
    }
  }

  getDatabases() {
    this.databaseApi.getAll()
      .subscribe({
        next: (res) => {
          this.options = res.reverse();
          let active = this.options.find(o => o.status != 'STARTED' && o.status != 'OPTIMIZING');
          if(active) {
            this.database = active.godina + active.semestar.substring(0, 1);
          }
          if (!window.sessionStorage.getItem('semestar')) {
            window.sessionStorage.setItem('semestar', this.database);
          } else {
            let activated = this.options.find(o => o.godina + o.semestar.substring(0, 1) == window.sessionStorage.getItem('semestar')!);
            if(activated?.status != 'STARTED' && activated?.status != 'OPTIMIZING') {
                this.database = window.sessionStorage.getItem('semestar')!;
            }
          }
        }
      });
  }

  changeDatabase() {
    if(!this.database) {
      return;
    }
    console.log(this.database);
    window.sessionStorage.setItem('semestar', this.database);
    this.databaseApi.switch(this.database.replace("/", "_"))
      .subscribe({
        next: () => {
          let currentUrl = this.router.url;
          this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
            this.router.navigate([currentUrl]);
          });

        }
      });
  }

  logout() {
    this.database = this.options[0].godina + this.options[0].semestar.substring(0, 1);
    this.storageService.clearToken();
    window.sessionStorage.removeItem('semestar');
    this.router.navigate(['']);
  }

}
