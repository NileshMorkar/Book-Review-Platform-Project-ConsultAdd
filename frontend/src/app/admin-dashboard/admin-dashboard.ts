import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-dashboard',
  imports: [RouterOutlet],
  templateUrl: './admin-dashboard.html',
  styleUrl: './admin-dashboard.css'
})
export class AdminDashboard {

  constructor(private router: Router) {}

  logout() {
    localStorage.clear(); // Clear everything
    this.router.navigate(['/login']); // Redirect to login page
  }

}
