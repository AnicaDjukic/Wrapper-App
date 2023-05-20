import { Component } from '@angular/core';
import { AuthenticationService } from '../services/authentication.service';
import { StorageService } from '../services/storage.service';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TokenDto } from '../dtos/TokenDto';
import { ToastrService } from 'ngx-toastr';

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
    private router: Router,
    private toastr: ToastrService) { }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]]
    });
  }

  login() {
    if (this.loginForm.valid) { }

    this.authService.login(this.loginForm.value).subscribe(
      (res) => {
        const tokenDto: TokenDto = res as TokenDto;
        this.storageService.storeToken(tokenDto.token);
        this.router.navigateByUrl('/predmeti');
      },
      () => {
        this.toastr.error('Pogrešno korisničko ime ili lozinka', 'Greška');
      }
    )
  }

}


