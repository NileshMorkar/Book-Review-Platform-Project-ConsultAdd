import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class BookService {
  private apiUrl = 'http://localhost:8080/api/books';

  constructor(private http: HttpClient) {}

  /** Get all books */
  getBooks(): Observable<any[]> {
    const token = localStorage.getItem('accessToken');
    const headers = this.getAuthHeaders();

    return this.http.get<any>(this.apiUrl, { headers }).pipe(
      map(response => response.data) // adjust according to your API response
    );
  }

  /** Get comments for a specific book */
  getCommentsByBookId(bookId: number): Observable<any[]> {
    const headers = this.getAuthHeaders();
    const url = `${this.apiUrl}/comments/${bookId}`;

    return this.http.get<any>(url, { headers }).pipe(
      map(response => response.data) // API returns { data: [...] }
    );
  }

  getBookById(bookId: number): Observable<any> {
  const headers = this.getAuthHeaders();
  const url = `${this.apiUrl}/${bookId}`;
  return this.http.get<any>(url, { headers })

}
updateBook(id: number, bookData: any) {
  const token = localStorage.getItem('accessToken');
  const headers = new HttpHeaders({
    'Authorization': `Bearer ${token}`
  });
  return this.http.put(`${this.apiUrl}/${id}`, bookData, { headers });
}

deleteBook(bookId: number): Observable<any> {
  const headers = this.getAuthHeaders();
  return this.http.delete(`${this.apiUrl}/${bookId}`, { headers });
}

  /** Private helper to reuse auth headers */
  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('accessToken');
    return new HttpHeaders({
      'Authorization': `Bearer ${token || ''}`
    });
  }
}


