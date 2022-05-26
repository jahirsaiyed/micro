import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IInvestorPortfolio, InvestorPortfolio } from '../investor-portfolio.model';

import { InvestorPortfolioService } from './investor-portfolio.service';

describe('InvestorPortfolio Service', () => {
  let service: InvestorPortfolioService;
  let httpMock: HttpTestingController;
  let elemDefault: IInvestorPortfolio;
  let expectedResult: IInvestorPortfolio | IInvestorPortfolio[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InvestorPortfolioService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      units: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a InvestorPortfolio', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new InvestorPortfolio()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InvestorPortfolio', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          units: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InvestorPortfolio', () => {
      const patchObject = Object.assign(
        {
          units: 1,
        },
        new InvestorPortfolio()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InvestorPortfolio', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          units: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a InvestorPortfolio', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addInvestorPortfolioToCollectionIfMissing', () => {
      it('should add a InvestorPortfolio to an empty array', () => {
        const investorPortfolio: IInvestorPortfolio = { id: 123 };
        expectedResult = service.addInvestorPortfolioToCollectionIfMissing([], investorPortfolio);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(investorPortfolio);
      });

      it('should not add a InvestorPortfolio to an array that contains it', () => {
        const investorPortfolio: IInvestorPortfolio = { id: 123 };
        const investorPortfolioCollection: IInvestorPortfolio[] = [
          {
            ...investorPortfolio,
          },
          { id: 456 },
        ];
        expectedResult = service.addInvestorPortfolioToCollectionIfMissing(investorPortfolioCollection, investorPortfolio);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InvestorPortfolio to an array that doesn't contain it", () => {
        const investorPortfolio: IInvestorPortfolio = { id: 123 };
        const investorPortfolioCollection: IInvestorPortfolio[] = [{ id: 456 }];
        expectedResult = service.addInvestorPortfolioToCollectionIfMissing(investorPortfolioCollection, investorPortfolio);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(investorPortfolio);
      });

      it('should add only unique InvestorPortfolio to an array', () => {
        const investorPortfolioArray: IInvestorPortfolio[] = [{ id: 123 }, { id: 456 }, { id: 59327 }];
        const investorPortfolioCollection: IInvestorPortfolio[] = [{ id: 123 }];
        expectedResult = service.addInvestorPortfolioToCollectionIfMissing(investorPortfolioCollection, ...investorPortfolioArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const investorPortfolio: IInvestorPortfolio = { id: 123 };
        const investorPortfolio2: IInvestorPortfolio = { id: 456 };
        expectedResult = service.addInvestorPortfolioToCollectionIfMissing([], investorPortfolio, investorPortfolio2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(investorPortfolio);
        expect(expectedResult).toContain(investorPortfolio2);
      });

      it('should accept null and undefined values', () => {
        const investorPortfolio: IInvestorPortfolio = { id: 123 };
        expectedResult = service.addInvestorPortfolioToCollectionIfMissing([], null, investorPortfolio, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(investorPortfolio);
      });

      it('should return initial array if no InvestorPortfolio is added', () => {
        const investorPortfolioCollection: IInvestorPortfolio[] = [{ id: 123 }];
        expectedResult = service.addInvestorPortfolioToCollectionIfMissing(investorPortfolioCollection, undefined, null);
        expect(expectedResult).toEqual(investorPortfolioCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
