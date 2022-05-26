import { IInvestor } from 'app/entities/investor/investor.model';

export interface IInvestorPortfolio {
  id?: number;
  units?: number | null;
  investor?: IInvestor | null;
}

export class InvestorPortfolio implements IInvestorPortfolio {
  constructor(public id?: number, public units?: number | null, public investor?: IInvestor | null) {}
}

export function getInvestorPortfolioIdentifier(investorPortfolio: IInvestorPortfolio): number | undefined {
  return investorPortfolio.id;
}
