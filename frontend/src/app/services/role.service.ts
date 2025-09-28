import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

export enum UserRole {
  LIBRARIAN = 'LIBRARIAN',
  LENDER = 'LENDER'
}

export interface User {
  role: UserRole;
  name: string;
}

@Injectable({
  providedIn: 'root'
})
export class RoleService {
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor() {
    // Check if user role is stored in localStorage
    const storedUser = localStorage.getItem('currentUser');
    if (storedUser) {
      this.currentUserSubject.next(JSON.parse(storedUser));
    }
  }

  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }

  setUser(user: User): void {
    this.currentUserSubject.next(user);
    localStorage.setItem('currentUser', JSON.stringify(user));
  }

  logout(): void {
    this.currentUserSubject.next(null);
    localStorage.removeItem('currentUser');
  }

  isLibrarian(): boolean {
    const user = this.getCurrentUser();
    return user?.role === UserRole.LIBRARIAN;
  }

  isLender(): boolean {
    const user = this.getCurrentUser();
    return user?.role === UserRole.LENDER;
  }

  hasAccess(requiredRole: UserRole): boolean {
    const user = this.getCurrentUser();
    if (!user) return false;
    
    // Librarians have access to everything
    if (user.role === UserRole.LIBRARIAN) return true;
    
    // For other roles, check exact match
    return user.role === requiredRole;
  }
}