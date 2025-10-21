import { bootstrapApplication } from '@angular/platform-browser';
import { App } from './app/app';
import { provideHttpClient } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { routes } from './app/app-routing-module';
import { ReactiveFormsModule } from '@angular/forms';
import 'zone.js';
import { ApplicationConfig } from '@angular/core';

console.log('Bootstrapping Angular app...');

bootstrapApplication(App, {
  providers: [
    provideHttpClient(),
    provideRouter(routes)
  ]
}).catch(err => console.error(err));

/*ajouter*/
export const appConfig: ApplicationConfig = {
  providers: [
    provideHttpClient(),
    // ... autres providers
  ]
};
