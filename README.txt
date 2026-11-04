XML builder - The simple XML builder, MIT (c) 2026 miktim@mail.ru

This is an attempt to implement the simplest XML builder
without using Java XML packages.

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
    Node current();
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

  Methods for generating well-formed XML:
    These methods validate prefix bindings
    and return the XML-formatted text as a single string.

    String toString();
      - returns XML text encoded in the JVM's DEFAULT charset.
    String toString(String charsetName) throws UnsupportedEncodingException;
      - returns XML text encoded in the specified charset.
    void toStream(OutputStream out) throws IOException;
      - writes XML text, encoded in the JVM's DEFAULT charset,
        to the output stream and closes this stream.
    void toStream(OutputStream out, String charsetName) throws IOException;
      - writes XML text, encoded in the specified charset,
        to the output stream and closes this stream.

    Throws:
      IllegalArgumentException: the prefix is not bound.
    
  Note:
    In Java 17 and earlier, the default charset was dynamic;
    it was determined at startup based on the user's operating system,
    locale, and regional settings (e.g., windows-1252 on Western Windows).


Example:

XML xml = new XML("multistatus");   // creates XML with root element "multistatus"
Node propstat = xml.attr("xmlns","DAV:") // adds an attribute to the "multistatus"
     .comment("This is an example") // adds comment to the root content
     .set("response")               // adds child "response" to the root, makes it parent
     .add("href")                   // adds child "href" to the "response"
        .cdata("http://www.example.com/container/") // adds CDATA section to the "href"
     .set("propstat");               // adds child "propstat" to the "response", makes it parent
// propstat root element is "propstat"

// continuation of XML generation
propstat.set("prop")                // adds child "prop" to the "propstat", makes it parent
        .attr("xmlns:R", "http://ns.example.com/schema/")
     .add("R:author", "John Doe")   // same as add("R:author").text("John Doe")
     .add("creationdate", "2026-06-12T23:20:50.52Z")
     .add("displayname", "container")
     .add("supportedlock");
propstat.add("status","HTTP/1.1 200 OK"); // adds "status" to the end of the "propstat" content

// at this point
// xml root: "multistatus", current: "multistatus"
// propstat root: "propstat", current: "status"

xml.toString() returns following XML text (actually as a single line):

<?xml version="1.0" encoding="utf-8"?>
<multistatus xmlns="DAV:">
  <!-- This is an example -->
  <response>
    <href><![CDATA[http://www.example.com/container/]]></href>
    <propstat>
      <prop xmlns:R="http://ns.example.com/schema/">
        <R:author>John Doe</R:author>
        <creationdate>2026-06-12T23:20:50.52Z</creationdate>
        <displayname>container</displayname>
        <supportedlock/>
      </prop>
      <status>HTTP/1.1 200 OK</status>
    </propstat>
  </response>
</multistatus>
