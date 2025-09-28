import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-lending',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './lending.component.html',
  styleUrls: ['./lending.component.css']
})
export class LendingComponent implements OnInit {
  
  constructor() { }

  ngOnInit(): void {
    // Lending functionality will be implemented later
  }
}