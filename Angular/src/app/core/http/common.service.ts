import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { PlatformLocation } from '@angular/common';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { StringResponse } from '../../shared/models/string-response';
import { Room } from '../../shared/models/room.model';
import { Reservation } from '../../shared/models/reservation.model';

@Injectable({
    providedIn: 'root'
})
export class CommonService {

    private _testApi = '/getAll';  // URL to web api
    private _reqOptionsArgs = { headers: new HttpHeaders().set('Content-Type', 'application/json') };

    constructor(private http: HttpClient, private pl: PlatformLocation) {
    }

    testJavaApi(): Observable<StringResponse> {
        const url = environment.apiResourceUri + this._testApi;
        console.log(url);
        return this.http.get<StringResponse>(url, this._reqOptionsArgs);
    }

    getRoom(roomId: number): Observable<Room> {
        const url = environment.apiResourceUri + '/rooms/' + roomId;

        return this.http.get<Room>(url);
    }

    getRooms(): Observable<Room[]> {
        const url = environment.apiResourceUri + '/rooms';

        return this.http.get<Room[]>(url);
    }

    setRoomAsFree(room: Room): Observable<Room> {
        const url = environment.apiResourceUri + '/rooms/' + room.id + '/free';

        return this.http.post<Room>(url, null);
    }

    setRoomAsPending(room: Room): Observable<Room> {
        const url = environment.apiResourceUri + '/rooms/' + room.id + '/pending';

        return this.http.post<Room>(url, null);
    }

    setRoomAsOccupied(room: Room, reservation: Reservation): Observable<Room> {
        const url = environment.apiResourceUri + '/rooms/' + room.id + '/occupy';

        return this.http.post<Room>(url, reservation);
    }

    followRoom(room: Room): Observable<String> {
        const url = environment.apiResourceUri + '/rooms/' + room.id + '/follow';

        return this.http.post<String>(url, null);
    }

    unfollowRoom(room: Room): Observable<String> {
        const url = environment.apiResourceUri + '/rooms/' + room.id + '/unfollow';

        return this.http.post<String>(url, null);
    }
}
