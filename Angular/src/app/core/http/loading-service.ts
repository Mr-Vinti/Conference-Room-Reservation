import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
@Injectable()
export class LoadingService {
    totalRequests = 0;
    isLoading = new BehaviorSubject<boolean>(false);
    show() {
        this.totalRequests++;
        this.isLoading.next(true);
    }
    hide() {
        this.totalRequests--;
        if (this.totalRequests === 0) {
            this.isLoading.next(false);
        }
    }
}