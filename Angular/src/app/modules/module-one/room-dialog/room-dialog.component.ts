import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Room } from '../../../shared/models/room.model';

@Component({
  selector: 'app-room-dialog',
  templateUrl: './room-dialog.component.html',
  styleUrls: ['./room-dialog.component.scss'],
})
export class RoomDialogComponent implements OnInit {
  roomForm: FormGroup;
  room: Room = null;

  constructor(
    public dialogRef: MatDialogRef<RoomDialogComponent>,
    private fb: FormBuilder,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}

  ngOnInit(): void {
    this.room = this.data.room;
    if (!this.room) {
      this.roomForm = this.fb.group({
        name: ['', Validators.required],
        description: ['', Validators.required],
      });
    } else {
      this.roomForm = this.fb.group({
        name: [this.room.name, Validators.required],
        description: [this.room.description, Validators.required],
      });
    }
  }

  onConfirm(): void {
    if (this.roomForm.valid === false) {
      this.roomForm.markAllAsTouched();
      return;
    }
    this.dialogRef.close({
      name: this.roomForm.controls.name.value,
      description: this.roomForm.controls.description.value,
    });
  }

  onDismiss(): void {
    this.dialogRef.close(null);
  }
}
