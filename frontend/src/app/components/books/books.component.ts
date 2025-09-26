import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BookService } from '../../services/book.service';
import { Book } from '../../models/book.model';

@Component({
  selector: 'app-books',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './books.component.html',
  styleUrls: ['./books.component.css']
})
export class BooksComponent implements OnInit {
  books: Book[] = [];
  selectedBook: Book | null = null;
  newBook: Book = this.createEmptyBook();
  showAddForm = false;

  constructor(private bookService: BookService) { }

  ngOnInit(): void {
    this.loadBooks();
  }

  loadBooks(): void {
    this.bookService.getAllBooks().subscribe({
      next: (books) => {
        console.log('Books received:', books);
        this.books = Array.isArray(books) ? books : [];
      },
      error: (error) => {
        console.error('Error loading books:', error);
        this.books = [];
      }
    });
  }

  selectBook(book: Book): void {
    this.selectedBook = { ...book };
  }

  addBook(): void {
    this.bookService.addBook(this.newBook).subscribe({
      next: (book) => {
        this.books.push(book);
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
      availableCopies: 0
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
}