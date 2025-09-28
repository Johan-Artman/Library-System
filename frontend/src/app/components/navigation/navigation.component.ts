import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { RoleService, UserRole, User } from '../../services/role.service';

interface MenuItem {
  path: string;
  label: string;
  icon: string;
  requiresRole?: UserRole;
}

@Component({
  selector: 'app-navigation',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css']
})
export class NavigationComponent implements OnInit, OnDestroy {
  currentUser: User | null = null;
  private subscription: Subscription = new Subscription();

  allMenuItems: MenuItem[] = [
    { path: '/books', label: 'Books', icon: '📚' },
    { path: '/members', label: 'Members', icon: '👥', requiresRole: UserRole.LIBRARIAN },
    { path: '/lending', label: 'Lending', icon: '📋', requiresRole: UserRole.LIBRARIAN }
  ];

  constructor(
    private roleService: RoleService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.subscription.add(
      this.roleService.currentUser$.subscribe((user: User | null) => {
        this.currentUser = user;
      })
    );
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  get visibleMenuItems(): MenuItem[] {
    if (!this.currentUser) return [];
    
    return this.allMenuItems.filter(item => {
      if (!item.requiresRole) return true;
      return this.roleService.hasAccess(item.requiresRole);
    });
  }

  get userDisplayName(): string {
    return this.currentUser?.name || '';
  }

  get userRole(): string {
    switch (this.currentUser?.role) {
      case UserRole.LIBRARIAN:
        return 'Librarian';
      case UserRole.LENDER:
        return 'Lender';
      default:
        return '';
    }
  }

  logout(): void {
    this.roleService.logout();
    this.router.navigate(['/login']);
  }
}