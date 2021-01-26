import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, Router } from '@angular/router';
import { MsalService } from '@azure/msal-angular';
import { GraphService } from './graph.service';
import * as config from '../modules/app-config.json';
import { InteractionRequiredAuthError, AuthError } from 'msal';

@Injectable({
  providedIn: 'root',
})
export class GroupGuardService implements CanActivate {
  groups: string[] = [];

  constructor(
    private authService: MsalService,
    private service: GraphService,
    private router: Router
  ) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {
    this.service.user.displayName = this.authService.getAccount().idTokenClaims.preferred_username;

    if (this.authService.getAccount().idTokenClaims.groups) {
      this.service.user.groupIDs = <string[]>(
        (<unknown>this.authService.getAccount().idTokenClaims.groups)
      );
    }

    const expectedGroup = route.data.expectedGroup;

    this.service.user.displayName = this.authService.getAccount().idTokenClaims.preferred_username;
    this.service.user.name = this.authService.getAccount().idTokenClaims.name;
    if (this.service.user.groupIDs.length === 0) {
      if (this.authService.getAccount().idTokenClaims.hasgroups) {
        window.alert(
          'You have too many group memberships. The application will now query Microsoft Graph to get the full list of groups that you are a member of.'
        );
        this.handleResponse();
        return false;
      }

      window.alert('Token does not have groups claim');
      return false;
    } else if (!this.service.user.groupIDs.includes(expectedGroup)) {
      window.alert('You do not have access for this');
      return false;
    }

    return true;
  }

  handleResponse(): void {
    this.service.getGroups().subscribe({
      next: (response: any) => {
        response.value.map((v) => this.groups.push(v.id));

        /**
         * Some queries against Microsoft Graph return multiple pages of data either due to server-side paging
         * or due to the use of the $top query parameter to specifically limit the page size in a request.
         * When a result set spans multiple pages, Microsoft Graph returns an @odata.nextLink property in
         * the response that contains a URL to the next page of results.
         * learn more at https://docs.microsoft.com/graph/paging
         */
        if (response['@odata.nextLink']) {
          this.handleNextPage(response['@odata.nextLink']);
        } else {
          if (this.groups.includes(config.groups.groupAllUsers)) {
            this.service.user.groupIDs.push(config.groups.groupAllUsers);
          }
        }

        console.log(this.groups);
      },
      error: (err: AuthError) => {
        console.log(err);
        // If there is an interaction required error,
        // call one of the interactive methods and then make the request again.
        if (
          InteractionRequiredAuthError.isInteractionRequiredError(err.errorCode)
        ) {
          this.authService
            .acquireTokenPopup({
              scopes: this.authService.getScopesForEndpoint(
                config.resources.graphApi.resourceUri
              ),
            })
            .then(() => {
              this.service
                .getGroups()
                .toPromise()
                .then((response: any) => {
                  response.value.map((v) => this.groups.push(v.id));

                  if (response['@odata.nextLink']) {
                    this.handleNextPage(response['@odata.nextLink']);
                  } else {
                    if (this.groups.includes(config.groups.groupAllUsers)) {
                      this.service.user.groupIDs.push(
                        config.groups.groupAllUsers
                      );
                    }
                  }
                  console.log(this.groups);
                });
            });
        }
      },
    });
  }

  handleNextPage(nextPage): void {
    this.service.getNextPage(nextPage).subscribe((response: any) => {
      response.value.map((v) => {
        if (!this.groups.includes(v.id)) {
          this.groups.push(v.id);
        }
      });

      if (response['@odata.nextLink']) {
        this.handleNextPage(response['@odata.nextLink']);
      } else {
        if (this.groups.includes(config.groups.groupAllUsers)) {
          this.service.user.groupIDs.push(config.groups.groupAllUsers);
        }
      }
    });
  }
}
