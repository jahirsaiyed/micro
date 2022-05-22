import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInvestor } from '../investor.model';

@Component({
  selector: 'jhi-investor-detail',
  templateUrl: './investor-detail.component.html',
})
export class InvestorDetailComponent implements OnInit {
  investor: IInvestor | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ investor }) => {
      this.investor = investor;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
