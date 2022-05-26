import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InvestorPortfolioComponent } from '../list/investor-portfolio.component';
import { InvestorPortfolioDetailComponent } from '../detail/investor-portfolio-detail.component';
import { InvestorPortfolioUpdateComponent } from '../update/investor-portfolio-update.component';
import { InvestorPortfolioRoutingResolveService } from './investor-portfolio-routing-resolve.service';

const investorPortfolioRoute: Routes = [
  {
    path: '',
    component: InvestorPortfolioComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InvestorPortfolioDetailComponent,
    resolve: {
      investorPortfolio: InvestorPortfolioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InvestorPortfolioUpdateComponent,
    resolve: {
      investorPortfolio: InvestorPortfolioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InvestorPortfolioUpdateComponent,
    resolve: {
      investorPortfolio: InvestorPortfolioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(investorPortfolioRoute)],
  exports: [RouterModule],
})
export class InvestorPortfolioRoutingModule {}
