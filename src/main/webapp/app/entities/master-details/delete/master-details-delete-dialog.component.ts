import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMasterDetails } from '../master-details.model';
import { MasterDetailsService } from '../service/master-details.service';

@Component({
  templateUrl: './master-details-delete-dialog.component.html',
})
export class MasterDetailsDeleteDialogComponent {
  masterDetails?: IMasterDetails;

  constructor(protected masterDetailsService: MasterDetailsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.masterDetailsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
