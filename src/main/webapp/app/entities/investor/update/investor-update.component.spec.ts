import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { InvestorService } from '../service/investor.service';
import { IInvestor, Investor } from '../investor.model';
import { IInvestorPortfolio } from 'app/entities/investor-portfolio/investor-portfolio.model';
import { InvestorPortfolioService } from 'app/entities/investor-portfolio/service/investor-portfolio.service';

import { InvestorUpdateComponent } from './investor-update.component';

describe('Investor Management Update Component', () => {
  let comp: InvestorUpdateComponent;
  let fixture: ComponentFixture<InvestorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let investorService: InvestorService;
  let investorPortfolioService: InvestorPortfolioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [InvestorUpdateComponent],
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
      .overrideTemplate(InvestorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InvestorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    investorService = TestBed.inject(InvestorService);
    investorPortfolioService = TestBed.inject(InvestorPortfolioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call portfolio query and add missing value', () => {
      const investor: IInvestor = { id: 456 };
      const portfolio: IInvestorPortfolio = { id: 1382 };
      investor.portfolio = portfolio;

      const portfolioCollection: IInvestorPortfolio[] = [{ id: 939 }];
      jest.spyOn(investorPortfolioService, 'query').mockReturnValue(of(new HttpResponse({ body: portfolioCollection })));
      const expectedCollection: IInvestorPortfolio[] = [portfolio, ...portfolioCollection];
      jest.spyOn(investorPortfolioService, 'addInvestorPortfolioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ investor });
      comp.ngOnInit();

      expect(investorPortfolioService.query).toHaveBeenCalled();
      expect(investorPortfolioService.addInvestorPortfolioToCollectionIfMissing).toHaveBeenCalledWith(portfolioCollection, portfolio);
      expect(comp.portfoliosCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const investor: IInvestor = { id: 456 };
      const portfolio: IInvestorPortfolio = { id: 44590 };
      investor.portfolio = portfolio;

      activatedRoute.data = of({ investor });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(investor));
      expect(comp.portfoliosCollection).toContain(portfolio);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Investor>>();
      const investor = { id: 123 };
      jest.spyOn(investorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ investor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: investor }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(investorService.update).toHaveBeenCalledWith(investor);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Investor>>();
      const investor = new Investor();
      jest.spyOn(investorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ investor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: investor }));
      saveSubject.complete();

      // THEN
      expect(investorService.create).toHaveBeenCalledWith(investor);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Investor>>();
      const investor = { id: 123 };
      jest.spyOn(investorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ investor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(investorService.update).toHaveBeenCalledWith(investor);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackInvestorPortfolioById', () => {
      it('Should return tracked InvestorPortfolio primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackInvestorPortfolioById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
