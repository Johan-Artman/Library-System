import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Book } from '../models/book.model';

@Injectable({
  providedIn: 'root'
})
export class BookService {
  private apiUrl = 'http://localhost:8080/api/books';

  constructor(private http: HttpClient) { }

  getAllBooks(): Observable<Book[]> {
    return this.http.get<Book[]>(this.apiUrl);
  }

  getBookByIsbn(isbn: number): Observable<Book> {
    return this.http.get<Book>(`${this.apiUrl}/${isbn}`);
  }

  addBook(book: Book): Observable<Book> {
    return this.http.post<Book>(this.apiUrl, book);
  }

  updateBook(isbn: number, book: Book): Observable<Book> {
    return this.http.put<Book>(`${this.apiUrl}/${isbn}`, book);
  }

  deleteBook(isbn: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${isbn}`);
  }

  searchBooks(title?: string): Observable<Book[]> {
    if (title && title.trim()) {
      return this.http.get<Book[]>(`${this.apiUrl}/search?title=${encodeURIComponent(title.trim())}`);
    } else {
      return this.getAllBooks();
    }
  }

  getBooksByFloor(floorLevel: number): Observable<Book[]> {
    return this.http.get<Book[]>(`${this.apiUrl}/floor/${floorLevel}`);
  }
}