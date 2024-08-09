import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { DealerService } from '../service/dealer.service';
import { IDealer } from '../dealer.model';
import { DealerFormService } from './dealer-form.service';

import { DealerUpdateComponent } from './dealer-update.component';

describe('Dealer Management Update Component', () => {
  let comp: DealerUpdateComponent;
  let fixture: ComponentFixture<DealerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let dealerFormService: DealerFormService;
  let dealerService: DealerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DealerUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DealerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DealerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    dealerFormService = TestBed.inject(DealerFormService);
    dealerService = TestBed.inject(DealerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const dealer: IDealer = { id: 456 };

      activatedRoute.data = of({ dealer });
      comp.ngOnInit();

      expect(comp.dealer).toEqual(dealer);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDealer>>();
      const dealer = { id: 123 };
      jest.spyOn(dealerFormService, 'getDealer').mockReturnValue(dealer);
      jest.spyOn(dealerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dealer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dealer }));
      saveSubject.complete();

      // THEN
      expect(dealerFormService.getDealer).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(dealerService.update).toHaveBeenCalledWith(expect.objectContaining(dealer));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDealer>>();
      const dealer = { id: 123 };
      jest.spyOn(dealerFormService, 'getDealer').mockReturnValue({ id: null });
      jest.spyOn(dealerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dealer: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dealer }));
      saveSubject.complete();

      // THEN
      expect(dealerFormService.getDealer).toHaveBeenCalled();
      expect(dealerService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDealer>>();
      const dealer = { id: 123 };
      jest.spyOn(dealerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dealer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(dealerService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
