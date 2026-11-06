/*
 * XML builder. Code for creating the sample-mixed-content.xml file  
 */
//package examples;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.miktim.xml.builder.XML;

public class ExampleMixedContent {

    public static void main(String[] args) throws IOException {
        XML xml = new XML("article");
        xml.set("title", "The Curious Case of")
                .add("em", "Mixed content")
                .parent().text("in Xml");
        xml.set("paragraph", "XML allows elements to contain a mixture of text and child elements, known as")
                .add("term", "mixed content")
                .parent().text(". For example,")
                .add("person", "Alan Turing").attr("id", "alan-turing")
                .parent().text("proposed the")
                .add("concept", "Turing test")
                .parent().text("in")
                .add("year", "1950")
                .parent().text(", building on earlier work by")
                .add("person", "Ada Lovelace").attr("id", "ada-lovelace")
                .parent().text(".");
        xml.set("paragraph", "Whitespace handling in mixed content is")
                .add("strong", "tricky")
                .parent().text(": parsers preserve significant whitespace between text and tags, but the")
                .add("code", "xml:space=\"preserve\"")
                .parent().text("attribute can override defaults.");
        xml.set("paragraph", "Leading and trailing whitespace is preserved here")
                .attr("xml", "space:preserve")
                .add("em","as well as")
                .parent().text("inside")
                .add("code", "code blocks")
                .parent().text(".");

        File file = new File("./example-mixed-content.xml");
        FileOutputStream fos = new FileOutputStream(file);
        xml.toStream(fos);
        fos.close();
        System.out.println("File created: " + file.getCanonicalPath());
    }

}
