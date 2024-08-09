import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IDealer } from '../dealer.model';

@Component({
  standalone: true,
  selector: 'jhi-dealer-detail',
  templateUrl: './dealer-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class DealerDetailComponent {
  dealer = input<IDealer | null>(null);

  previousState(): void {
    window.history.back();
  }
}
