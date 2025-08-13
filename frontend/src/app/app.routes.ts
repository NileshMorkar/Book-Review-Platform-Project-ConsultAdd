import { Routes ,RouterModule} from '@angular/router';
import { Login } from './login/login';
import { Register } from './register/register';
import { App } from './app';
import { authGuard } from './auth-guard';
import { UserDashboard  } from './user-dashboard/user-dashboard';
import { AdminDashboard } from './admin-dashboard/admin-dashboard';
import { NgModule } from '@angular/core';
import { AdminBookViewComponent} from './admin/admin-book-view/admin-book-view';
import { AdminBookComponent} from './admin/admin-book/admin-book';
import { AdminEditBook} from './admin/admin-edit-book/admin-edit-book';




export const routes: Routes = [
  { path: 'login', component: Login },
  { path: 'register', component: Register },
  { path: '', redirectTo: 'login', pathMatch: 'full' },
    {
    path: 'admin-dashboard',
    component: AdminDashboard,
    children: [
      { path: '', redirectTo: 'book', pathMatch: 'full' },
      { path: 'book', component:  AdminBookViewComponent},
      { path: 'books/:id', component: AdminBookComponent },
      { path: 'edit/:id', component: AdminEditBook }
    ]
  },

  { path: 'user-dashboard', component: UserDashboard , canActivate: [authGuard] }


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
