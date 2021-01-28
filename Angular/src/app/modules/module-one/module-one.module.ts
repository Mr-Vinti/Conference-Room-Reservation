import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ComponentOneComponent } from './component-one/component-one.component';
import { ModuleOneComponent } from './module-one.component';

import {ModuleOneRouting} from './module-one.routing';
import { MaterialModule } from './material.module';
import { NgxMaterialTimepickerModule } from 'ngx-material-timepicker';
import { MsgBannerComponent } from '../../shared/components/msg-banner/msg-banner.component';
import { SharedModule } from '../../shared/shared.module';
import { OccupyDialogComponent } from './occupy-dialog/occupy-dialog.component';
import { InfoDialogComponent } from './info-dialog/info-dialog.component';


@NgModule({
  declarations: [ModuleOneComponent, ComponentOneComponent, OccupyDialogComponent, InfoDialogComponent],
  imports: [
    CommonModule,
    MaterialModule,
    NgxMaterialTimepickerModule,
    ModuleOneRouting,
    SharedModule
  ], providers: [MsgBannerComponent]
})
export class ModuleOneModule { }
