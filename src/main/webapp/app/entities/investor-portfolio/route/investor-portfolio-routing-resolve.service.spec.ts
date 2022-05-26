import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IInvestorPortfolio, InvestorPortfolio } from '../investor-portfolio.model';
import { InvestorPortfolioService } from '../service/investor-portfolio.service';

import { InvestorPortfolioRoutingResolveService } from './investor-portfolio-routing-resolve.service';

describe('InvestorPortfolio routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: InvestorPortfolioRoutingResolveService;
  let service: InvestorPortfolioService;
  let resultInvestorPortfolio: IInvestorPortfolio | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(InvestorPortfolioRoutingResolveService);
    service = TestBed.inject(InvestorPortfolioService);
    resultInvestorPortfolio = undefined;
  });

  describe('resolve', () => {
    it('should return IInvestorPortfolio returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInvestorPortfolio = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInvestorPortfolio).toEqual({ id: 123 });
    });

    it('should return new IInvestorPortfolio if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInvestorPortfolio = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultInvestorPortfolio).toEqual(new InvestorPortfolio());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as InvestorPortfolio })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInvestorPortfolio = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInvestorPortfolio).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
