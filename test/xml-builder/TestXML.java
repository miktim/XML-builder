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
import org.miktim.xml.builder.Node;
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
        XML xml = new XML(new Node("Root", "content").addAttr("attr","value"));
        xml.addNode("Child");
        log(xml);
        xml = new XML(new Node("Root"));
        xml.setNode("Child", "content")
                .setNode("Child");
        xml.setNode("Child");
        log(xml);
        checkXml(xml.toString(), false);
        log("Ok");
        xml = new XML((new Node("multistatus")).addAttr("xmlns","DAV:"));
        xml.addComment("This is an example");
        xml.setNode("response")
                .addNode("href", Node.CDATA("http://www.example.com/container/"))
                .setNode("propstat")
                .addNode("status", "HTTP/1.1 200 OK")
                .setNode((new Node("prop"))
                        .addAttr("xmlns:R", "http://ns.example.com/schema/"))
                .addNode("R:author", "John Doe")
                .addNode("creationdate", "2026-06-12T23:20:50.52Z")
                .addNode("displayname", "container")
                .addNode("supportedlock");
        log(xml + "\r\n");
        checkXml(xml.toString(), true);
        log("Ok");//CRLF
        log("XML clone");
        if(xml.toString().equals((new XML(xml.clone())).toString()))
            log("Ok");
        else {
            log("FAILED!");
            System.exit(1);
        }            
        xml = new XML((new Node("ТекстовыйУзел", "текст"))
                .addAttr("xmlns:префикс","DAV:", "attr","value\" "));
        xml.addNode("префикс:ДочернийУзел","текст");
        log(xml);
        checkXml(xml.toString(), false);
        log("Ok");

        String encoding = "windows-1251";
        log(xml.toString(encoding));
        checkXml(xml.toString(encoding), false); // not work
        File file = new File("./test.xml");
        FileOutputStream fos = new FileOutputStream(file);
        xml.toStream(new FileOutputStream(file), encoding);
        fos.close();
        if (xml.toString(encoding).getBytes(encoding).length != file.length()) {
            throw new Exception("Length mismatch");
        }
        FileInputStream fis = new FileInputStream(file);
        checkXml(fis, true);
        file.delete();
        log("Ok");

        xml = new XML(new Node("Text.Node", "text"));
        xml.addNode("Child.Node");
        log(xml);
        checkXml(xml.toString("utf-8"), false);
        log("Ok");

        xml = new XML(new Node("TextNode", "text"));
        xml.addNode(new Node("ChildTextNode", "child text"));
        log(xml);
        checkXml(xml.toString(), false);
        log("Ok");

        xml = new XML(new Node("ParentNode"));
        xml.addNode("ChildNode");
        log(xml);
        checkXml(xml.toString(), false);
        log("Ok");

        xml = new XML((new Node("node","&<>\"'\u0080")).addAttr("attr", "\u0086&<>\"'"));
        log(xml);
        checkXml(xml.toString(), false);
        log("Ok");

        xml = new XML((new Node("node",Node.CDATA(Node.CDATA(new Node("node"))))));
        log(xml);
        checkXml(xml.toString(), true);
        log("Ok");

        xml = new XML((new XML(new Node("node")).addAttr("attr", "value")));
        xml.addNode((new XML(new Node("node")).addAttr("attr", "value")));
        log(xml);
        checkXml(xml.toString(), false);
        log("Ok");
       
    }
}
