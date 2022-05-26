import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { InvestorPortfolioService } from '../service/investor-portfolio.service';
import { IInvestorPortfolio, InvestorPortfolio } from '../investor-portfolio.model';

import { InvestorPortfolioUpdateComponent } from './investor-portfolio-update.component';

describe('InvestorPortfolio Management Update Component', () => {
  let comp: InvestorPortfolioUpdateComponent;
  let fixture: ComponentFixture<InvestorPortfolioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let investorPortfolioService: InvestorPortfolioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [InvestorPortfolioUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(InvestorPortfolioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InvestorPortfolioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    investorPortfolioService = TestBed.inject(InvestorPortfolioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const investorPortfolio: IInvestorPortfolio = { id: 456 };

      activatedRoute.data = of({ investorPortfolio });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(investorPortfolio));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InvestorPortfolio>>();
      const investorPortfolio = { id: 123 };
      jest.spyOn(investorPortfolioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ investorPortfolio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: investorPortfolio }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(investorPortfolioService.update).toHaveBeenCalledWith(investorPortfolio);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InvestorPortfolio>>();
      const investorPortfolio = new InvestorPortfolio();
      jest.spyOn(investorPortfolioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ investorPortfolio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: investorPortfolio }));
      saveSubject.complete();

      // THEN
      expect(investorPortfolioService.create).toHaveBeenCalledWith(investorPortfolio);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InvestorPortfolio>>();
      const investorPortfolio = { id: 123 };
      jest.spyOn(investorPortfolioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ investorPortfolio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(investorPortfolioService.update).toHaveBeenCalledWith(investorPortfolio);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
