Crimson 1.1.2beta2
README.txt
2001-8-10, (August 10, 2001)

This version of Crimson supports the Java API for XML Processing (JAXP)
version 1.1 specification by providing implementations for the following
package hierarchies: javax.xml.parsers, org.w3c.dom, org.xml.sax.*.  Note
that the javax.xml.transform hierarchy is NOT supported.  One known
implementation of the javax.xml.transform hierarchy is Xalan 2.  The
implementation is split into two separate jar files.

From comments found in ChangeLog...

This version fixes bugs in previous releases such as version 1.1.1 and 1.1.
It was named 1.1.2beta2 because it is the same code that went into J2SE
1.4 beta2 (also called beta refresh).  Despite the "beta2" name,
this version is just as stable as the previous version 1.1.1.

This version also uses the new common XML API jarfile to contain
the JAXP API and removes the DOM and SAX classes from crimson.jar.
To use this package, place both xml-apis.jar and crimson.jar in
your classpath.


To use the binaries, put the following two jar files in your classpath:
    + xml-apis.jar = contains the javax.xml.*, DOM, and SAX packages
    + crimson.jar = contains the implementation for the parsing parts of
      the JAXP 1.1 specification

Documentation is in:
    + Example programs = see examples/index.html
    + JAXP 1.1 javadoc = see docs/javadoc/index.html
    + JAXP FAQ = http://xml.apache.org/~edwingo/jaxp-faq.html

Source files are under the "src" directory.
