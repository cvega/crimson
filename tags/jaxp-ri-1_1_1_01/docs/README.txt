Crimson
README.txt
30 Jan 2001

This version of Crimson supports the Java API for XML Processing (JAXP)
version 1.1 specification by providing implementations for the following
package hierarchies: javax.xml.parsers, org.w3c.dom, org.xml.sax.*.  Note
that the javax.xml.transform hierarchy is NOT supported.  One known
implementation of the javax.xml.transform hierarchy is Xalan 2.

To use the binaries, put the following two jar files in your classpath:
    + jaxp.jar = contains the javax.xml.* hierarchy
    + crimson.jar = contains org.w3c.dom and org.xml.sax.* and the
      implementation for the parsing parts of JAXP 1.1

Documentation is in:
    + example programs = see examples/index.html
    + JAXP 1.1 javadoc = see docs/api/index.html

Source files are under the "src" directory.
