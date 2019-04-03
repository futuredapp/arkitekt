MVVM Android
============

MVVM Android is the framework based on Android Architecture components and gives you set of
base classes to implement concise, testable and solid application. It combines built-in
support for Dagger 2 dependency injection, View DataBinding, ViewModel and RxJava
interactors (use cases). Architecture described here is used among wide variety of
projects and it's production ready.

Overview
========
Standard MVVM architecture mentions three base components. Model, View and ViewModel.
Communication between these components works as described in the following schema:


As you might see, `View`, represented by Activity/Fragment communicates only with `ViewModel`
and it happens through Datainding, through ViewModel reference stored in Activity or by listening to one-shot events.
It is important to note, that ViewModel is pure Java object and it doesn't hold reference to
Activity/Fragment.

Getting started
===============
TBD

Download
========
