/*
 * XML builder. Code for creating the sample-soap-request.xml file
 */
//package examples;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.miktim.xml.builder.XML;

public class ExampleSoapRequest {

    public static void main(String[] args) throws IOException {
        XML xml = new XML("soap:Envelope");
        xml.attr(
                "xmlns:soap", "http://schemas.xmlsoap.org/soap/envelope/",
                "xmlns:cat", "http://samplelib.com/catalog");
        xml.set("soap:Header")
                .set("cat:Auth")
                .set("cat:Token", "b3f1c2d4-5e6a-4b7c-8d9e-0f1a2b3c4d5e");
        xml.set("soap:Body")
                .set("cat:GetBookRequest")
                .add("cat:BookId", "b2")
                .add("cat:IncludePrice", true);
        
        File file = new File("./example-soap-request.xml");
        FileOutputStream fos = new FileOutputStream(file);
        xml.toStream(fos);
        fos.close();
        System.out.println("File created: " + file.getCanonicalPath());
    }
}
