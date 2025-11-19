import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InscriptionDetail } from './inscription-detail';

describe('InscriptionDetail', () => {
  let component: InscriptionDetail;
  let fixture: ComponentFixture<InscriptionDetail>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [InscriptionDetail]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InscriptionDetail);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
