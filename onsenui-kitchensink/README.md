## Gradle Tasks
Whenever you want to produce a minified "production" version of your code pass in `-Pproduction=true` or `-Pprod=true` to your build command.
### Resource Processing
* generatePotFile - Generates a `src/main/resources/i18n/messages.pot` translation template file.
### Running
* run - Starts a webpack dev server on port 3000.
### Packaging
* browserWebpack - Bundles the compiled js files into `build/distributions`
* zip - Packages a zip archive with all required files into `build/libs/*.zip`
