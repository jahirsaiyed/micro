import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'investor',
        data: { pageTitle: 'microApp.investor.home.title' },
        loadChildren: () => import('./investor/investor.module').then(m => m.InvestorModule),
      },
      {
        path: 'transaction',
        data: { pageTitle: 'microApp.transaction.home.title' },
        loadChildren: () => import('./transaction/transaction.module').then(m => m.TransactionModule),
      },
      {
        path: 'report',
        data: { pageTitle: 'microApp.report.home.title' },
        loadChildren: () => import('./report/report.module').then(m => m.ReportModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
