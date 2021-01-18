import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, from } from 'rxjs';
import { mergeMap, tap } from 'rxjs/operators';
import { Location } from '@angular/common';
import { MsalService, BroadcastService } from '@azure/msal-angular';
import { AuthResponse, ServerHashParamKeys } from 'msal';
import 'rxjs/add/observable/fromPromise';
import { MSALError } from '../../shared/models/MSALError';
import { environment } from '../../../environments/environment';

@Injectable()
export class CustomHttpInterceptor implements HttpInterceptor {
  constructor(private auth: MsalService, private broadcastService: BroadcastService) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const _this = this;
    const scopes = this.auth.getScopesForEndpoint(window.location.origin + environment.baseHref);
    console.log('Url: ' + window.location.origin + environment.baseHref + ' maps to scopes: ' + scopes);

    if (scopes === null) {
      return next.handle(req);
    }
    let tokenStored: any;

    return from(
      this.auth.acquireTokenSilent({ scopes })
        .then((response: AuthResponse) => {
          tokenStored = response.tokenType === ServerHashParamKeys.ID_TOKEN ? response.idToken.rawIdToken : response.accessToken;
          if (tokenStored) {
            return req.clone({
              setHeaders: {
                Authorization: 'Bearer ' + tokenStored,
              }
            });
          }
        })
    )
      .pipe(
        mergeMap(nextReq => next.handle(nextReq)),
        tap(
          event => { }, // tslint:disable-line
          async err => {
            if (err instanceof HttpErrorResponse && err.status === 401) {
              const scopes = _this.auth.getScopesForEndpoint(window.location.origin + environment.baseHref);
              const tokenStored = await _this.auth.acquireTokenSilent({ scopes });
              if (tokenStored && tokenStored.accessToken) {
                _this.auth.clearCacheForScope(tokenStored.accessToken);
              }
              const msalError = new MSALError(JSON.stringify(err), '', JSON.stringify(scopes));
              _this.broadcastService.broadcast('msal:notAuthorized', msalError);
            }
          }
        )
      );
  }
}
