import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InvestorPortfolioComponent } from './list/investor-portfolio.component';
import { InvestorPortfolioDetailComponent } from './detail/investor-portfolio-detail.component';
import { InvestorPortfolioUpdateComponent } from './update/investor-portfolio-update.component';
import { InvestorPortfolioDeleteDialogComponent } from './delete/investor-portfolio-delete-dialog.component';
import { InvestorPortfolioRoutingModule } from './route/investor-portfolio-routing.module';

@NgModule({
  imports: [SharedModule, InvestorPortfolioRoutingModule],
  declarations: [
    InvestorPortfolioComponent,
    InvestorPortfolioDetailComponent,
    InvestorPortfolioUpdateComponent,
    InvestorPortfolioDeleteDialogComponent,
  ],
  entryComponents: [InvestorPortfolioDeleteDialogComponent],
})
export class InvestorPortfolioModule {}
