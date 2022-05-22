import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InvestorComponent } from '../list/investor.component';
import { InvestorDetailComponent } from '../detail/investor-detail.component';
import { InvestorUpdateComponent } from '../update/investor-update.component';
import { InvestorRoutingResolveService } from './investor-routing-resolve.service';

const investorRoute: Routes = [
  {
    path: '',
    component: InvestorComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InvestorDetailComponent,
    resolve: {
      investor: InvestorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InvestorUpdateComponent,
    resolve: {
      investor: InvestorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InvestorUpdateComponent,
    resolve: {
      investor: InvestorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(investorRoute)],
  exports: [RouterModule],
})
export class InvestorRoutingModule {}
