import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { PlatformLocation } from '@angular/common';
import { Observable, throwError } from 'rxjs';
import { map, debounceTime, catchError, tap } from 'rxjs/operators';
import { environment } from '../../../../environments/environment';
import { StringResponse } from '../../../shared/models/string-response';
import { CoreModule } from '../../core.module';
import * as config from '../../../modules/app-config.json';

@Injectable({
    providedIn: 'root'
})
export class TestserviceService {

    private _testApi = '/getAll';  // URL to web api
    private _reqOptionsArgs = { headers: new HttpHeaders().set('Content-Type', 'application/json') };

    constructor(private http: HttpClient, private pl: PlatformLocation) {
    }

    testJavaApi(): Observable<StringResponse> {
        let url = environment.apiResourceUri + this._testApi;
        console.log(url);
        return this.http.get<StringResponse>(url, this._reqOptionsArgs);
    }
}
