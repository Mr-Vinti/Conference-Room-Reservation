import { DatePipe } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Reservation } from '../../../shared/models/reservation.model';

@Component({
  selector: 'app-occupy-dialog',
  templateUrl: './occupy-dialog.component.html',
  styleUrls: ['./occupy-dialog.component.scss'],
})
export class OccupyDialogComponent implements OnInit {
  reservationForm: FormGroup;

  constructor(
    public dialogRef: MatDialogRef<OccupyDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private fb: FormBuilder,
    public datepipe: DatePipe
  ) {}

  ngOnInit(): void {
    this.reservationForm = this.fb.group({
      date: [new Date(), [Validators.required]],
      endTime: [
        this.datepipe.transform(new Date(), 'hh:mm a'),
        [Validators.required],
      ],
      name: [this.data.user.name, [Validators.required]],
      email: [this.data.user.displayName, [Validators.required]],
      reason: ['', [Validators.required]],
    });
  }

  onConfirm(): void {
    if (this.reservationForm.valid === false) {
      this.reservationForm.markAllAsTouched();
      return;
    }

    const endTime: Date = new Date(
      this.datepipe.transform(
        this.reservationForm.controls.date.value,
        'MM/dd/yyyy'
      ) +
        ' ' +
        this.reservationForm.controls.endTime.value
    );

    const reservation: Reservation = {
      date: this.reservationForm.controls.date.value,
      startTime: this.reservationForm.controls.date.value,
      endTime: endTime,
      name: this.reservationForm.controls.name.value,
      email: this.reservationForm.controls.email.value,
      reason: this.reservationForm.controls.reason.value,
    };
    this.dialogRef.close(reservation);
  }

  onDismiss(): void {
    this.dialogRef.close(null);
  }
}
