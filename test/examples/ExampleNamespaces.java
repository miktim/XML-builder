
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.miktim.xml.builder.XML;

/*
 * XML builder. Code for creating the sample-namespaces.xml file
 */
//package examples;
public class ExampleNamespaces {

    public static void main(String[] args) throws IOException {

        XML xml = new XML("root");
        xml.attr(
                "xmlns", "http://example.com/default",
                "xmlns:a", "http://example.com/a",
                "xmlns:b", "http://example.com/b");
        xml.set("item").attr("id", "1")
                .add("a:title", "Title in namespace A")
                .add("b:title", "Title in namespace B")
                .add("description", "Description in default namespace");
        xml.set("a:section").attr("xmlns", "http://example.com/inner-default")
                .add("heading", "This heading is in the inner default namespace")
                .add("a:label", "This is in namespace A")
                .add("b:label", "This is in namespace B")
                .set("subsection").attr("xmlns:a", "http://example.com/a-redefined")
                .add("a:note", "Namespace A is redefined here to a different URI");
        xml.set("empty-prefix").attr("xmlns", "")
                .add("plain", "This element has no namespace (xmlns=\"\")");

        File file = new File("./example-namespaces.xml");
        FileOutputStream fos = new FileOutputStream(file);
        xml.toStream(fos);
        fos.close();
        System.out.println("File created: " + file.getCanonicalPath());
    }

}
