import { Component, OnDestroy, OnInit } from '@angular/core';
import { CommonService } from '../../../core/http/common.service';
import { User } from '../../../shared/models/user';
import { GraphService } from '../../../core/graph.service';
import { Room } from '../../../shared/models/room.model';
import { MatTableDataSource } from '@angular/material/table';
import { MsgType } from '../../../shared/models/msg-type';
import { Message } from '../../../shared/models/message.model';
import { MatDialog } from '@angular/material/dialog';
import { OccupyDialogComponent } from '../occupy-dialog/occupy-dialog.component';
import { Reservation } from '../../../shared/models/reservation.model';

@Component({
  selector: 'app-component-one',
  templateUrl: './component-one.component.html',
  styleUrls: ['./component-one.component.scss'],
})
export class ComponentOneComponent implements OnInit, OnDestroy {
  messageList = [];
  roomsLoaded = false;
  rooms: Room[];
  user: User;
  dataSource: MatTableDataSource<Room>;
  displayedColumns: String[] = [
    'name',
    'status',
    'reservedBy',
    'timeUntilFree',
    'action',
    'follow',
  ];

  intervalId;

  constructor(
    private service: CommonService,
    private graphService: GraphService,
    private dialog: MatDialog
  ) {}

  updateTimeUntilFree() {
    this.rooms.forEach((room) => {
      if (room.status.code !== 0) {
        let millisecondsLeft: number;
        if (room.status.code === 2) {
          millisecondsLeft =
            new Date(room.currentReservation.endTime).getTime() -
            new Date().getTime() -
            1000 * 60 * 60 * 2;
        } else if (room.status.code === 1) {
          millisecondsLeft =
            1000 * 60 * 5 +
            new Date(room.pendingDate).getTime() -
            new Date().getTime() -
            1000 * 60 * 60 * 2;
        }

        room.timeUntilFree = new Date(millisecondsLeft);
        if (millisecondsLeft < -7202235) {
          this.free(room, true);
        }
      }
    });
  }

  ngOnInit(): void {
    this.user = this.graphService.user;
    this.service.getRooms().subscribe(
      (response) => {
        this.rooms = response;
        this.updateTimeUntilFree();
        this.intervalId = setInterval(
          this.updateTimeUntilFree.bind(this),
          1000
        );

        this.dataSource = new MatTableDataSource<Room>(this.rooms);
        this.roomsLoaded = true;
      },
      (err) => {
        this.addBannerMsg(err.error.message, MsgType.ERROR);
      }
    );
  }

  free(row: Room, automatic?: boolean) {
    this.service.setRoomAsFree(row).subscribe(
      (response) => {
        const updatedRoom: Room = response;
        this.dataSource.data.splice(
          this.dataSource.data.indexOf(row),
          1,
          updatedRoom
        );
        this.dataSource.data = this.dataSource.data;
      },
      (err) => {
        if (automatic) {
          if (err.error.message.includes('was already free')) {
            this.service.getRoom(row.id).subscribe(
              (response) => {
                const updatedRoom: Room = response;
                this.dataSource.data.splice(
                  this.dataSource.data.indexOf(row),
                  1,
                  updatedRoom
                );
                this.dataSource.data = this.dataSource.data;
              },
              (errr) => {
                this.addBannerMsg(errr.error.message, MsgType.ERROR);
              }
            );
          }
        } else {
          this.addBannerMsg(err.error.message, MsgType.ERROR);
        }
      }
    );
  }

  pending(row: Room) {
    this.service.setRoomAsPending(row).subscribe(
      (response) => {
        const updatedRoom: Room = response;
        this.dataSource.data.splice(
          this.dataSource.data.indexOf(row),
          1,
          updatedRoom
        );
        updatedRoom.timeUntilFree = new Date(
          1000 * 60 * 5 +
            new Date(updatedRoom.pendingDate).getTime() -
            new Date().getTime() -
            1000 * 60 * 60 * 2
        );
        this.dataSource.data = this.dataSource.data;

        const dialogRef = this.dialog.open(OccupyDialogComponent, {
          data: {
            title: 'Occupy room ' + updatedRoom.name,
            message: 'Please fill in the following details:',
            user: this.user,
            confirmationRequired: true,
          },
        });
        dialogRef.afterClosed().subscribe((dialogResult) => {
          if (dialogResult) {
            const reservation: Reservation = dialogResult;
            this.service.setRoomAsOccupied(updatedRoom, reservation).subscribe(
              (ocupiedResponse) => {
                const updatedOccupiedRoom: Room = ocupiedResponse;
                this.dataSource.data.splice(
                  this.dataSource.data.indexOf(updatedRoom),
                  1,
                  updatedOccupiedRoom
                );
                updatedOccupiedRoom.timeUntilFree = new Date(
                  new Date(
                    updatedOccupiedRoom.currentReservation.endTime
                  ).getTime() -
                    new Date().getTime() -
                    1000 * 60 * 60 * 2
                );
                this.dataSource.data = this.dataSource.data;
              },
              (err) => {
                this.addBannerMsg(err.error.message, MsgType.ERROR);
                this.free(updatedRoom);
              }
            );
          } else {
            this.free(updatedRoom, true);
          }
        });
      },
      (err) => {
        this.addBannerMsg(err.error.message, MsgType.ERROR);
      }
    );
  }

  occupy(row: Room) {}

  follow(row: Room) {
    this.service.followRoom(row).subscribe(
      (response) => {
        row.following = true;
        this.dataSource.data = this.dataSource.data;
      },
      (err) => {
        this.addBannerMsg(err.error.message, MsgType.ERROR);
      }
    );
  }

  unfollow(row: Room) {
    this.service.unfollowRoom(row).subscribe(
      (response) => {
        row.following = false;
        this.dataSource.data = this.dataSource.data;
      },
      (err) => {
        this.addBannerMsg(err.error.message, MsgType.ERROR);
      }
    );
  }

  addBannerMsg(text: string, type: MsgType) {
    const errMsg = new Message();
    errMsg.content = text
      ? text
      : 'A server error occured. Please attempt again!';
    errMsg.type = type;
    errMsg.disabled = false;

    if (
      this.messageList.filter((item) => item.content === errMsg.content)
        .length === 0
    ) {
      this.messageList = this.messageList.concat([errMsg]);
    }
  }

  ngOnDestroy(): void {
    clearInterval(this.intervalId);
  }
}
