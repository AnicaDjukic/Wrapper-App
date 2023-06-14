import { Component } from '@angular/core';
import { AuthenticationService } from '../services/authentication.service';
import { StorageService } from '../services/storage.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TokenDto } from '../dtos/TokenDto';
import { ToastrService } from 'ngx-toastr';
import { DatabaseService } from '../services/database.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  loginForm!: FormGroup;

  constructor(private formBuilder: FormBuilder,
    private authService: AuthenticationService,
    private storageService: StorageService,
    private toastr: ToastrService,
    private databaseApi: DatabaseService) { }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]]
    });
  }

  async login() {
    if (this.loginForm.valid) { }

    this.authService.login(this.loginForm.value).subscribe(
      async (res) => {
        const tokenDto: TokenDto = res as TokenDto;
        this.storageService.storeTokenData(tokenDto.token, tokenDto.refreshToken);
        await this.setDatabase();
        window.location.href = '/predmeti';
      },
      () => {
        this.toastr.error('Pogrešno korisničko ime ili lozinka', 'Greška');
      }
    )
  }

  async setDatabase(): Promise<any> {
    return new Promise<void>((resolve, reject) => {
      this.databaseApi.getAll().subscribe({
        next: (res) => {
          let options = res.reverse();
          let database = options[0].godina + options[0].semestar.substring(0, 1);
          window.sessionStorage.setItem('semestar', database);
          this.databaseApi.switch(database.replace("/", "_")).subscribe({
            complete: () => {
              resolve();
            },
            error: (err) => {
              reject(err);
            }
          });
        },
        error: (err) => {
          reject(err);
        }
      });
    });
  }
}


