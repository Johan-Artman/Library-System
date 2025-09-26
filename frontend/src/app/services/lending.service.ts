import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Lending } from '../models/lending.model';

@Injectable({
  providedIn: 'root'
})
export class LendingService {
  private apiUrl = 'http://localhost:8080/api/lending';

  constructor(private http: HttpClient) { }

  getAllLendings(): Observable<Lending[]> {
    return this.http.get<Lending[]>(this.apiUrl);
  }

  getLendingById(id: number): Observable<Lending> {
    return this.http.get<Lending>(`${this.apiUrl}/${id}`);
  }

  lendBook(memberID: number, isbn: string): Observable<Lending> {
    return this.http.post<Lending>(`${this.apiUrl}/lend`, { memberID, isbn });
  }

  returnBook(lendingId: number): Observable<Lending> {
    return this.http.put<Lending>(`${this.apiUrl}/return/${lendingId}`, {});
  }

  getLendingsByMember(memberID: number): Observable<Lending[]> {
    return this.http.get<Lending[]>(`${this.apiUrl}/member/${memberID}`);
  }

  getActiveLendings(): Observable<Lending[]> {
    return this.http.get<Lending[]>(`${this.apiUrl}/active`);
  }

  getOverdueLendings(): Observable<Lending[]> {
    return this.http.get<Lending[]>(`${this.apiUrl}/overdue`);
  }
}