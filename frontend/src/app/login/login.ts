import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, RouterModule],
  standalone: true,
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class Login {
  loginForm: FormGroup;
  isLoading = false;

  constructor(private fb: FormBuilder, private router: Router, private http: HttpClient) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
    });
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      this.isLoading = true;

      const formData = this.loginForm.value;

      this.http.post<{ jwtToken: string; role: string }>(
  'http://localhost:8080/api/auth/login',
  formData,
  { observe: 'response' }
).subscribe({
  next: (response) => {
    console.log('Full response:', response);
    if (response.status === 200) {
      const body = response.body!;
      localStorage.setItem('accessToken', body.jwtToken);
      localStorage.setItem('userRole', body.role);

      if (body.role === 'ROLE_ADMIN') {
        this.router.navigate(['/admin-dashboard']);
      } else {
        this.router.navigate(['/admin-dashboard']);
      }
    } else {
      alert('Unexpected response status: ' + response.status);
    }
  },
  error: (error) => {
    alert('Login failed. Please check your credentials.');
    console.error('Login failed:', error);
  },
  complete: () => {
    this.isLoading = false;
  }
});
    }
  }
}



