XML builder - The simple XML builder, MIT (c) 2026 miktim@mail.ru

This is an attempt to implement the simplest XML builder
without using Java XML packages.
Custom XML entities and XSD validation are not supported.

The jar ./dist/xml-builder-... file was generated with debugging info
using JDK1.8 for target JRE1.7

package org.miktim.xml.builder;

class XML extends Node;

  Constructor:
    XML(String rootName);
      Creates an XML with root element.

  Methods:
    The Node is an XML or an element.

    Node add(String name);
      - adds an element to the parent node;
      - the element becomes the current one;
      - returns parent node;
    Node set(String name);
      - adds an element to the parent node;
      - the element becomes the current one;
      - returns this element as the parent;
    Node parent();
      - makes the parent the current;
      - returns parent node;

    Node attr(String name, String attrValue);
      - adds an attribute to the current element
      - returns parent node;
    Node attr(String... attrs);
      - adds attributes to the current element;
      - attrs are attribute name-value pairs;
      - returns parent node;
    Node text(Object content);
      - adds the text to the current element content
      - returns parent node;
    Node cdata(Object content);
      - adds CDATA section to the current element
      - returns parent node;

    Node add(String name, Object content)
      - same as add(name).text(content)
    Node set(String name, Object content)
      - same as set(name).text(content)
    Node comment(String content);
      - adds comment to the current node content;
      - returns parent node;

    Throws:
      NullPointerException: 
        the name argument or the attribute value is null;
      IllegalArgumentException:
        - the name is empty or syntactically incorrect;
        - the attribute is duplicated;
        - the text or attribute value contains illegal characters
      ArrayIndexOutOfBoundsException:
        the attributes name-value pair is incomplete.

  Methods for creating well-formed XML:
    These methods check prefix bindings
    and generate text in XML format as a single line.

    String toString();
      - returns XML text encoded in the JVM's DEFAULT charset.
    String toString(String charsetName) throws UnsupportedEncodingException;
      - returns XML text encoded in the specified charset.
    void toStream(OutputStream out) throws IOException;
      - writes XML text, encoded in the UTF-8 charset, to the output stream. 
        The stream remains open and you must close it manually.
    void toStream(OutputStream out, String charsetName) throws IOException;
      - writes XML text, encoded in the specified charset, to the output stream.
        The stream remains open and you must close it manually.

    Throws:
      IllegalArgumentException: the prefix is not bound.
    
  Note:
    In Java 17 and earlier, the default charset was dynamic;
    it was determined at startup based on the user's operating system,
    locale, and regional settings (e.g., windows-1252 on Western Windows).


Example:

XML xml = new XML("root");
Node a = xml.set("a");
a.set("b").text("text b")
  .add("c")
  .add("d").text("text d");
a.set("b")
  .add("c").text("text c")
  .add("d")
  .parent().text("text b");
     
xml.toString() method returns the following XML text 
(in fact, as a single line):

<?xml version="1.0" encoding="UTF-8"?>
<root>
  <a>
    <b>text b<c/><d>text d</d></b>
    <b><c>text c</c><d/>text b</b>
  </a>
</root>

See also: ./test/examples/