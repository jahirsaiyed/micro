import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IInvestor, Investor } from '../investor.model';
import { InvestorService } from '../service/investor.service';
import { IInvestorPortfolio } from 'app/entities/investor-portfolio/investor-portfolio.model';
import { InvestorPortfolioService } from 'app/entities/investor-portfolio/service/investor-portfolio.service';
import { Gender } from 'app/entities/enumerations/gender.model';

@Component({
  selector: 'jhi-investor-update',
  templateUrl: './investor-update.component.html',
})
export class InvestorUpdateComponent implements OnInit {
  isSaving = false;
  genderValues = Object.keys(Gender);

  portfoliosCollection: IInvestorPortfolio[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [],
    email: [null, [Validators.required]],
    gender: [null, [Validators.required]],
    phone: [null, [Validators.required]],
    addressLine1: [null, [Validators.required]],
    addressLine2: [],
    city: [null, [Validators.required]],
    country: [null, [Validators.required]],
    createdOn: [],
    portfolio: [],
  });

  constructor(
    protected investorService: InvestorService,
    protected investorPortfolioService: InvestorPortfolioService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ investor }) => {
      if (investor.id === undefined) {
        const today = dayjs().startOf('day');
        investor.createdOn = today;
      }

      this.updateForm(investor);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const investor = this.createFromForm();
    if (investor.id !== undefined) {
      this.subscribeToSaveResponse(this.investorService.update(investor));
    } else {
      this.subscribeToSaveResponse(this.investorService.create(investor));
    }
  }

  trackInvestorPortfolioById(_index: number, item: IInvestorPortfolio): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInvestor>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(investor: IInvestor): void {
    this.editForm.patchValue({
      id: investor.id,
      name: investor.name,
      description: investor.description,
      email: investor.email,
      gender: investor.gender,
      phone: investor.phone,
      addressLine1: investor.addressLine1,
      addressLine2: investor.addressLine2,
      city: investor.city,
      country: investor.country,
      createdOn: investor.createdOn ? investor.createdOn.format(DATE_TIME_FORMAT) : null,
      portfolio: investor.portfolio,
    });

    this.portfoliosCollection = this.investorPortfolioService.addInvestorPortfolioToCollectionIfMissing(
      this.portfoliosCollection,
      investor.portfolio
    );
  }

  protected loadRelationshipsOptions(): void {
    this.investorPortfolioService
      .query({ filter: 'investor-is-null' })
      .pipe(map((res: HttpResponse<IInvestorPortfolio[]>) => res.body ?? []))
      .pipe(
        map((investorPortfolios: IInvestorPortfolio[]) =>
          this.investorPortfolioService.addInvestorPortfolioToCollectionIfMissing(investorPortfolios, this.editForm.get('portfolio')!.value)
        )
      )
      .subscribe((investorPortfolios: IInvestorPortfolio[]) => (this.portfoliosCollection = investorPortfolios));
  }

  protected createFromForm(): IInvestor {
    return {
      ...new Investor(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      email: this.editForm.get(['email'])!.value,
      gender: this.editForm.get(['gender'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      addressLine1: this.editForm.get(['addressLine1'])!.value,
      addressLine2: this.editForm.get(['addressLine2'])!.value,
      city: this.editForm.get(['city'])!.value,
      country: this.editForm.get(['country'])!.value,
      createdOn: this.editForm.get(['createdOn'])!.value ? dayjs(this.editForm.get(['createdOn'])!.value, DATE_TIME_FORMAT) : undefined,
      portfolio: this.editForm.get(['portfolio'])!.value,
    };
  }
}
