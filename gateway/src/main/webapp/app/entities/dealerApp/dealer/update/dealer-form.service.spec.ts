import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../dealer.test-samples';

import { DealerFormService } from './dealer-form.service';

describe('Dealer Form Service', () => {
  let service: DealerFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DealerFormService);
  });

  describe('Service methods', () => {
    describe('createDealerFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDealerFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            address: expect.any(Object),
          }),
        );
      });

      it('passing IDealer should create a new form with FormGroup', () => {
        const formGroup = service.createDealerFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            address: expect.any(Object),
          }),
        );
      });
    });

    describe('getDealer', () => {
      it('should return NewDealer for default Dealer initial value', () => {
        const formGroup = service.createDealerFormGroup(sampleWithNewData);

        const dealer = service.getDealer(formGroup) as any;

        expect(dealer).toMatchObject(sampleWithNewData);
      });

      it('should return NewDealer for empty Dealer initial value', () => {
        const formGroup = service.createDealerFormGroup();

        const dealer = service.getDealer(formGroup) as any;

        expect(dealer).toMatchObject({});
      });

      it('should return IDealer', () => {
        const formGroup = service.createDealerFormGroup(sampleWithRequiredData);

        const dealer = service.getDealer(formGroup) as any;

        expect(dealer).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDealer should not enable id FormControl', () => {
        const formGroup = service.createDealerFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDealer should disable id FormControl', () => {
        const formGroup = service.createDealerFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
