import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IMasterDetails, MasterDetails } from '../master-details.model';

import { MasterDetailsService } from './master-details.service';

describe('MasterDetails Service', () => {
  let service: MasterDetailsService;
  let httpMock: HttpTestingController;
  let elemDefault: IMasterDetails;
  let expectedResult: IMasterDetails | IMasterDetails[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MasterDetailsService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      balance: 0,
      totalUnits: 0,
      aum: 0,
      createdOn: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          createdOn: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a MasterDetails', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          createdOn: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createdOn: currentDate,
        },
        returnedFromService
      );

      service.create(new MasterDetails()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MasterDetails', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          balance: 1,
          totalUnits: 1,
          aum: 1,
          createdOn: currentDate.format(DATE_FORMAT),
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

    it('should partial update a MasterDetails', () => {
      const patchObject = Object.assign(
        {
          balance: 1,
          aum: 1,
          createdOn: currentDate.format(DATE_FORMAT),
        },
        new MasterDetails()
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

    it('should return a list of MasterDetails', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          balance: 1,
          totalUnits: 1,
          aum: 1,
          createdOn: currentDate.format(DATE_FORMAT),
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

    it('should delete a MasterDetails', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMasterDetailsToCollectionIfMissing', () => {
      it('should add a MasterDetails to an empty array', () => {
        const masterDetails: IMasterDetails = { id: 123 };
        expectedResult = service.addMasterDetailsToCollectionIfMissing([], masterDetails);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(masterDetails);
      });

      it('should not add a MasterDetails to an array that contains it', () => {
        const masterDetails: IMasterDetails = { id: 123 };
        const masterDetailsCollection: IMasterDetails[] = [
          {
            ...masterDetails,
          },
          { id: 456 },
        ];
        expectedResult = service.addMasterDetailsToCollectionIfMissing(masterDetailsCollection, masterDetails);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MasterDetails to an array that doesn't contain it", () => {
        const masterDetails: IMasterDetails = { id: 123 };
        const masterDetailsCollection: IMasterDetails[] = [{ id: 456 }];
        expectedResult = service.addMasterDetailsToCollectionIfMissing(masterDetailsCollection, masterDetails);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(masterDetails);
      });

      it('should add only unique MasterDetails to an array', () => {
        const masterDetailsArray: IMasterDetails[] = [{ id: 123 }, { id: 456 }, { id: 14358 }];
        const masterDetailsCollection: IMasterDetails[] = [{ id: 123 }];
        expectedResult = service.addMasterDetailsToCollectionIfMissing(masterDetailsCollection, ...masterDetailsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const masterDetails: IMasterDetails = { id: 123 };
        const masterDetails2: IMasterDetails = { id: 456 };
        expectedResult = service.addMasterDetailsToCollectionIfMissing([], masterDetails, masterDetails2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(masterDetails);
        expect(expectedResult).toContain(masterDetails2);
      });

      it('should accept null and undefined values', () => {
        const masterDetails: IMasterDetails = { id: 123 };
        expectedResult = service.addMasterDetailsToCollectionIfMissing([], null, masterDetails, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(masterDetails);
      });

      it('should return initial array if no MasterDetails is added', () => {
        const masterDetailsCollection: IMasterDetails[] = [{ id: 123 }];
        expectedResult = service.addMasterDetailsToCollectionIfMissing(masterDetailsCollection, undefined, null);
        expect(expectedResult).toEqual(masterDetailsCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
