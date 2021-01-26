import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CommonService } from './http/common.service';
import { GraphService } from './graph.service';
import { GroupGuardService } from './group-guard.service';

// Services

// components


@NgModule({
  declarations: [],
  imports: [
    CommonModule
  ],
  exports: [CommonModule],
  providers: [CommonService, GraphService, GroupGuardService]
})
export class CoreModule { }
