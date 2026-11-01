/*
 * XML builder. Node test, MIT (c) 2026 miktim@mail.ru
 */

import org.miktim.xml.builder.Node;

public class TestNode {

    static void log(Object obj) {
        System.out.println(obj);
    }
    
    static void logErr(Object obj) {
        System.err.println(obj);
    }
    
    static void testNode(Node node, String expected) {
        String nodeXml = node.toString();
        log(nodeXml);
        if(nodeXml.equals(expected)) 
            log("Ok");
        else {
            logErr(expected);
            logErr("Failed!");
        }
    }
        
    public static void main(String[] args) throws Exception {
        Node node;
        String expected;
        node = new Node("node");
        expected = "<node/>";
        testNode(node, expected);

        node = (new Node("node")).text(null);
        expected = "<node/>";
        testNode(node, expected);
        node = (new Node("node")).text("");
        expected = "<node/>";
        testNode(node, expected);
        node = (new Node("node")).text("    ");
        expected = "<node>    </node>";
        testNode(node, expected);

        node = (new Node("node")).text("text");
        expected = "<node>text</node>";
        testNode(node, expected);
        node.set("child","text");
        expected = "<node>text<child>text</child></node>";
        testNode(node, expected);
        node.set("boolean",false);
        expected = "<node>text<child>text</child><boolean>false</boolean></node>";
        testNode(node, expected);

        node = (new Node("node")).text("text");
        node.set("child","text").add("number",12);
        expected = "<node>text<child>text<number>12</number></child></node>";
        testNode(node, expected);

        node = (new Node("node")).set("text");
        node.set("child").text((new Node("number")).text(12));
        expected = "<text><child>&lt;number&gt;12&lt;/number&gt;</child></text>";
        testNode(node, expected);

        node = ((new Node("node")).text("<&>'\"")).attr("attr","<&>'\"");
        expected = "<node attr=\"&lt;&amp;&gt;'&quot;\">&lt;&amp;&gt;'\"</node>";
        testNode(node,expected);

        node = (new Node("node"))
                .text("text").attr("attr1","value1","attr2","value2");
        expected = "<node attr1=\"value1\" attr2=\"value2\">text</node>";
        testNode(node, expected);

        try {
            node = (new Node("node")).text("text").attr(" attr","abcd");
            logErr(node + "\r\nFailed!");
        } catch (Exception e) {
            log(e + "\r\nOk");
        }

        try {
            node = (new Node("node")).attr("attr","value","attr","value");
            logErr(node + "\r\nFailed!");
        } catch (Exception e) {
            log(e + "\r\nOk");
        }

        try {
            node = (new Node("node")).text("text").attr("attr","ab\u007Fcd");
            logErr(node + "\r\nFailed!");
        } catch (Exception e) {
            log(e + "\r\nOk");
        }
    }
}
