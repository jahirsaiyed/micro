import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInvestorPortfolio, InvestorPortfolio } from '../investor-portfolio.model';
import { InvestorPortfolioService } from '../service/investor-portfolio.service';

@Injectable({ providedIn: 'root' })
export class InvestorPortfolioRoutingResolveService implements Resolve<IInvestorPortfolio> {
  constructor(protected service: InvestorPortfolioService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInvestorPortfolio> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((investorPortfolio: HttpResponse<InvestorPortfolio>) => {
          if (investorPortfolio.body) {
            return of(investorPortfolio.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new InvestorPortfolio());
  }
}
