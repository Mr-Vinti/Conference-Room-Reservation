import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from '../shared/models/user';
import * as config from '../modules/app-config.json';

@Injectable({
  providedIn: 'root',
})
export class GraphService {
  user: User = {
    displayName: '',
    groupIDs: [],
    name: '',
  };

  graphUri = config.resources.graphApi.resourceUri;

  constructor(private http: HttpClient) {}

  getGroups() {
    return this.http.get(this.graphUri);
  }

  getNextPage(nextPage) {
    return this.http.get(nextPage);
  }
}
