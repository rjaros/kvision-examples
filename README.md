# KVision examples

A set of examples for [KVision](https://github.com/rjaros/kvision) framework.

## Template

An application template. It does nothing, but includes all dependencies to develop KVision applications with all
supported components (including unit tests). A perfect starting point for a new application.

[See live demo](https://rjaros.github.io/kvision-examples/template/)

## Hello World

A very simple application with no optional modules.

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
[Jooby][https://jooby.org] micro web framework. KVision closely integrates the client and the server side of the project with a 
shared data model and fully type-safe connectivity between both sides (based on automatically generated routings and JSON-RPC 
endpoints). The example project utilizes:

- H2 SQL database (configured for "in memory" database)
- [Kwery](https://github.com/andrewoma/kwery) ORM for database connectivity
- [Pac4J](https://github.com/pac4j/pac4j) security engine for authentication and profile management

The architecture of the project is heavily based on Kotlin coroutines, wrapping asynchronous client-server calls into 
easy-to-read synchronous-like code.
