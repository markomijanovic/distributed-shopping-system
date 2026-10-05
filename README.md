# Distributed Shopping System

A Java EE 8 shopping backend with a REST gateway and three JMS-connected subsystems. Features include users and balances, product categories, carts, wishlists, orders, and transactions.

## Architecture

| Module | Responsibility |
|---|---|
| `CentralniServer` | JAX-RS endpoints, JMS requests, correlation IDs, and replies |
| `Podsistem1` | Users, cities, roles, and balances |
| `Podsistem2` | Products, categories, carts, and wishlists |
| `Podsistem3` | Orders, order items, and transactions |

The gateway uses `jms/IS1Factory`, request queues `jms/Queue1` through `jms/Queue3`, and `jms/QueueResponse` for correlated replies.

## Build

Requires JDK 8 or 11 and Maven. The preparation adds a Maven reactor because the local `Podsistem3` folder had no build descriptor.

```sh
mvn clean package
```

The gateway produces a WAR and the subsystems produce application-client JARs. The Maven build has not yet been run in this environment; it needs access to dependency repositories.

## Server setup

Use a compatible Java EE 8 application server, such as the GlassFish 5 environment used by the original NetBeans projects. Create the JMS connection factory and queues named above. Supply a compatible MySQL Connector/J driver to the application clients/server.

The database names in the original configuration are `podsistem1`, `podsistem2`, and `podsistem3`. Persistence configuration sets schema generation to `none`, so existing schemas are required. No complete schema migration or deployment automation was present in these source folders.

For each subsystem, copy `src/conf/persistence.xml.example` to `src/conf/persistence.xml` and replace `CHANGE_ME` locally. Real `persistence.xml` files are ignored by Git and are required before building a runnable deployment. Database usernames and passwords have been removed from the prepared configuration.

Deploy the gateway WAR, and launch each subsystem using the server's application-client container (`appclient -client <jar>`), rather than plain `java -jar`, because the source depends on injected JMS resources.

## Limitations

This is coursework code. Authentication/authorization, password handling, transactions, response-queue consumption under concurrency, and failure recovery need review before production use. Some gateway requests wait five seconds for a response. This preparation does not claim production security or end-to-end validation.

## Context

Educational project by Marko Mijanovic, University of Belgrade, School of Electrical Engineering. Course scaffolding and supplied assets remain part of the project; this preparation does not grant a new license to third-party material.

## Preparation validation

- JDK 8 javac with Java EE 8 API: passed.
- JDK 8 javac with Java EE 8 API: passed.
- JDK 8 javac with Java EE 8 API: passed.
- JDK 8 javac with Java EE 8 API: passed.

All four modules compile with the local JDK 8 and Java EE 8 API. Maven packaging and server/database/JMS integration still require validation in the deployment environment.
