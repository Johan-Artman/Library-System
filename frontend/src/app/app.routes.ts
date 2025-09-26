import { Routes } from '@angular/router';
import { MembersComponent } from './components/members/members.component';
import { BooksComponent } from './components/books/books.component';

export const routes: Routes = [
  { path: '', redirectTo: '/members', pathMatch: 'full' },
  { path: 'members', component: MembersComponent },
  { path: 'books', component: BooksComponent },
  { path: '**', redirectTo: '/members' }
];
