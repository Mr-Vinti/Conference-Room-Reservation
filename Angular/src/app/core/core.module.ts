import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TestserviceService } from './http/testrest/testservice.service';
import { GraphService } from './graph.service';
import { GroupGuardService } from './group-guard.service';

//Services

//components


@NgModule({
  declarations: [],
  imports: [
    CommonModule
  ],
  exports: [CommonModule],
  providers:[TestserviceService, GraphService, GroupGuardService]
})
export class CoreModule { }