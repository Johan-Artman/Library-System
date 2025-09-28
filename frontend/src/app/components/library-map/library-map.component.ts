import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';
import { LocationService } from '../../services/location.service';
import { UserLocation, BookLocation } from '../../models/location.model';

@Component({
  selector: 'app-library-map',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './library-map.component.html',
  styleUrls: ['./library-map.component.css']
})
export class LibraryMapComponent implements OnInit, OnDestroy {
  @Input() selectedBookIsbn?: number;
  @Input() showAllBooks = false;

  userLocation: UserLocation | null = null;
  bookLocations: BookLocation[] = [];
  selectedBookLocation: BookLocation | null = null;
  private subscription: Subscription = new Subscription();

  constructor(private locationService: LocationService) {}

  ngOnInit(): void {
    this.subscription.add(
      this.locationService.userLocation$.subscribe(location => {
        this.userLocation = location;
      })
    );

    this.bookLocations = this.locationService.getAllBookLocations();
    
    if (this.selectedBookIsbn) {
      this.selectedBookLocation = this.locationService.getBookLocation(this.selectedBookIsbn);
    }
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  get visibleBookLocations(): BookLocation[] {
    if (this.showAllBooks) {
      return this.bookLocations;
    }
    if (this.selectedBookLocation) {
      return [this.selectedBookLocation];
    }
    return [];
  }

  moveToBook(isbn: number): void {
    this.locationService.moveToBook(isbn).subscribe({
      next: () => {
        console.log(`Moved to book location for ISBN: ${isbn}`);
      },
      error: (error) => {
        console.error('Error moving to book location:', error);
      }
    });
  }

  get libraryWidth(): number {
    return this.locationService.LIBRARY_WIDTH;
  }

  get libraryHeight(): number {
    return this.locationService.LIBRARY_HEIGHT;
  }
}