/*
 * XML builder. Code for creating the sample-complex.xml file  
 */
//package examples;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.miktim.xml.builder.Node;
import org.miktim.xml.builder.XML;

public class ExampleComplex {

    public static void main(String[] args) throws IOException {
        XML xml = new XML("po:PurchaseOrder");
        xml.attr(
                "xmlns:po", "http://example.com",
                "xmlns:xsi", "http://w3.org",
                "xsi:schemaLocation", "http://example.com schema.xsd",
                "orderId", "PO-2026-99503",
                "orderDate", "2026-11-05T08:00:00Z",
                "priority", "high",
                "status", "confirmed"
        );
        Node node = xml.set("po:Customer").attr("customerId", "CUST-45812");
        node.set("po:Name")
                .add("po:FirstName", "Ellen")
                .add("po:LastName", "Adams");
        node.set("po:Contact")
                .add("po:Email", "ellen.adams@example.com")
                .add("po:Phone", "+1-555-0199").attr("type", "mobile");
        node = xml.set("po:Addresses");
        node.set("po:ShippingAddress").attr("type", "residence")
                .add("po:Street", "123 Maple Street")
                .add("po:City", "Mill Valley")
                .add("po:State", "CA")
                .add("po:PostalCode", 10999)
                .add("po:Country", "USA");
        node.set("po:BillingAddress").attr("type", "office")
                .add("po:Street", "8 Oak Avenue")
                .add("po:City", "Old Town")
                .add("po:State", "PA")
                .add("po:PostalCode", 95819)
                .add("po:Country", "USA");
        xml.set("po:DeliveryInstructions")
                .add("po:Note", "Please leave packages in shed by driveway.")
                .add("po:SignatureRequired", false);
        node = xml.set("po:Items");
        node.set("po:Item").attr("itemId", "ITEM-1", "sku", "872-AA")
                .add("po:Description", "Lawnmower (Electric)")
                .add("po:Quantity", 1).attr("units", "pcs")
                .add("po:Price", 148.95).attr("currency", "USD")
                .add("po:Comment", "Confirm this is electric");
        node.set("po:Item").attr("itemId", "ITEM-2", "sku", "926-AA")
                .add("po:Description", "Baby Monitor")
                .add("po:Quantity", 2).attr("units", "pcs")
                .add("po:Price", 39.98).attr("currency", "USD")
                .add("po:ShipDate", "2026-05-21");
        xml.set("po:Totals")
                .add("po:Subtotal", 188.93).attr("currency", "USD")
                .add("po:Tax", 15.11).attr("currency", "USD")
                .add("po:ShippingCost", "12.00").attr("currency", "USD")
                .add("po:GrandTotal", 216.04).attr("currency", "USD");

        File file = new File("./example-complex.xml");
        FileOutputStream fos = new FileOutputStream(file);
        xml.toStream(fos);
        fos.close();
        System.out.println("File created: " + file.getCanonicalPath());
    }

}
