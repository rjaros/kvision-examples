# KVision examples

A set of examples for [KVision](https://github.com/rjaros/kvision) framework.

## Mini template

A minimal KVision application with simplified build configuration. A perfect project for quick start.

## Template

An application template. It does nothing, but includes all dependencies to develop KVision applications with all
supported components (including unit tests). A perfect starting point for a new application.

[See live demo](https://rjaros.github.io/kvision-examples/template/)

## Hello World

A very simple application with almost no optional modules.

[See live demo](https://rjaros.github.io/kvision-examples/helloworld/)

## Showcase

A simple application presenting all main features of KVision framework.

[See live demo](https://rjaros.github.io/kvision-examples/showcase/)

## Address book

An address book application presenting a classic [CRUD](https://en.wikipedia.org/wiki/Create,_read,_update_and_delete) project 
with Material-like CSS template from [Bootswatch](https://bootswatch.com/3/paper/).

[See live demo](https://rjaros.github.io/kvision-examples/addressbook/)

## Address book with Tabulator

An address book application rewritten with a [Tabulator](http://tabulator.info) module.

[See live demo](https://rjaros.github.io/kvision-examples/addressbook-tabulator/)

## Desktop

A very simple desktop with four mini applications - a calculator, a text editor, a paint program and a web browser.

[See live demo](https://rjaros.github.io/kvision-examples/desktop/)

## Pokedex PWA

The list of Pokémon with live search, build with Redux module. It's also a fully compatible [PWA](https://developers.google.com/web/progressive-web-apps/).

[See live demo](https://kvision-pokedex.netlify.com/)

## TodoMVC

A complete implementation of [TodoMVC](https://todomvc.com/) demo application.

[See live demo](https://rjaros.github.io/kvision-examples/todomvc/)

## Onsen UI Kitchen Sink

An example mobile application created with KVision's bindings for [Onsen UI](https://onsen.io/) library. 
It's an almost 1 to 1 rewrite of an [official Vue Kitchen Sink example](https://github.com/OnsenUI/vue-onsenui-kitchensink). 
It presents how to use all Onsen UI visual components with fully type-safe Kotlin API and DSL builders.

[See live demo with iOS platform](https://rjaros.github.io/kvision-examples/onsenui-kitchensink/?platform=ios)

[See live demo with Android platform](https://rjaros.github.io/kvision-examples/onsenui-kitchensink/?platform=android)

## Patternfly demo

A demo application created with [Patternfly UI toolkit](https://www.patternfly.org/). It shows how to create
KVision application with a modern design system alternative to Bootstrap. The application is heavily inspired by
[Patternfly Kotlin project](https://patternfly-kotlin.github.io/patternfly-fritz2-showcase/#user-demo).

[See live demo](https://rjaros.github.io/kvision-examples/patternfly/)

## Fomantic-UI demo

A demo application created with [Fomantic-UI toolkit](https://fomantic-ui.com/). It shows how to create
KVision application with a modern design system alternative to Bootstrap. It also presents state management based 
on Kotlin flows (using both StateFlow and SharedFlow).

[See live demo](https://rjaros.github.io/kvision-examples/fomantic/)

## Address book - fullstack

A complete, fullstack address book application. It presents an innovative way to build fullstack applications with KVision and 
[Ktor](https://ktor.io), [Jooby](https://jooby.io), [Spring Boot](https://spring.io/projects/spring-boot), 
[Javalin](https://javalin.io), [Vert.x](https://vertx.io) or [Micronaut](https://micronaut.io) frameworks. KVision closely integrates the client and the server side of the project with a 
shared data model and fully type-safe connectivity between both sides (based on automatically generated routings and JSON-RPC 
endpoints). The example project utilizes:

- H2 SQL database (local) or PostgreSQL database (configured on Heroku)
- [Exposed](https://github.com/JetBrains/Exposed) Kotlin SQL library for database connectivity (used in Ktor, Javalin and Vert.x versions)
- [Spring Data R2DBC](https://spring.io/projects/spring-data-r2dbc) with Kotlin Coroutines extensions (used in Spring Boot and Micronaut version)
- [Kwery](https://github.com/andrewoma/kwery) ORM for database connectivity (used in Jooby version)
- [Pac4J](https://github.com/pac4j/pac4j) security engine for authentication and profile management (used in Jooby version)

The architecture of the project is heavily based on Kotlin coroutines, wrapping asynchronous client-server calls into 
easy-to-read synchronous-like code.

[See live demo on Heroku](https://kvision-address-book.herokuapp.com/)

## Number converter - fullstack

A simple application to convert integer numbers to words by using JVM [Tradukisto](https://github.com/allegro/tradukisto) library.

[See live demo on Heroku](https://kvision-numbers.herokuapp.com/)

## Encoder - fullstack

A simple application to encode the given text, based on the overview chapter from the [KVision guide](https://kvision.gitbook.io/kvision-guide/part-3-server-side-interface/overview).

[See live demo on Heroku](https://kvision-encoder.herokuapp.com/)

## Tweets - fullstack

A simple, multiplatform, message board application based on websocket communication. 

[See live demo on Heroku](https://kvision-tweets.herokuapp.com/)

## Template - fullstack

A fullstack application template (versions for all supported frameworks). A starting point for a new application.

## Simple MPP - fullstack

A fullstack MPP application created without KVision, but using kvision-remote module for server-side connectivity. 
It's based on an official [Ktor MPP example](https://github.com/ktorio/ktor-samples/tree/master/mpp/fullstack-mpp).

## Template - Electron

An application template for [Electron](https://electronjs.org/) framework. It shows how to build cross-platform 
(Windows, Linux, MacOS), desktop applications with KVision and Kotlin. 

## Template - Apache Cordova

An application template for [Apache Cordova](https://cordova.apache.org/) framework. It shows how to build mobile (Android, iOS),
hybrid applications with KVision and Kotlin.
