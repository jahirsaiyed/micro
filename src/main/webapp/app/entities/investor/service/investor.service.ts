import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInvestor, getInvestorIdentifier } from '../investor.model';

export type EntityResponseType = HttpResponse<IInvestor>;
export type EntityArrayResponseType = HttpResponse<IInvestor[]>;

@Injectable({ providedIn: 'root' })
export class InvestorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/investors');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(investor: IInvestor): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(investor);
    return this.http
      .post<IInvestor>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(investor: IInvestor): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(investor);
    return this.http
      .put<IInvestor>(`${this.resourceUrl}/${getInvestorIdentifier(investor) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(investor: IInvestor): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(investor);
    return this.http
      .patch<IInvestor>(`${this.resourceUrl}/${getInvestorIdentifier(investor) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IInvestor>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IInvestor[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addInvestorToCollectionIfMissing(investorCollection: IInvestor[], ...investorsToCheck: (IInvestor | null | undefined)[]): IInvestor[] {
    const investors: IInvestor[] = investorsToCheck.filter(isPresent);
    if (investors.length > 0) {
      const investorCollectionIdentifiers = investorCollection.map(investorItem => getInvestorIdentifier(investorItem)!);
      const investorsToAdd = investors.filter(investorItem => {
        const investorIdentifier = getInvestorIdentifier(investorItem);
        if (investorIdentifier == null || investorCollectionIdentifiers.includes(investorIdentifier)) {
          return false;
        }
        investorCollectionIdentifiers.push(investorIdentifier);
        return true;
      });
      return [...investorsToAdd, ...investorCollection];
    }
    return investorCollection;
  }

  protected convertDateFromClient(investor: IInvestor): IInvestor {
    return Object.assign({}, investor, {
      createdOn: investor.createdOn?.isValid() ? investor.createdOn.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdOn = res.body.createdOn ? dayjs(res.body.createdOn) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((investor: IInvestor) => {
        investor.createdOn = investor.createdOn ? dayjs(investor.createdOn) : undefined;
      });
    }
    return res;
  }
}
