import { Component, OnInit } from '@angular/core';
import { BookService } from '../../services/book';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-admin-book-view',
  templateUrl: './admin-book-view.html',
  imports: [FormsModule,CommonModule],
  styleUrls: ['./admin-book-view.css'],
})
export class AdminBookViewComponent implements OnInit {
  books: any[] = [];
  filteredBooks: any[] = [];
  searchTerm: string = '';

  constructor(private bookService: BookService,private router: Router) {}

  ngOnInit() {
    this.bookService.getBooks().subscribe({
      next: (data) => {
        this.books = data;
        this.filteredBooks = data;
      },
      error: (err) => console.error('Error fetching books', err),
    });
  }

  onSearch() {
    const term = this.searchTerm.toLowerCase();

    this.filteredBooks = this.books.filter((book) => {
      return (
        book.title.toLowerCase().includes(term) ||
        book.author.toLowerCase().includes(term) ||
        book.genre.toLowerCase().includes(term)
      );
    });
  }

  // getImageSrc(byteString: string): string {
  //   return `data:image/jpeg;base64,${byteString}`;
  // }

  onCardClick(book: any) {
    this.router.navigate(['/admin-dashboard/books', book.id]);
    console.log('Clicked book:', book);
  }
}

