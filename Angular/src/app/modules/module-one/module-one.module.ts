import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ComponentOneComponent } from './component-one/component-one.component';
import { ModuleOneComponent } from './module-one.component';

import {ModuleOneRouting} from './module-one.routing';
import { MaterialModule } from './material.module';
import { NgxMaterialTimepickerModule } from 'ngx-material-timepicker';


@NgModule({
  declarations: [ModuleOneComponent, ComponentOneComponent],
  imports: [
    CommonModule,
    MaterialModule,
    NgxMaterialTimepickerModule,
    ModuleOneRouting,
  ]
})
export class ModuleOneModule { }
