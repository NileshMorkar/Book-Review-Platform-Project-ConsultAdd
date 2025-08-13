import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { App } from './app/app';

bootstrapApplication(App, appConfig)
  .catch((err) => console.error(err));


// import { bootstrapApplication } from '@angular/platform-browser';
// import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
// import { App } from './app/app';
// import { appConfig } from './app/app.config';

// bootstrapApplication(App,{
//   providers: [
//     provideHttpClient(withInterceptorsFromDi()) // replaces HttpClientModule
//   ]
// },);