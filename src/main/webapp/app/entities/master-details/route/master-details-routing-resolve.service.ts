import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMasterDetails, MasterDetails } from '../master-details.model';
import { MasterDetailsService } from '../service/master-details.service';

@Injectable({ providedIn: 'root' })
export class MasterDetailsRoutingResolveService implements Resolve<IMasterDetails> {
  constructor(protected service: MasterDetailsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMasterDetails> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((masterDetails: HttpResponse<MasterDetails>) => {
          if (masterDetails.body) {
            return of(masterDetails.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MasterDetails());
  }
}
