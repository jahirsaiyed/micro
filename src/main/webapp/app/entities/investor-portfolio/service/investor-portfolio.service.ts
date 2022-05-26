import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInvestorPortfolio, getInvestorPortfolioIdentifier } from '../investor-portfolio.model';

export type EntityResponseType = HttpResponse<IInvestorPortfolio>;
export type EntityArrayResponseType = HttpResponse<IInvestorPortfolio[]>;

@Injectable({ providedIn: 'root' })
export class InvestorPortfolioService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/investor-portfolios');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(investorPortfolio: IInvestorPortfolio): Observable<EntityResponseType> {
    return this.http.post<IInvestorPortfolio>(this.resourceUrl, investorPortfolio, { observe: 'response' });
  }

  update(investorPortfolio: IInvestorPortfolio): Observable<EntityResponseType> {
    return this.http.put<IInvestorPortfolio>(
      `${this.resourceUrl}/${getInvestorPortfolioIdentifier(investorPortfolio) as number}`,
      investorPortfolio,
      { observe: 'response' }
    );
  }

  partialUpdate(investorPortfolio: IInvestorPortfolio): Observable<EntityResponseType> {
    return this.http.patch<IInvestorPortfolio>(
      `${this.resourceUrl}/${getInvestorPortfolioIdentifier(investorPortfolio) as number}`,
      investorPortfolio,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInvestorPortfolio>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInvestorPortfolio[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addInvestorPortfolioToCollectionIfMissing(
    investorPortfolioCollection: IInvestorPortfolio[],
    ...investorPortfoliosToCheck: (IInvestorPortfolio | null | undefined)[]
  ): IInvestorPortfolio[] {
    const investorPortfolios: IInvestorPortfolio[] = investorPortfoliosToCheck.filter(isPresent);
    if (investorPortfolios.length > 0) {
      const investorPortfolioCollectionIdentifiers = investorPortfolioCollection.map(
        investorPortfolioItem => getInvestorPortfolioIdentifier(investorPortfolioItem)!
      );
      const investorPortfoliosToAdd = investorPortfolios.filter(investorPortfolioItem => {
        const investorPortfolioIdentifier = getInvestorPortfolioIdentifier(investorPortfolioItem);
        if (investorPortfolioIdentifier == null || investorPortfolioCollectionIdentifiers.includes(investorPortfolioIdentifier)) {
          return false;
        }
        investorPortfolioCollectionIdentifiers.push(investorPortfolioIdentifier);
        return true;
      });
      return [...investorPortfoliosToAdd, ...investorPortfolioCollection];
    }
    return investorPortfolioCollection;
  }
}
