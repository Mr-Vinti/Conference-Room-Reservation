import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConfirmDialogComponent } from './components/confirm-dialog/confirm-dialog.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MsgBannerComponent } from './components/msg-banner/msg-banner.component';
import { MatCardModule } from '@angular/material/card';

@NgModule({
  declarations: [ConfirmDialogComponent, MsgBannerComponent],
  imports: [CommonModule, MatCardModule, MatDialogModule, MatButtonModule, FlexLayoutModule],
  exports: [CommonModule, ConfirmDialogComponent, MsgBannerComponent],
})
export class SharedModule {}
