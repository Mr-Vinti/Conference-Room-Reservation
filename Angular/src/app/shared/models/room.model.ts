import { Reservation } from './reservation.model';
import { Status } from './status.model';

export interface Room {
  id: number;
  name: string;
  status: Status;
  description: string;
  currentReservation: Reservation;
  pastFiveReservations: Reservation[];
  pendingBy: string;
  pendingDate: Date;
  following: boolean;
  timeUntilFree?: Date;
}
