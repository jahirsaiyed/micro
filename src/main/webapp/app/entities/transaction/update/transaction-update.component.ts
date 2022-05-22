import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ITransaction, Transaction } from '../transaction.model';
import { TransactionService } from '../service/transaction.service';
import { IInvestor } from 'app/entities/investor/investor.model';
import { InvestorService } from 'app/entities/investor/service/investor.service';
import { TransactionType } from 'app/entities/enumerations/transaction-type.model';

@Component({
  selector: 'jhi-transaction-update',
  templateUrl: './transaction-update.component.html',
})
export class TransactionUpdateComponent implements OnInit {
  isSaving = false;
  transactionTypeValues = Object.keys(TransactionType);

  usersCollection: IInvestor[] = [];

  editForm = this.fb.group({
    id: [],
    amount: [null, [Validators.required]],
    createdOn: [],
    type: [],
    user: [null, Validators.required],
  });

  constructor(
    protected transactionService: TransactionService,
    protected investorService: InvestorService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transaction }) => {
      if (transaction.id === undefined) {
        const today = dayjs().startOf('day');
        transaction.createdOn = today;
      }

      this.updateForm(transaction);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const transaction = this.createFromForm();
    if (transaction.id !== undefined) {
      this.subscribeToSaveResponse(this.transactionService.update(transaction));
    } else {
      this.subscribeToSaveResponse(this.transactionService.create(transaction));
    }
  }

  trackInvestorById(_index: number, item: IInvestor): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransaction>>): void {
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

  protected updateForm(transaction: ITransaction): void {
    this.editForm.patchValue({
      id: transaction.id,
      amount: transaction.amount,
      createdOn: transaction.createdOn ? transaction.createdOn.format(DATE_TIME_FORMAT) : null,
      type: transaction.type,
      user: transaction.user,
    });

    this.usersCollection = this.investorService.addInvestorToCollectionIfMissing(this.usersCollection, transaction.user);
  }

  protected loadRelationshipsOptions(): void {
    this.investorService
      .query({ filter: 'transaction-is-null' })
      .pipe(map((res: HttpResponse<IInvestor[]>) => res.body ?? []))
      .pipe(
        map((investors: IInvestor[]) => this.investorService.addInvestorToCollectionIfMissing(investors, this.editForm.get('user')!.value))
      )
      .subscribe((investors: IInvestor[]) => (this.usersCollection = investors));
  }

  protected createFromForm(): ITransaction {
    return {
      ...new Transaction(),
      id: this.editForm.get(['id'])!.value,
      amount: this.editForm.get(['amount'])!.value,
      createdOn: this.editForm.get(['createdOn'])!.value ? dayjs(this.editForm.get(['createdOn'])!.value, DATE_TIME_FORMAT) : undefined,
      type: this.editForm.get(['type'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
