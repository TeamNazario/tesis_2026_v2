import { Injectable, signal } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class LoadingService {
  private readonly activeRequests = signal(0);
  readonly isLoading = signal(false);

  show(): void {
    this.activeRequests.update((value) => value + 1);
    this.isLoading.set(true);
  }

  hide(): void {
    this.activeRequests.update((value) => Math.max(0, value - 1));
    this.isLoading.set(this.activeRequests() > 0);
  }
}
