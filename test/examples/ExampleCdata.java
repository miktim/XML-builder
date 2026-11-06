/*
 * XML builder. Code for creating the sample-cdata.xml file
 */
//package examples;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.miktim.xml.builder.XML;

public class ExampleCdata {

    public static void main(String[] args) throws IOException {
        XML xml = new XML("documents");
        xml.set("doc").attr("id","" + 1)
                .add("title","HTML inside CDATA")
                .set("body")
                .cdata("<html><body><h1>This is <em>HTML</em> content</h1><p>No need to escape & or < or > inside CDATA.</p></body></html>");
        xml.set("doc").attr("id","" + 2)
                .add("title","Code snippet inside CDATA")
                .set("body")
                .cdata("\n" +
"function compare(a, b) {\n" +
"  if (a < b) return -1;\n" +
"  if (a > b) return 1;\n" +
"  return 0;  // a === b\n" +
"}\n    ");
        xml.set("doc").attr("id","" + 3)
                .add("title","CDATA containing the end-of-CDATA sequence")
                .set("body")
                .cdata("The literal sequence ]]")
                .cdata("> is split across two CDATA sections to embed it.");
        xml.set("doc").attr("id","" + 4)
                .add("title","Mixed CDATA and entity-escaped text")
                .set("body")
                .text("Before CDATA & some entities <tag>")
                .cdata(" Inside CDATA: <raw> & \"quotes\" without escaping ")
                .text("After CDATA & more.");
                
        File file = new File("./example-cdata.xml");
        FileOutputStream fos = new FileOutputStream(file);
        xml.toStream(fos);
        fos.close();
        System.out.println("File created: " + file.getCanonicalPath());
    }
    
}
