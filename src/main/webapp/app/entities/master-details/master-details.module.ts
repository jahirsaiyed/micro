import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MasterDetailsComponent } from './list/master-details.component';
import { MasterDetailsDetailComponent } from './detail/master-details-detail.component';
import { MasterDetailsUpdateComponent } from './update/master-details-update.component';
import { MasterDetailsDeleteDialogComponent } from './delete/master-details-delete-dialog.component';
import { MasterDetailsRoutingModule } from './route/master-details-routing.module';

@NgModule({
  imports: [SharedModule, MasterDetailsRoutingModule],
  declarations: [MasterDetailsComponent, MasterDetailsDetailComponent, MasterDetailsUpdateComponent, MasterDetailsDeleteDialogComponent],
  entryComponents: [MasterDetailsDeleteDialogComponent],
})
export class MasterDetailsModule {}
