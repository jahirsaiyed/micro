import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInvestor } from '../investor.model';
import { InvestorService } from '../service/investor.service';

@Component({
  templateUrl: './investor-delete-dialog.component.html',
})
export class InvestorDeleteDialogComponent {
  investor?: IInvestor;

  constructor(protected investorService: InvestorService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.investorService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
