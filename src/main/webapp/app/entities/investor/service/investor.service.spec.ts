import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Gender } from 'app/entities/enumerations/gender.model';
import { IInvestor, Investor } from '../investor.model';

import { InvestorService } from './investor.service';

describe('Investor Service', () => {
  let service: InvestorService;
  let httpMock: HttpTestingController;
  let elemDefault: IInvestor;
  let expectedResult: IInvestor | IInvestor[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InvestorService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      description: 'AAAAAAA',
      email: 'AAAAAAA',
      gender: Gender.MALE,
      phone: 'AAAAAAA',
      addressLine1: 'AAAAAAA',
      addressLine2: 'AAAAAAA',
      city: 'AAAAAAA',
      country: 'AAAAAAA',
      createdOn: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          createdOn: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Investor', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          createdOn: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createdOn: currentDate,
        },
        returnedFromService
      );

      service.create(new Investor()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Investor', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          description: 'BBBBBB',
          email: 'BBBBBB',
          gender: 'BBBBBB',
          phone: 'BBBBBB',
          addressLine1: 'BBBBBB',
          addressLine2: 'BBBBBB',
          city: 'BBBBBB',
          country: 'BBBBBB',
          createdOn: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createdOn: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Investor', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          description: 'BBBBBB',
          email: 'BBBBBB',
          gender: 'BBBBBB',
          phone: 'BBBBBB',
          addressLine1: 'BBBBBB',
          city: 'BBBBBB',
        },
        new Investor()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          createdOn: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Investor', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          description: 'BBBBBB',
          email: 'BBBBBB',
          gender: 'BBBBBB',
          phone: 'BBBBBB',
          addressLine1: 'BBBBBB',
          addressLine2: 'BBBBBB',
          city: 'BBBBBB',
          country: 'BBBBBB',
          createdOn: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createdOn: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Investor', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addInvestorToCollectionIfMissing', () => {
      it('should add a Investor to an empty array', () => {
        const investor: IInvestor = { id: 123 };
        expectedResult = service.addInvestorToCollectionIfMissing([], investor);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(investor);
      });

      it('should not add a Investor to an array that contains it', () => {
        const investor: IInvestor = { id: 123 };
        const investorCollection: IInvestor[] = [
          {
            ...investor,
          },
          { id: 456 },
        ];
        expectedResult = service.addInvestorToCollectionIfMissing(investorCollection, investor);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Investor to an array that doesn't contain it", () => {
        const investor: IInvestor = { id: 123 };
        const investorCollection: IInvestor[] = [{ id: 456 }];
        expectedResult = service.addInvestorToCollectionIfMissing(investorCollection, investor);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(investor);
      });

      it('should add only unique Investor to an array', () => {
        const investorArray: IInvestor[] = [{ id: 123 }, { id: 456 }, { id: 9782 }];
        const investorCollection: IInvestor[] = [{ id: 123 }];
        expectedResult = service.addInvestorToCollectionIfMissing(investorCollection, ...investorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const investor: IInvestor = { id: 123 };
        const investor2: IInvestor = { id: 456 };
        expectedResult = service.addInvestorToCollectionIfMissing([], investor, investor2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(investor);
        expect(expectedResult).toContain(investor2);
      });

      it('should accept null and undefined values', () => {
        const investor: IInvestor = { id: 123 };
        expectedResult = service.addInvestorToCollectionIfMissing([], null, investor, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(investor);
      });

      it('should return initial array if no Investor is added', () => {
        const investorCollection: IInvestor[] = [{ id: 123 }];
        expectedResult = service.addInvestorToCollectionIfMissing(investorCollection, undefined, null);
        expect(expectedResult).toEqual(investorCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
