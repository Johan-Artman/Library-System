import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { RoleService } from '../services/role.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(
    private roleService: RoleService,
    private router: Router
  ) {}

  canActivate(): boolean {
    const currentUser = this.roleService.getCurrentUser();
    if (currentUser) {
      return true;
    }
    
    this.router.navigate(['/login']);
    return false;
  }
}