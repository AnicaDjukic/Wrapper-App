import { Component, OnInit } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';
import { Router } from '@angular/router';
import { DatabaseDto } from '../dtos/DatabaseDto';
import { DatabaseService } from '../services/database.service';


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

  database = '2022/23Z';
  options: DatabaseDto[] = [];

  constructor(private breakpointObserver: BreakpointObserver,
    public router: Router,
    private databaseApi: DatabaseService) { }

  ngOnInit(): void {
    this.getDatabases();
  }

  getDatabases() {
    this.databaseApi.getAll()
      .subscribe({
        next: (res) => {
          this.options = res.reverse();
          this.database = this.options[0].godina + this.options[0].semestar.substring(0, 1);
          if (!window.sessionStorage.getItem('semestar')) {
            window.sessionStorage.setItem('semestar', this.database);
          } else {
            this.database = window.sessionStorage.getItem('semestar')!;
          }
        }
      });
  }

  changeDatabase() {
    console.log(this.database);
    window.sessionStorage.setItem('semestar', this.database);
    this.databaseApi.switch(this.database.replace("/", "_"))
      .subscribe({
        next: () => {
          let currentUrl = this.router.url;
          if (currentUrl != '/') {
            this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
              this.router.navigate([currentUrl]);
            });
          } else {
            window.location.reload();
          }
        }
      });
  }

}
