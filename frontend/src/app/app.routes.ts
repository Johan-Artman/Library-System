import { Routes } from '@angular/router';
import { MembersComponent } from './components/members/members.component';
import { BooksComponent } from './components/books/books.component';
import { LendingComponent } from './components/lending/lending.component';
import { RoleSelectionComponent } from './components/role-selection/role-selection.component';
import { AuthGuard } from './guards/auth.guard';
import { RoleGuard } from './guards/role.guard';
import { UserRole } from './services/role.service';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: RoleSelectionComponent },
  { path: 'books', component: BooksComponent, canActivate: [AuthGuard] },
  { 
    path: 'members', 
    component: MembersComponent, 
    canActivate: [AuthGuard, RoleGuard],
    data: { requiredRole: UserRole.LIBRARIAN }
  },
  { 
    path: 'lending', 
    component: LendingComponent, 
    canActivate: [AuthGuard, RoleGuard],
    data: { requiredRole: UserRole.LIBRARIAN }
  },
  { path: '**', redirectTo: '/login' }
];
