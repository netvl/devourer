Devourer - simple XML processing library
========================================

Devourer is simple streaming XML processing library written in Java taking advantage of modern XML
parsing library and providing easy-to-use API, which is compatible with latest Java releases,
i.e. Java 8 and its Project Lambda.

Devourer is heavily inspired by [Apache Digester](http://commons.apache.org/digester/). Devourer
essentially performs the same job as Digester, but it avoids heavy use of reflection (thus making
your XML processing way faster) and it also uses StAX processing library instead of SAX, which leads
to much simpler and easier to support architecture, as well as nearly effortless thread
safety. Devourer also has minimal amount of runtime dependencies (only
[Google Guava](https://code.google.com/p/guava-libraries/) library).

Installation
------------

Devourer is in process of being published to Maven Central. When it gets there, you would be able to
add it to your Maven dependency list:

```xml
    <dependency>
        <groupId>org.bitbucket.googolplex.devourer</groupId>
        <artifactId>devourer</artifactId>
        <version>0.1</version>
    </dependency>
```

Usage
-----

See [here](http://dpx-infinity.github.com/devourer/overview.html) for the user manual.

Notes
-----

There are several points I think I should note about the library.

* Namespace support is far from adequate. Currently all namespace filtering must be done inside
  actions; also namespaces are mostly handled via prefixes, not real namespace identifiers. This
  should change in the nearest future.
* The library is not covered by automatic tests. This is also likely to change soon.
* Support for wildcards in element paths is completely absent. This would be very nice feature to
  have, and I may add it one day.

