import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IMasterDetails, MasterDetails } from '../master-details.model';
import { MasterDetailsService } from '../service/master-details.service';

@Component({
  selector: 'jhi-master-details-update',
  templateUrl: './master-details-update.component.html',
})
export class MasterDetailsUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    balance: [],
    totalUnits: [],
    aum: [],
    createdOn: [],
  });

  constructor(protected masterDetailsService: MasterDetailsService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ masterDetails }) => {
      this.updateForm(masterDetails);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const masterDetails = this.createFromForm();
    if (masterDetails.id !== undefined) {
      this.subscribeToSaveResponse(this.masterDetailsService.update(masterDetails));
    } else {
      this.subscribeToSaveResponse(this.masterDetailsService.create(masterDetails));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMasterDetails>>): void {
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

  protected updateForm(masterDetails: IMasterDetails): void {
    this.editForm.patchValue({
      id: masterDetails.id,
      balance: masterDetails.balance,
      totalUnits: masterDetails.totalUnits,
      aum: masterDetails.aum,
      createdOn: masterDetails.createdOn,
    });
  }

  protected createFromForm(): IMasterDetails {
    return {
      ...new MasterDetails(),
      id: this.editForm.get(['id'])!.value,
      balance: this.editForm.get(['balance'])!.value,
      totalUnits: this.editForm.get(['totalUnits'])!.value,
      aum: this.editForm.get(['aum'])!.value,
      createdOn: this.editForm.get(['createdOn'])!.value,
    };
  }
}
