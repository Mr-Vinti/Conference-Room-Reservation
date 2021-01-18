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
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { environment } from '../../environments/environment';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';


// Microsoft
import {
  MsalModule,
  MsalInterceptor,
  MSAL_CONFIG,
  MSAL_CONFIG_ANGULAR,
  MsalService,
  MsalAngularConfiguration
} from '@azure/msal-angular';
import * as config from './app-config.json';
import { Configuration, Logger, CacheLocation } from 'msal';
import { LogLevel } from 'msal';

// external angular components,modules,directives
import { ModuleOneModule } from '../modules/module-one/module-one.module';
import { AppComponent } from './app.component';

// core Module
import { CoreModule } from '../core/core.module';

// shared Module
import { SharedModule } from '../shared/shared.module';

// routes
import { AppRoutingModule } from './app-routing.module';
import { LoadingService } from '../core/http/loading-service';
import { LoadingInterceptor } from '../core/http/loading-interceptor';

// Material
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';

export function loggerCallback(logLevel, message, piiEnabled) {
  console.log('client logging' + message);
}


export const protectedResourceMap: [string, string[]][] = [
  [environment.apiResourceUri, [config.resources.backendApi.resourceScope]],
  [config.resources.graphApi.resourceUri, [config.resources.graphApi.resourceScope]]
];

const isIE = window.navigator.userAgent.indexOf('MSIE ') > -1 || window.navigator.userAgent.indexOf('Trident/') > -1;

function MSALConfigFactory(): Configuration {
  return {
    auth: {
      clientId: config.auth.clientId,
      authority: config.auth.authority,
      validateAuthority: true,
      redirectUri: window.location.origin + environment.baseHref,
      postLogoutRedirectUri: window.location.origin + environment.baseHref,
      navigateToLoginRequestUrl: false,
    },
    system: {
      logger: new Logger(loggerCallback, {
        correlationId: '1000',
        level: LogLevel.Info,
        piiLoggingEnabled: true
      }),
    },
    cache: {
      cacheLocation: <CacheLocation>config.cache.cacheLocation,
      storeAuthStateInCookie: isIE, // set to true for IE 11
    },
  };
}

function MSALAngularConfigFactory(): MsalAngularConfiguration {
  return {
    popUp: !isIE,
    consentScopes: [
      config.resources.backendApi.resourceScope,
      ...config.scopes.loginRequest
    ],
    unprotectedResources: [],
    protectedResourceMap,
    extraQueryParameters: {}
  };
}

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    CommonModule,
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    MatButtonModule,
    HttpClientModule,
    AppRoutingModule,
    CoreModule,
    SharedModule,
    ModuleOneModule,
    MatProgressSpinnerModule,
    MatToolbarModule,
    MsalModule,
    BrowserAnimationsModule],
  bootstrap: [AppComponent],
  providers: [
    [LoadingService],
    {
      provide: HTTP_INTERCEPTORS,
      useClass: MsalInterceptor, multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: LoadingInterceptor, multi: true
    },
    {
      provide: MSAL_CONFIG,
      useFactory: MSALConfigFactory
    },
    {
      provide: MSAL_CONFIG_ANGULAR,
      useFactory: MSALAngularConfigFactory
    },
    MsalService
  ]
})
export class AppModule { }
