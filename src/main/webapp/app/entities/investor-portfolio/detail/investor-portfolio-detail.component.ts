import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInvestorPortfolio } from '../investor-portfolio.model';

@Component({
  selector: 'jhi-investor-portfolio-detail',
  templateUrl: './investor-portfolio-detail.component.html',
})
export class InvestorPortfolioDetailComponent implements OnInit {
  investorPortfolio: IInvestorPortfolio | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ investorPortfolio }) => {
      this.investorPortfolio = investorPortfolio;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
