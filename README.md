
This is an attempt to implement the simplest XML builder without using Java XML packages.  

Look First:  
XMLBuilder: [https://github.com/simonemmott/XMLBuilder](https://github.com/simonemmott/XMLBuilder)  
  
XMLBuilder: [https://github.com/atulsm/XMLBuilder/tree/master](https://github.com/atulsm/XMLBuilder/tree/master)  
XISS: [https://github.com/mschrag/xiss/tree/master](https://github.com/mschrag/xiss/tree/master)  
java-xmlbuilder: [https://github.com/jmurty/java-xmlbuilder](https://github.com/jmurty/java-xmlbuilder)  
xembly: [https://github.com/yegor256/xembly](https://github.com/yegor256/xembly)  
simpleXml: [https://github.com/codemonstur/simplexml](https://github.com/codemonstur/simplexml)  
. . . and others.  
  
  
An example of using this package (Java):
```java
XML xml = new XML("multistatus");
xml.attr("xmlns","DAV:");
  .set("response")
    .add("href").cdata("http://www.example.com/container/")
    .set("propstat")
      .add("status").text("HTTP/1.1 200 OK")
      .set("prop")
          .attr("xmlns:R", "http://ns.example.com/schema/")
        .add("R:author", "John Doe")
        .add("creationdate", "2026-06-12T23:20:50.52Z")
        .add("displayname", "container")
        .add("supportedlock");
```  
The xml.toString() method returns the following XML text (actually as a single line):
```xml
<?xml version="1.0" encoding="utf-8"?>
<multistatus xmlns="DAV:">
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
```  
Package help: [./README.txt](./README.txt)  
The latest version of the package is here: [./dist](./dist)  
 

