import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InvestorComponent } from './list/investor.component';
import { InvestorDetailComponent } from './detail/investor-detail.component';
import { InvestorUpdateComponent } from './update/investor-update.component';
import { InvestorDeleteDialogComponent } from './delete/investor-delete-dialog.component';
import { InvestorRoutingModule } from './route/investor-routing.module';

@NgModule({
  imports: [SharedModule, InvestorRoutingModule],
  declarations: [InvestorComponent, InvestorDetailComponent, InvestorUpdateComponent, InvestorDeleteDialogComponent],
  entryComponents: [InvestorDeleteDialogComponent],
})
export class InvestorModule {}
