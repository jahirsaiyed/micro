import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMasterDetails, getMasterDetailsIdentifier } from '../master-details.model';

export type EntityResponseType = HttpResponse<IMasterDetails>;
export type EntityArrayResponseType = HttpResponse<IMasterDetails[]>;

@Injectable({ providedIn: 'root' })
export class MasterDetailsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/master-details');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(masterDetails: IMasterDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(masterDetails);
    return this.http
      .post<IMasterDetails>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(masterDetails: IMasterDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(masterDetails);
    return this.http
      .put<IMasterDetails>(`${this.resourceUrl}/${getMasterDetailsIdentifier(masterDetails) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(masterDetails: IMasterDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(masterDetails);
    return this.http
      .patch<IMasterDetails>(`${this.resourceUrl}/${getMasterDetailsIdentifier(masterDetails) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMasterDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMasterDetails[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMasterDetailsToCollectionIfMissing(
    masterDetailsCollection: IMasterDetails[],
    ...masterDetailsToCheck: (IMasterDetails | null | undefined)[]
  ): IMasterDetails[] {
    const masterDetails: IMasterDetails[] = masterDetailsToCheck.filter(isPresent);
    if (masterDetails.length > 0) {
      const masterDetailsCollectionIdentifiers = masterDetailsCollection.map(
        masterDetailsItem => getMasterDetailsIdentifier(masterDetailsItem)!
      );
      const masterDetailsToAdd = masterDetails.filter(masterDetailsItem => {
        const masterDetailsIdentifier = getMasterDetailsIdentifier(masterDetailsItem);
        if (masterDetailsIdentifier == null || masterDetailsCollectionIdentifiers.includes(masterDetailsIdentifier)) {
          return false;
        }
        masterDetailsCollectionIdentifiers.push(masterDetailsIdentifier);
        return true;
      });
      return [...masterDetailsToAdd, ...masterDetailsCollection];
    }
    return masterDetailsCollection;
  }

  protected convertDateFromClient(masterDetails: IMasterDetails): IMasterDetails {
    return Object.assign({}, masterDetails, {
      createdOn: masterDetails.createdOn?.isValid() ? masterDetails.createdOn.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((masterDetails: IMasterDetails) => {
        masterDetails.createdOn = masterDetails.createdOn ? dayjs(masterDetails.createdOn) : undefined;
      });
    }
    return res;
  }
}
