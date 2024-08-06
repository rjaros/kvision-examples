## Gradle Tasks

### Resource Processing
* generatePotFile - Generates a `src/jsMain/resources/i18n/messages.pot` translation template file.
### Compiling
* compileKotlinJs - Compiles frontend sources.
* compileKotlinJvm - Compiles backend sources.
### Running
* jsRun - Starts a webpack dev server on port 3000
* jvmRun - Starts a dev server on port 8080
### Packaging
* jsBrowserDistribution - Bundles the compiled js files into `build/dist/js/productionExecutable`
* jsJar - Packages a standalone "web" frontend jar with all required files into `build/libs/*.jar`
* jvmJar - Packages a backend jar with compiled source files into `build/libs/*.jar`
* jar - Packages a "fat" jar with all backend sources and dependencies while also embedding frontend resources into `build/libs/*.jar`
