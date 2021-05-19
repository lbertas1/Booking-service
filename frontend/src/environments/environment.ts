// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  apiBaseUrl: 'http://localhost:8090',
  // apiBaseUrl: 'http://First-CLB-322333755.eu-central-1.elb.amazonaws.com',
  // apiBaseUrl: 'http://18.198.24.230',
  // wsUrl: 'ws://18.198.24.230/ws'
  // wsUrl: 'ws://rabbitmq-broker:61613/ws'
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
