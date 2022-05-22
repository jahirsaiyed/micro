import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInvestor, Investor } from '../investor.model';
import { InvestorService } from '../service/investor.service';

@Injectable({ providedIn: 'root' })
export class InvestorRoutingResolveService implements Resolve<IInvestor> {
  constructor(protected service: InvestorService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInvestor> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((investor: HttpResponse<Investor>) => {
          if (investor.body) {
            return of(investor.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Investor());
  }
}
