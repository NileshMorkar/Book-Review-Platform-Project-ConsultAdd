import { inject } from '@angular/core';
import { CanActivateFn } from '@angular/router';
import { Router } from '@angular/router';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);

  if (typeof window !== 'undefined') {
    const token = localStorage.getItem('accessToken');
    if (token) {
      return true;
    }
  }
  
  router.navigate(['/login']);
  return false;
};
