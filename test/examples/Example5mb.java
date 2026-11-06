/*
 * XML builder. Code for creating the sample-5mb.xml file  
 */
//package examples;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import static java.lang.String.format;
import org.miktim.xml.builder.XML;

public class Example5mb {

    public static void main(String[] args) throws IOException {
        XML xml = new XML("records");
        for(int id = 1; id < 32828; id++) {
            xml.set("record").attr("id", "" + id)
                    .add("name", format("Person %08d", id))
                    .add("email", format("person%d@example.com",id))
                    .add("city","Springfield")
                    .add("score", id * 37 % 1000)
                    .add("active", (id & 1) > 0);
        }
        
        File file = new File("./example-5mb.xml");
        FileOutputStream fos = new FileOutputStream(file);
        xml.toStream(fos);
        fos.close();
        System.out.println("File created: " + file.getCanonicalPath());
    }
   
}
