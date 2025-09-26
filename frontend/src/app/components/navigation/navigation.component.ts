import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-navigation',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css']
})
export class NavigationComponent {
  menuItems = [
    { path: '/members', label: 'Members', icon: '👥' },
    { path: '/books', label: 'Books', icon: '📚' },
    { path: '/lending', label: 'Lending', icon: '📋' }
  ];
}