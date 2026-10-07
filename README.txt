XMLbuilder - The simple XML builder, MIT (c) 2026 miktim@mail.ru

This is an attempt to implement the simplest XML builder
without using Java XML packages.

The jar ./dist/xml-builder-... file was generated with debugging info
using JDK1.8 for target JRE1.7

package org.miktim.xml.builder;

class Node;
  Node is an XML element node.

  Constructors:
    Node(String nodeName);
      Creates a node.
      - checks the nodeName syntax.
        Example:
          new Node("prop");
    Node(String nodeName, Object content);
      Creates a text node.
      - converts the content object into its String representation;
      - checks the content string for illegal characters (0x0-0x8,...,0x7F);
      - escapes ("<", ">", "&") the text content;
      - the content can be null.
        Examples:
          new Node("R:author", "John Doe");
          new Node("IsReadOnly", false);
    Throws:
      NullPointerException: when the node name is null;
      IllegalArgumentException: when the node name is empty or syntactically incorrect.

  Methods:
    Node clone();
      - returns a shallow copy of this Node instance:
        the child nodes themselves are not cloned.

    Node addAttr(String attrName, String attrValue);
      Adds an attribute to the node tag.
      - checks the value for illegal characters (0x0-0x8,...,0x7F);
      - the attribute value will be escaped ("\"","<",">","&")
        and enclosed in double quotes;
      - returns this.
    Node addAttr(String... attrs);
      Adds attributes to the node tag.
      - attrs are attribute name-value pairs;
      - returns this. 
    Throws: 
      NullPointerException: when the name or value is null;
      IllegalArgumentException: when the name is duplicated or syntactically incorrect.
      ArrayIndexOutOfBoundsException: when the name-value pair is incomplete.

    Node setNode(Node node);
      - adds the node to the this (current parent) Node;
      - avoid recursion!;
      - returns node (new parent node).
    Node addNode(Node node);
      - adds the node to the this (current parent) Node.
      - avoid recursion!;
      - returns this (current parent node).
    Node setNode(String nodeName);
      - adds new node to the parent Node;
      - returns new node.
    Node addNode(String nodeName);
      - adds new node to the parent Node;
      - returns this.
    Node setNode(String nodeName, Object content);
      - adds new text node to the parent Node;
      - returns new node.
    Node addNode(String nodeName, Object content);
      - adds new text node to the parent Node;
      - returns this.

    Node addComment(String comment)
      - adds a comment node;
      - comment is a text between "<!-- " and " -->";
      - returns this.

    Throws:
      NullPointerException: when the node or node name argument is null;
      IllegalArgumentException: when the node name is empty or syntactically incorrect.
      
    static String CDATA(Object content);
      - converts the content object into its String representation
        and creates CDATA section;
      - escapes "]]>" with "<![CDATA[]]]><![CDATA[>]>", if any.

    String toString();
      - returns XML text as a single line.

class XML extends Node;
  Constructor:
    XML(Node rootNode);
      Creates an XML with the root node.
      XML can be used as a node.

  Constant:
    static String NAME_PATTERN;
      - the regex XML names pattern: [prefix:]name
  Methods:
    String toString();
      - returns XML text encoded in the JVM's DEFAULT charset.
    String toString(String charset) throws UnsupportedEncodingException;
      - returns XML text encoded in the specified charset.
    void toStream(OutputStream out) throws IOException;
      - writes XML text, encoded in the JVM's DEFAULT charset,
        to the output stream and closes this stream.
    void toStream(OutputStream out, String charset) throws IOException;
      - writes XML text, encoded in the specified charset,
        to the output stream and closes this stream.

    Throws:
      IllegalArgumentException: if the prefix is not bound.
    
  Notes:
    - XML text is a single line;
    - In Java 17 and earlier, the default charset was dynamic;
      it was determined at startup based on the user's operating system,
      locale, and regional settings (e.g., windows-1252 on Western Windows).


Example:

XML xml = new XML((new Node("multistatus)).addAttr("xmlns","DAV:"));
xml.addComment("This is an example")
     .setNode("response")
     .addNode("href", Node.CDATA("http://www.example.com/container/"))
     .setNode("propstat")
       .addNode("status", "HTTP/1.1 200 OK")
       .setNode((new Node("prop"))
            .addAttr("xmlns:R", "http://ns.example.com/schema/"))
         .addNode("R:author", "John Doe")
         .addNode("creationdate", "2026-06-12T23:20:50.52Z")
         .addNode("displayname", "container")
         .addNode("supportedlock");

xml.toString() returns following XML text (actually as a single line):

<?xml version="1.0" encoding="utf-8" ?>
<multistatus xmlns="DAV:">
  <!-- This is an example -->
  <response>
    <href><![CDATA[http://www.example.com/container/]]></href>
    <propstat>
      <status>HTTP/1.1 200 OK</status>
      <prop xmlns:R="http://ns.example.com/schema/">
        <R:author>John Doe</R:author>
        <creationdate>2026-06-12T23:20:50.52Z</creationdate>
        <displayname>container</displayname>
        <supportedlock/>
      </prop>
    </propstat>
  </response>
</multistatus>
