import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InvestorDetailComponent } from './investor-detail.component';

describe('Investor Management Detail Component', () => {
  let comp: InvestorDetailComponent;
  let fixture: ComponentFixture<InvestorDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InvestorDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ investor: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(InvestorDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(InvestorDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load investor on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.investor).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
