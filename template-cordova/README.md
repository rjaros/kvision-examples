## Gradle Tasks (`gradlew` command)

### Resource Processing
* generatePotFile - Generates a `src/jsMain/resources/i18n/messages.pot` translation template file.
### Running
* run - Starts a webpack dev server on port 3000.
### Packaging
* jsBrowserDistribution - Bundles the compiled js files into `build/dist/js/productionExecutable`
* zip - Packages a zip archive with all required files into `build/libs/*.zip`
* distCordova - Bundles the application into Cordova source dir `www`

## Cordova tasks (`cordova` command)
### Running
* emulate - run Cordova application on the emulator.
* run - run Cordova application on the connected physical device.
### Packaging
* build - generate the mobile application package.
