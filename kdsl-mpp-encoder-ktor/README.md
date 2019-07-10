## Gradle Tasks
* pot - Generates a `src/frontendMain/reseources/i18n/messages.pot` translation template file.
* po2json - Processes `src/frontendMain/resources/i18n/*.po` files and converts them to json.
* backendRun - Starts backend ktor server on port 8080. Logs to `build/logs/ktor-8080.log`
* frontendRun - Starts frontend webpack server on port 3000. Logs to `build/logs/webpack-dev-server.log`
* backendStop - Stops backend ktor server.
* frontendStop - Stops frontend ktor server.
* run - Starts both, backend and frontend servers.
* stop - Stops both, backend and frontend servers.
* backendJar - Packages backend sources.
* frontendJar - Packages frontend sources.
* jar - Packages a standalone "fat" jar with all required dependencies.