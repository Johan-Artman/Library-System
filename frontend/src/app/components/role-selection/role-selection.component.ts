import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { RoleService, UserRole, User } from '../../services/role.service';

@Component({
  selector: 'app-role-selection',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './role-selection.component.html',
  styleUrls: ['./role-selection.component.css']
})
export class RoleSelectionComponent implements OnInit {
  UserRole = UserRole;
  selectedRole: UserRole | null = null;
  userName: string = '';

  constructor(
    private roleService: RoleService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Check if user is already logged in
    const currentUser = this.roleService.getCurrentUser();
    if (currentUser) {
      this.router.navigate(['/books']);
    }
  }

  selectRole(role: UserRole): void {
    this.selectedRole = role;
  }

  login(): void {
    if (this.selectedRole && this.userName.trim()) {
      const user: User = {
        role: this.selectedRole,
        name: this.userName.trim()
      };
      this.roleService.setUser(user);
      this.router.navigate(['/books']);
    }
  }

  getRoleDisplayName(role: UserRole): string {
    switch (role) {
      case UserRole.LIBRARIAN:
        return 'Librarian';
      case UserRole.LENDER:
        return 'Lender/Student';
      default:
        return '';
    }
  }

  getRoleDescription(role: UserRole): string {
    switch (role) {
      case UserRole.LIBRARIAN:
        return 'Full access to manage books, members, and lending operations';
      case UserRole.LENDER:
        return 'Search and view books only';
      default:
        return '';
    }
  }
}