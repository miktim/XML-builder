/*
 * XML builder. Code for creating the sample-deep-nested.xml file
 */
//package examples;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.miktim.xml.builder.Node;
import org.miktim.xml.builder.XML;

public class ExampleDeepNested {

    public static void main(String[] args) throws IOException {
        XML xml = new XML("root");
        Node node = xml;
        for(int i = 1; i <= 200; i++) {
            node = node.set("level").attr("n","" + i);
        }
        node.add("leaf").attr("value","deepest");
        
        File file = new File("./example-deep-nested.xml");
        FileOutputStream fos = new FileOutputStream(file);
        xml.toStream(fos);
        fos.close();
        System.out.println("File created: " + file.getCanonicalPath());
    }
}
