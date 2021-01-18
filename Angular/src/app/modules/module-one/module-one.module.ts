import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ComponentOneComponent } from './component-one/component-one.component';
import { ModuleOneComponent } from './module-one.component';

import {ModuleOneRouting} from './module-one.routing';


@NgModule({
  declarations: [ModuleOneComponent, ComponentOneComponent],
  imports: [
    CommonModule,
    ModuleOneRouting,
  ]
})
export class ModuleOneModule { }
