import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDealer } from '../dealer.model';
import { DealerService } from '../service/dealer.service';
import { DealerFormService, DealerFormGroup } from './dealer-form.service';

@Component({
  standalone: true,
  selector: 'jhi-dealer-update',
  templateUrl: './dealer-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DealerUpdateComponent implements OnInit {
  isSaving = false;
  dealer: IDealer | null = null;

  protected dealerService = inject(DealerService);
  protected dealerFormService = inject(DealerFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DealerFormGroup = this.dealerFormService.createDealerFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dealer }) => {
      this.dealer = dealer;
      if (dealer) {
        this.updateForm(dealer);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dealer = this.dealerFormService.getDealer(this.editForm);
    if (dealer.id !== null) {
      this.subscribeToSaveResponse(this.dealerService.update(dealer));
    } else {
      this.subscribeToSaveResponse(this.dealerService.create(dealer));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDealer>>): void {
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

  protected updateForm(dealer: IDealer): void {
    this.dealer = dealer;
    this.dealerFormService.resetForm(this.editForm, dealer);
  }
}
