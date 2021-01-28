// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `angular-cli.json`.

export const environment = {
  production: false,
  baseHref: '/reservation/',
  apiResourceUri: 'http://localhost:8080/reservation/api',
  adminGroup: '632eab28-21e4-4af9-ad45-5f96bdd31c32'
};
