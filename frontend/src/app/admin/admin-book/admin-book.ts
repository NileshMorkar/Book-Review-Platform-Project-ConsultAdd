import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BookService } from '../../services/book';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-admin-book',
  templateUrl: './admin-book.html',
  styleUrls: ['./admin-book.css'],
  standalone: true,
  imports: [CommonModule]
})
export class AdminBookComponent implements OnInit {
  book: any = null;
  comments: any[] = [];
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
        this.bookService.getBookById(+id).subscribe({
        next: (book) => {
      this.book = book;
      this.loading = false;
    },
    error: () => {
      this.error = 'Failed to load book';
      this.loading = false;
    }
  });

      this.bookService.getCommentsByBookId(+id).subscribe({
        next: (comments) => {
          this.comments = comments;
        },
        error: () => {
          console.error('Failed to load comments');
        }
      });
    } else {
      this.error = 'Invalid book ID';
      this.loading = false;
    }
  }

  onEdit() {
    if (this.book) {
      this.router.navigate(['/admin-dashboard/edit', this.book.id]);
    }
  }
  onDelete() {
  if (this.book && confirm('Are you sure you want to delete this book?')) {
    this.bookService.deleteBook(this.book.id).subscribe({
      next: () => {
        alert('Book deleted successfully!');
        this.router.navigate(['/admin-dashboard/book']); // redirect
      },
      error: (err) => {
        console.error('Error deleting book:', err);
        alert('Failed to delete book');
      }
    });
  }
}
}

