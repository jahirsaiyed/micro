import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMasterDetails } from '../master-details.model';

@Component({
  selector: 'jhi-master-details-detail',
  templateUrl: './master-details-detail.component.html',
})
export class MasterDetailsDetailComponent implements OnInit {
  masterDetails: IMasterDetails | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ masterDetails }) => {
      this.masterDetails = masterDetails;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
