import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MasterDetailsService } from '../service/master-details.service';
import { IMasterDetails, MasterDetails } from '../master-details.model';

import { MasterDetailsUpdateComponent } from './master-details-update.component';

describe('MasterDetails Management Update Component', () => {
  let comp: MasterDetailsUpdateComponent;
  let fixture: ComponentFixture<MasterDetailsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let masterDetailsService: MasterDetailsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MasterDetailsUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(MasterDetailsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MasterDetailsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    masterDetailsService = TestBed.inject(MasterDetailsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const masterDetails: IMasterDetails = { id: 456 };

      activatedRoute.data = of({ masterDetails });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(masterDetails));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MasterDetails>>();
      const masterDetails = { id: 123 };
      jest.spyOn(masterDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ masterDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: masterDetails }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(masterDetailsService.update).toHaveBeenCalledWith(masterDetails);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MasterDetails>>();
      const masterDetails = new MasterDetails();
      jest.spyOn(masterDetailsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ masterDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: masterDetails }));
      saveSubject.complete();

      // THEN
      expect(masterDetailsService.create).toHaveBeenCalledWith(masterDetails);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MasterDetails>>();
      const masterDetails = { id: 123 };
      jest.spyOn(masterDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ masterDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(masterDetailsService.update).toHaveBeenCalledWith(masterDetails);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
