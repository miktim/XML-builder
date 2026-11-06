/*
 * XML builder. Code for creating the sample-soap-response.xml file  
 */
//package examples;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.miktim.xml.builder.XML;

public class ExampleSoapResponse {

    public static void main(String[] args) throws IOException {
        XML xml = new XML("soap:Envelope");
        xml.attr(
                "xmlns:soap", "http://schemas.xmlsoap.org/soap/envelope/",
                "xmlns:cat", "http://samplelib.com/catalog");
        xml.set("soap:Body")
            .set("cat:GetBookResponse")
                .set("cat:Book").attr("id", "b2", "lang","en")
                    .add("cat:Title", "Clean Code")
                    .add("cat:Author", "Robert C. Martin")
                    .add("cat:Year", 2008)
                    .add("cat:Price", "34.50").attr("currency","USD");
        
        File file = new File("./example-soap-response.xml");
        FileOutputStream fos = new FileOutputStream(file);
        xml.toStream(fos);
        fos.close();
        System.out.println("File created: " + file.getCanonicalPath());
    }
    
}
