import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminEditBook } from './admin-edit-book';

describe('AdminEditBook', () => {
  let component: AdminEditBook;
  let fixture: ComponentFixture<AdminEditBook>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminEditBook]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminEditBook);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
