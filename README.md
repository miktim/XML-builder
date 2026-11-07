This is an attempt to implement the simplest XML builder without using Java XML packages.  
Custom XML entities and XSD validation are not supported.  
  
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
XML xml = new XML("soap:Envelope");
xml.attr(
    "xmlns:soap", "http://schemas.xmlsoap.org/soap/envelope/",
    "xmlns:cat", "http://samplelib.com/catalog");
xml.set("soap:Header")
  .set("cat:Auth")
    .add("cat:Token","b3f1c2d4-5e6a-4b7c-8d9e-0f1a2b3c4d5e");
xml.set("soap:Body")
  .set("cat:GetBookRequest")
    .add("cat:BookId", "b2")
    .add("cat:IncludePrice", true);       
```  
The xml.toString() returns following well-formed XML text (actually as a single line):
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:cat="http://samplelib.com/catalog">
  <soap:Header>
    <cat:Auth>
      <cat:Token>b3f1c2d4-5e6a-4b7c-8d9e-0f1a2b3c4d5e</cat:Token>
    </cat:Auth>
  </soap:Header>
  <soap:Body>
    <cat:GetBookRequest>
      <cat:BookId>b2</cat:BookId>
      <cat:IncludePrice>true</cat:IncludePrice>
    </cat:GetBookRequest>
  </soap:Body>
</soap:Envelope>
```  
Package help: [./README.txt](./README.txt)  
Examples: [./test/examples/](./test/examples)  
The latest version of the package is here: [./dist](./dist)  
 

