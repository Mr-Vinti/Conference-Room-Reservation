/**
 *    Copyright 2016 Sven Loesekann
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
       http://www.apache.org/licenses/LICENSE-2.0
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { BroadcastService, MsalService } from '@azure/msal-angular';
import { Subscriber, Subscription, Subject } from 'rxjs';
import { User } from '../shared/models/user';
import { LoadingService } from '../core/http/loading-service';
import {
  Router,
  Event,
  NavigationStart,
  NavigationEnd,
  NavigationCancel,
  NavigationError,
} from '@angular/router';
import { Logger, CryptoUtils } from 'msal';
import { GraphService } from '../core/graph.service';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../shared/components/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit, OnDestroy {
  title = 'Template app!';
  isIframe: boolean;
  private loginSubscription: Subscription;
  private tokenSubscription: Subscription;
  loggedIn: boolean;
  user: User = {
    displayName: '',
    groupIDs: [],
    name: ''
  };
  isLoading: Subject<boolean> = this.loadingService.isLoading;

  constructor(
    private broadcastService: BroadcastService,
    private authService: MsalService,
    private loadingService: LoadingService,
    private cdref: ChangeDetectorRef,
    private router: Router,
    private graphService: GraphService,
    private dialog: MatDialog
  ) {
    this.isIframe = window !== window.parent && !window.opener;

    this.router.events.subscribe((event: Event) => {
      switch (true) {
        case event instanceof NavigationStart: {
          this.loadingService.show();
          break;
        }

        case event instanceof NavigationEnd:
        case event instanceof NavigationCancel:
        case event instanceof NavigationError: {
          this.loadingService.hide();
          break;
        }
        default: {
          break;
        }
      }
    });
  }

  ngOnInit(): void {
    this.isIframe = window !== window.parent && !window.opener;

    this.checkoutAccount();

    this.loginSubscription = this.broadcastService.subscribe(
      'msal:loginSuccess',
      (payload) => {
        console.log('login success ' + JSON.stringify(payload));
        this.graphService.user.displayName = this.authService.getAccount().idTokenClaims.preferred_username;

        if (this.authService.getAccount().idTokenClaims.groups) {
          this.graphService.user.groupIDs = <string[]>(
            (<unknown>this.authService.getAccount().idTokenClaims.groups)
          );
        }
        this.user = this.graphService.user;
        this.checkoutAccount();
      }
    );
    this.authService.handleRedirectCallback((authError, response) => {
      if (authError) {
        console.error('Redirect Error: ', authError.errorMessage);
        return;
      }

      console.log('Redirect Success: ', response);
    });

    this.authService.setLogger(
      new Logger(
        (logLevel, message, piiEnabled) => {
          console.log('MSAL Logging: ', message);
        },
        {
          correlationId: CryptoUtils.createNewGuid(),
          piiLoggingEnabled: false,
        }
      )
    );

    this.broadcastService.subscribe('msal:acquireTokenFailure', (payload) => {
      console.log('Ackuire Token Failure callback');
      const dialogRef = this.dialog.open(ConfirmDialogComponent, {
        maxWidth: '400px',
        disableClose: true,
        data: {
          title: 'Confirm Action',
          message:
            'An authentication exception occured. You need to log out of your American Greetings account and then log back in.',
          confirmationRequired: false
        },
      });
      dialogRef.afterClosed().subscribe((dialogResult) => {
        if (dialogResult) {
          this.logout();
          return;
        }
      });
    });

    this.tokenSubscription = this.broadcastService.subscribe(
      'msal:acquireTokenSuccess',
      (payload) => {
        console.log('Ackuire Token Succes callback');
        if (this.user.displayName.length === 0) {
          this.graphService.user.displayName = this.authService.getAccount().idTokenClaims.preferred_username;

          if (this.authService.getAccount().idTokenClaims.groups) {
            this.graphService.user.groupIDs = <string[]>(
              (<unknown>this.authService.getAccount().idTokenClaims.groups)
            );
          }
          this.user = this.graphService.user;
          console.log(this.user);
        }
      }
    );

    if (this.user.displayName.length === 0 && this.loggedIn) {
      this.graphService.user.displayName = this.authService.getAccount().idTokenClaims.preferred_username;

      if (this.authService.getAccount().idTokenClaims.groups) {
        this.graphService.user.groupIDs = <string[]>(
          (<unknown>this.authService.getAccount().idTokenClaims.groups)
        );
      }
      this.user = this.graphService.user;
      console.log(this.user);
    }
  }

  checkoutAccount() {
    this.loggedIn = !!this.authService.getAccount();
  }

  login() {
    const isIE =
      window.navigator.userAgent.indexOf('MSIE ') > -1 ||
      window.navigator.userAgent.indexOf('Trident/') > -1;

    if (isIE) {
      this.authService.loginRedirect();
    } else {
      this.authService.loginPopup();
    }
  }

  logout() {
    this.authService.logout();
  }

  ngOnDestroy() {
    // disconnect from broadcast service on component destroy
    this.broadcastService.getMSALSubject().next(1);
    if (this.loginSubscription) {
      console.log('Unsubscribe Login Subscription');
      this.loginSubscription.unsubscribe();
    }
    if (this.tokenSubscription) {
      console.log('Unsubscribe Token Subscription');
      this.tokenSubscription.unsubscribe();
    }
  }
}
