import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BookService } from '../../services/book.service';
import { RoleService, UserRole } from '../../services/role.service';
import { LocationService } from '../../services/location.service';
import { LibraryMapComponent } from '../library-map/library-map.component';
import { Book } from '../../models/book.model';

@Component({
  selector: 'app-books',
  standalone: true,
  imports: [CommonModule, FormsModule, LibraryMapComponent],
  templateUrl: './books.component.html',
  styleUrls: ['./books.component.css']
})
export class BooksComponent implements OnInit {
  books: Book[] = [];
  filteredBooks: Book[] = [];
  selectedBook: Book | null = null;
  newBook: Book = this.createEmptyBook();
  showAddForm = false;
  searchQuery: string = '';
  isSearching: boolean = false;
  showLocation = false;

  constructor(
    private bookService: BookService,
    private roleService: RoleService,
    private locationService: LocationService
  ) { }

  ngOnInit(): void {
    this.loadBooks();
  }

  get isLibrarian(): boolean {
    return this.roleService.isLibrarian();
  }

  loadBooks(): void {
    this.bookService.getAllBooks().subscribe({
      next: (books) => {
        console.log('Books received:', books);
        this.books = Array.isArray(books) ? books : [];
        this.filteredBooks = [...this.books];
      },
      error: (error) => {
        console.error('Error loading books:', error);
        this.books = [];
        this.filteredBooks = [];
      }
    });
  }

  searchBooks(): void {
    this.isSearching = true;
    this.bookService.searchBooks(this.searchQuery).subscribe({
      next: (books) => {
        console.log('Search results:', books);
        this.filteredBooks = Array.isArray(books) ? books : [];
        this.isSearching = false;
      },
      error: (error) => {
        console.error('Error searching books:', error);
        this.filteredBooks = [];
        this.isSearching = false;
      }
    });
  }

  clearSearch(): void {
    this.searchQuery = '';
    this.filteredBooks = [...this.books];
  }

  selectBook(book: Book): void {
    this.selectedBook = { ...book };
  }

  showBookLocation(book: Book): void {
    this.selectedBook = { ...book };
    this.showLocation = true;
    // Try to move to the book location
    this.locationService.moveToBook(book.isbn).subscribe({
      next: () => {
        console.log(`Showing location for: ${book.title}`);
      },
      error: (error) => {
        console.log(`Location not available for: ${book.title}`);
      }
    });
  }

  toggleLocationView(): void {
    this.showLocation = !this.showLocation;
  }

  addBook(): void {
    this.bookService.addBook(this.newBook).subscribe({
      next: (book) => {
        this.books.push(book);
        this.filteredBooks.push(book);
        this.newBook = this.createEmptyBook();
        this.showAddForm = false;
      },
      error: (error) => {
        console.error('Error adding book:', error);
      }
    });
  }

  updateBook(): void {
    if (this.selectedBook) {
      this.bookService.updateBook(this.selectedBook.isbn, this.selectedBook).subscribe({
        next: (updatedBook) => {
          const index = this.books.findIndex(b => b.isbn === updatedBook.isbn);
          if (index !== -1) {
            this.books[index] = updatedBook;
          }
          const filteredIndex = this.filteredBooks.findIndex(b => b.isbn === updatedBook.isbn);
          if (filteredIndex !== -1) {
            this.filteredBooks[filteredIndex] = updatedBook;
          }
          this.selectedBook = null;
        },
        error: (error) => {
          console.error('Error updating book:', error);
        }
      });
    }
  }

  deleteBook(isbn: number): void {
    if (confirm('Are you sure you want to delete this book?')) {
      this.bookService.deleteBook(isbn).subscribe({
        next: () => {
          this.books = this.books.filter(b => b.isbn !== isbn);
          this.filteredBooks = this.filteredBooks.filter(b => b.isbn !== isbn);
          if (this.selectedBook && this.selectedBook.isbn === isbn) {
            this.selectedBook = null;
          }
        },
        error: (error) => {
          console.error('Error deleting book:', error);
        }
      });
    }
  }

  private createEmptyBook(): Book {
    return {
      isbn: 0,
      title: '',
      availableCopies: 0,
      shelfLocation: 'General',
      floorLevel: 1
    };
  }

  cancelEdit(): void {
    this.selectedBook = null;
  }

  toggleAddForm(): void {
    this.showAddForm = !this.showAddForm;
    if (!this.showAddForm) {
      this.newBook = this.createEmptyBook();
    }
  }

  filterByFloor(event: any): void {
    const selectedFloor = event.target.value;
    if (selectedFloor === '') {
      this.filteredBooks = [...this.books];
    } else {
      this.filteredBooks = this.books.filter(book => 
        book.floorLevel === parseInt(selectedFloor)
      );
    }
  }

  trackByIsbn(index: number, book: Book): number {
    return book.isbn;
  }
}