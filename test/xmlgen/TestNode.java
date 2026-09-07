/*
 * XMLgen. Node test, MIT (c) 2026 miktim@mail.ru
 */

package xmlgen;

import org.miktim.xmlgen.Node;

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

        node = new Node("node",null);
        expected = "<node/>";
        testNode(node, expected);
        node = new Node("node","");
        expected = "<node/>";
        testNode(node, expected);
        node = new Node("node", "    ");
        expected = "<node>    </node>";
        testNode(node, expected);

        node = new Node("node", "text");
        expected = "<node>text</node>";
        testNode(node, expected);
        node.setNode("child","text");
        expected = "<node>text<child>text</child></node>";
        testNode(node, expected);
        node.setNode("boolean",false);
        expected = "<node>text<child>text</child><boolean>false</boolean></node>";
        testNode(node, expected);

        node = new Node("node", "text");
        node.setNode("child","text")
                .addNode("number",12);
        expected = "<node>text<child>text<number>12</number></child></node>";
        testNode(node, expected);

        node = new Node("node", "text");
        node.setNode("child",new Node("number",12));
        expected = "<node>text<child><number>12</number></child></node>";
        testNode(node, expected);

        node = (new Node("node", "<&>'\"")).addAttr("attr","<&>'\"");
        expected = "<node attr=\"&lt;&amp;&gt;'&quot;\">&lt;&amp;&gt;'\"</node>";
        testNode(node,expected);

        try {
            node = new Node("node","text").addAttr(" attr","abcd");
            logErr(node + "\r\nFailed!");
        } catch (Exception e) {
            log(e + "\r\nOk");
        }

        try {
            node = (new Node("node")).addAttr("attr","value","attr","value");
            logErr(node + "\r\nFailed!");
        } catch (Exception e) {
            log(e + "\r\nOk");
        }

        try {
            node = new Node("node","text").addAttr("attr","ab\u007Fcd");
            logErr(node + "\r\nFailed!");
        } catch (Exception e) {
            log(e + "\r\nOk");
        }
    }
}
