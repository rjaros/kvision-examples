# KVision examples

A set of examples for [KVision](https://github.com/rjaros/kvision) framework.

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

## Desktop

A very simple desktop with four mini applications - a calculator, a text editor, a paint program and a web browser.

[See live demo](https://rjaros.github.io/kvision-examples/desktop/)

## TodoMVC

A complete implementation of [TodoMVC](http://todomvc.com/) demo application.

[See live demo](https://rjaros.github.io/kvision-examples/todomvc/)

## Address book - fullstack

A complete, multiplatform address book application. It presents an innovative way to build fullstack applications with KVision and 
[Ktor](https://ktor.io), [Jooby](https://jooby.org) or [Spring Boot](https://spring.io/projects/spring-boot) frameworks. KVision closely integrates the client and the server side of the project with a 
shared data model and fully type-safe connectivity between both sides (based on automatically generated routings and JSON-RPC 
endpoints). The example project utilizes:

- H2 SQL database (local) or PostgreSQL database (configured on Heroku)
- [Exposed](https://github.com/JetBrains/Exposed) Kotlin SQL library for database connectivity (used in Ktor version)
- [Kwery](https://github.com/andrewoma/kwery) ORM for database connectivity (used in Jooby and Spring Boot versions)
- [Pac4J](https://github.com/pac4j/pac4j) security engine for authentication and profile management (used in Jooby and Spring Boot versions)

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

A simple, multiplatform, message board application. 

[See live demo on Heroku](https://kvision-tweets.herokuapp.com/)

Note: The application uses simple, in memory storage, so all messages are gone after Heroku redeployments.

## Template - fullstack

A fullstack application template (versions for all supported frameworks). A starting point for a new application.

## Simple MPP - fullstack

A fullstack MPP application created without KVision, but using kvision-remote module for server-side connectivity. 
It's based on an official [Ktor MPP example](https://github.com/ktorio/ktor-samples/tree/master/mpp/fullstack-mpp).
