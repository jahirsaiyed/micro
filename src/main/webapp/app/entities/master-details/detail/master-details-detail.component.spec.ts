import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MasterDetailsDetailComponent } from './master-details-detail.component';

describe('MasterDetails Management Detail Component', () => {
  let comp: MasterDetailsDetailComponent;
  let fixture: ComponentFixture<MasterDetailsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MasterDetailsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ masterDetails: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MasterDetailsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MasterDetailsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load masterDetails on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.masterDetails).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
