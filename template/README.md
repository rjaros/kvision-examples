## Gradle Tasks
Whenever you want to produce a minified "production" version of your code pass in `-Pproduction=true` or `-Pprod=true` to your build command.
### Resource Processing
* generatePotFile - Generates a `src/main/resources/i18n/messages.pot` translation template file.
### Running
* webpack-run - Starts a webpack dev server on port 3000. Logs to `build/logs/webpack-dev-server.log`
* webpack-stop - Stops a webpack dev server.
* run - Starts all configured servers.
* stop - Stops all configured servers.
### Packaging
* webpack-bundle - Bundles the compiles js files into `build/bundle`
* webJar - Packages a standalone "web" jar with all required files into `build/libs/*.jar`
* zip - Packages a zip archive with all required files into `build/libs/*.zip`
