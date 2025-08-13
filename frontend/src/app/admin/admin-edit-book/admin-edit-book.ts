
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BookService } from '../../services/book';

@Component({
  selector: 'app-admin-edit-book',
  standalone: true,
  templateUrl: './admin-edit-book.html',
  styleUrls: ['./admin-edit-book.css'],
  imports: [CommonModule, FormsModule]
})
export class AdminEditBook implements OnInit {
  book: any = {
    title: '',
    author: '',
    publicationYear: '',
    rating: 0,
    likeCount: 0,
    imageUrl: ''
  };
  loading = true;
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private bookService: BookService,
    private router: Router
  ) {}

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.bookService.getBooks().subscribe({
        next: (books) => {
          const found = books.find(b => b.id === +id);
          if (found) {
            this.book = { ...found };
          } else {
            this.error = 'Book not found';
          }
          this.loading = false;
        },
        error: () => {
          this.error = 'Failed to load book';
          this.loading = false;
        }
      });
    } else {
      this.error = 'Invalid book ID';
      this.loading = false;
    }
  }

  onSave() {
    console.log('Sending book data:', this.book); 
    this.bookService.updateBook(this.book.id, this.book).subscribe({
      next: () => {
        alert('Book updated successfully!');
        this.router.navigate([`/admin-dashboard/books/${this.book.id}`]);
      },
      error: () => {
        this.error = 'Failed to update book';
      }
    });
  }

  onCancel() {
    this.router.navigate(['/admin-books']);
  }
}

