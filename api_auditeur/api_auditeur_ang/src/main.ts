import { bootstrapApplication } from '@angular/platform-browser';
import { App } from './app/app';
import { provideHttpClient, HTTP_INTERCEPTORS } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { routes } from './app/app-routing-module';
import 'zone.js';
import { AuthInterceptor } from './app/services/authinterceptor.service';
import { AuthGuard } from './app/services/authguard.service';
import { AuthorizationGuard } from './app/services/authorizationguard';

bootstrapApplication(App, {
  providers: [
    provideRouter(routes),
    provideHttpClient(),
    AuthGuard,
    AuthorizationGuard,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ]
}).catch(err => console.error(err));
