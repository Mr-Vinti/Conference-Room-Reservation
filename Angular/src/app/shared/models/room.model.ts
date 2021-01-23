import { Reservation } from './reservation.model';
import { Status } from './status.model';

export interface Room {
  name: string;
  status: Status;
  description: string;
  currentReservation: Reservation;
  pastFiveReservations: Reservation[];
  following: boolean;
}
