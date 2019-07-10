## Gradle Tasks
Whenever you want to produce a minified "production" version of your code pass in `-Pproduction=true` or `-Pprod=true` to your build command.
### Resource Processing
* generatePotFile - Generates a `build/reseources/main/i18n/messages.pot` translation template file.
### Running
* webpack-run - Starts webpack server on port 3000. Logs to `build/logs/webpack-dev-server.log`
* webpack-stop - Stops webpack dev server.
* run - Starts all dev servers.
* stop - Stops all dev servers.
### Packaging
* webpack-bundle - Creates a webpack bundle at `build/bundle/`.
* jar - Packages a standalone "web" jar with all required files at `build/libs/`