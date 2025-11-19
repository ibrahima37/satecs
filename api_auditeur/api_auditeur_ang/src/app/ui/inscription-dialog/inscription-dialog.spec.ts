import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InscriptionDialog } from './inscription-dialog';

describe('InscriptionDialog', () => {
  let component: InscriptionDialog;
  let fixture: ComponentFixture<InscriptionDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [InscriptionDialog]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InscriptionDialog);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
