## Gradle Tasks
Whenever you want to produce a minified "production" version of your code pass in `-Pproduction=true` or `-Pprod=true` to your build command.
### Resource Processing
* generatePotFile - Generates a `src/frontendMain/resources/i18n/messages.pot` translation template file.
### Running
* backendRun - Starts a jooby dev server on port 8080
* frontendRun - Starts a webpack dev server on port 3000. Logs to `build/logs/webpack-dev-server.log`
* frontendStop - Stops a webpack dev server.
* run - Starts all configured servers.
* stop - Stops all configured servers.
### Packaging
* webpack-bundle - Bundles the compiles js files into `build/bundle`
* backendJar - Packages a backend jar with compiled source files into `build/libs/*.jar`
* frontendJar - Packages a standalone "web" frontend jar with all required files into `build/libs/*.jar`
* frontendZip - Packages a frontend zip archive with all required files into `build/libs/*.zip`
* jar - Packages a "fat" jar with all backend sources and dependencies while also embedding frontend resources into `build/libs/*.jar`
