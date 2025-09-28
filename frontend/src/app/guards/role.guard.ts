import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, Router } from '@angular/router';
import { RoleService, UserRole } from '../services/role.service';

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {
  constructor(
    private roleService: RoleService,
    private router: Router
  ) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {
    const requiredRole = route.data['requiredRole'] as UserRole;
    
    if (!requiredRole) {
      // No role requirement specified, allow access if user is authenticated
      return !!this.roleService.getCurrentUser();
    }

    const hasAccess = this.roleService.hasAccess(requiredRole);
    
    if (!hasAccess) {
      // Redirect to books page (default accessible page)
      this.router.navigate(['/books']);
      return false;
    }
    
    return true;
  }
}