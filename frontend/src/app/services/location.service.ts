import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Location, BookLocation, UserLocation } from '../models/location.model';

@Injectable({
  providedIn: 'root'
})
export class LocationService {
  private userLocationSubject = new BehaviorSubject<UserLocation | null>(null);
  public userLocation$ = this.userLocationSubject.asObservable();

  // Mock book locations - in a real app, this would come from a database
  private bookLocations: Map<number, BookLocation> = new Map([
    [9780743273565, { x: 120, y: 80, description: 'Fiction Section A-M, Shelf 3', isbn: 9780743273565, title: 'The Great Gatsby', shelfId: 'A3' }],
    [9780451524935, { x: 220, y: 120, description: 'Classic Literature, Shelf 7', isbn: 9780451524935, title: '1984', shelfId: 'CL7' }],
    [9780061120084, { x: 180, y: 160, description: 'Fiction Section A-M, Shelf 5', isbn: 9780061120084, title: 'To Kill a Mockingbird', shelfId: 'A5' }]
  ]);

  // Library floor dimensions (simplified single floor)
  readonly LIBRARY_WIDTH = 400;
  readonly LIBRARY_HEIGHT = 300;

  constructor() {
    // Simulate user entering library at entrance
    this.setUserLocation(50, 280, 'Library Entrance');
  }

  getCurrentUserLocation(): UserLocation | null {
    return this.userLocationSubject.value;
  }

  setUserLocation(x: number, y: number, description: string): void {
    const location: UserLocation = {
      x,
      y,
      description,
      timestamp: new Date()
    };
    this.userLocationSubject.next(location);
  }

  getBookLocation(isbn: number): BookLocation | null {
    return this.bookLocations.get(isbn) || null;
  }

  getAllBookLocations(): BookLocation[] {
    return Array.from(this.bookLocations.values());
  }

  // Simulate movement to a book location
  moveToBook(isbn: number): Observable<UserLocation> {
    return new Observable(observer => {
      const bookLocation = this.getBookLocation(isbn);
      if (bookLocation) {
        // Simulate walking to the book location
        setTimeout(() => {
          this.setUserLocation(
            bookLocation.x, 
            bookLocation.y, 
            `At ${bookLocation.description}`
          );
          observer.next(this.userLocationSubject.value!);
          observer.complete();
        }, 1000); // Simulate 1 second walk time
      } else {
        observer.error('Book location not found');
      }
    });
  }

  // Calculate distance between two points
  calculateDistance(loc1: Location, loc2: Location): number {
    return Math.sqrt(
      Math.pow(loc2.x - loc1.x, 2) + Math.pow(loc2.y - loc1.y, 2)
    );
  }
}