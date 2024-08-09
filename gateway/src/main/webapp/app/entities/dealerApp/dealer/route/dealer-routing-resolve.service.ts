import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDealer } from '../dealer.model';
import { DealerService } from '../service/dealer.service';

const dealerResolve = (route: ActivatedRouteSnapshot): Observable<null | IDealer> => {
  const id = route.params['id'];
  if (id) {
    return inject(DealerService)
      .find(id)
      .pipe(
        mergeMap((dealer: HttpResponse<IDealer>) => {
          if (dealer.body) {
            return of(dealer.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default dealerResolve;
