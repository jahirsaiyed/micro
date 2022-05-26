import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MasterDetailsComponent } from '../list/master-details.component';
import { MasterDetailsDetailComponent } from '../detail/master-details-detail.component';
import { MasterDetailsUpdateComponent } from '../update/master-details-update.component';
import { MasterDetailsRoutingResolveService } from './master-details-routing-resolve.service';

const masterDetailsRoute: Routes = [
  {
    path: '',
    component: MasterDetailsComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MasterDetailsDetailComponent,
    resolve: {
      masterDetails: MasterDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MasterDetailsUpdateComponent,
    resolve: {
      masterDetails: MasterDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MasterDetailsUpdateComponent,
    resolve: {
      masterDetails: MasterDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(masterDetailsRoute)],
  exports: [RouterModule],
})
export class MasterDetailsRoutingModule {}
