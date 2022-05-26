import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInvestorPortfolio } from '../investor-portfolio.model';
import { InvestorPortfolioService } from '../service/investor-portfolio.service';

@Component({
  templateUrl: './investor-portfolio-delete-dialog.component.html',
})
export class InvestorPortfolioDeleteDialogComponent {
  investorPortfolio?: IInvestorPortfolio;

  constructor(protected investorPortfolioService: InvestorPortfolioService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.investorPortfolioService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
