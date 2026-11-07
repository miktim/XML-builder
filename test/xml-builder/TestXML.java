/*
 * XML builder. XML test, MIT (c) 2026 miktim@mail.ru
 */

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.miktim.xml.builder.XML;
import org.xml.sax.Attributes;

public class TestXML {

    static void log(Object obj) {
        System.out.println(obj);
    }

    static void checkXml(String xml, boolean show) throws IOException, ParserConfigurationException, SAXException {
        checkXml(new ByteArrayInputStream(xml.getBytes()), show);
    }

    static void checkXml(InputStream in, boolean show) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        SAXParser parser = factory.newSAXParser();
        DefaultHandler handler = new DefaultHandler();
        if (show) {
            handler = new DefaultHandler() {
                String node = "";

                @Override
                public void startElement(String uri, String localName, String qName, Attributes atts) {
                    node = "start: " + qName + " " + uri;
                }

                @Override
                public void characters(char[] ch, int start, int length) {
                    log(node += " content: " + new String(ch, start, length).trim());
                }

                @Override
                public void endElement(String uri, String localName, String qName) {
                    log("end: " + qName + " " + uri);
                }
            };
        }
        parser.parse(in, handler);
        parser.reset();
    }

    public static void main(String[] args) throws Exception {
        XML xml = new XML("Root");
        xml.text("content").attr("attr","value");
        xml.add("Child");
        log(xml);
        xml = new XML("Root");
        xml.set("Child").text("text").set("Child");
        xml.set("Child");
        log(xml);
        checkXml(xml.toString(), false);
        log("Ok");
//        xml = new XML((new Node("multistatus")).addAttr("xmlns","DAV:"));
        xml = new XML("multistatus");
        xml.attr("xmlns","DAV:")
                .comment("This is an example")
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
        log(xml + "\r\n");
        checkXml(xml.toString(), true);
        log("Ok");//CRLF

        xml = new XML("ТекстовыйУзел");
        xml.text("текст")
                .attr("xmlns:префикс-д","DAV:", "attr","value\" ");
        xml.add("префикс-д:ДочернийУзел").text("текст");
        log(xml);
        checkXml(xml.toString(), false);
        log("Ok");

        String encoding = "windows-1251";
        log(xml.toString(encoding));
        checkXml(xml.toString(encoding), false); // not work
        File file = new File("./test.xml");
        xml.toStream(new FileOutputStream(file), encoding);
        if (xml.toString(encoding).getBytes(encoding).length != file.length()) {
            throw new Exception("Length mismatch");
        }
        FileInputStream fis = new FileInputStream(file);
        checkXml(fis, true);
        file.delete();
        log("Ok");

        xml = new XML("Text.Node");
        xml.add("text")
            .add("Child.Node");
        log(xml);
        checkXml(xml.toString("utf-8"), false);
        log("Ok");

        xml = new XML("TextNode");
        xml.text("text")
           .add("ChildTextNode").text("child text");
        log(xml);
        checkXml(xml.toString(), false);
        log("Ok");

        xml = new XML("ParentNode");
        xml.add("ChildNode");
        log(xml);
        checkXml(xml.toString(), false);
        log("Ok");

        xml = new XML("node");
        xml.text("&<>\"'\u0080").attr("attr", "\u0086&<>\"'");
        log(xml);
        checkXml(xml.toString(), false);
        log("Ok");

        xml = new XML("root");
        xml.cdata(xml);
        log(xml);
        checkXml(xml.toString(), true);
        log("Ok");

        xml = new XML("root");
        xml.attr("attr", "value")
            .add("node").attr("R:attr", "value"); // unbounded prefix
        try {
            log(xml);
            log("FAILED!");
        } catch(Exception e) {
            log(e);
            log("Ok");
        }
       
        xml = new XML("root");
        try {
            xml.attr("xmlns:D", "   ") // empty prefixed namespace
              .add("node").attr("D:attr", "value");
            log(xml);
            log("FAILED!");
        } catch(Exception e) {
            log(e);
            log("Ok");
        }
       
    }
}
