import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InvestorPortfolioDetailComponent } from './investor-portfolio-detail.component';

describe('InvestorPortfolio Management Detail Component', () => {
  let comp: InvestorPortfolioDetailComponent;
  let fixture: ComponentFixture<InvestorPortfolioDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InvestorPortfolioDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ investorPortfolio: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(InvestorPortfolioDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(InvestorPortfolioDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load investorPortfolio on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.investorPortfolio).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
