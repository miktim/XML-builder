/*
 * XMLgen Node, MIT (c) 2026 miktim@mail.ru
 */
package org.miktim.xmlgen;

import static java.lang.String.format;
import java.util.ArrayList;

public class Node {

    String nodeTag = null;
    ArrayList<Object> nodeList = new ArrayList<>(); // not Thread-safe

    protected Node() {

    }

    public Node(String nodeName) {
        this.nodeTag = checkName(nodeName);
    }

    public Node(String nodeName, Object content) {
        this(nodeName);
        if (content == null) {
            return;
        }
        String text = checkChars(String.valueOf(content));
        if (content instanceof String) {
            if (text.isEmpty()) {
                return;
            }
            if(!isCDATA(text)){ 
                text = escape(text);
            }
        }
        nodeList.add(text);
    }
    
    private static String NAME_PATTERN = format("^%s$", XML.NAME_PATTERN);

    private static String checkName(String name) {
        if (name.matches(NAME_PATTERN)) {
            return name;
        }
        throw new IllegalArgumentException("illegal name: " + name);
    }
    
    private static String checkChars(String value) {
//        if(value.matches(".*[\u0000-\u0008\u000B\u000C\u000E-\u001F\u007F-\u0084\u0086-\u009F].*"))
        if(value.matches(".*[\u0000-\u0008\u000B\u000C\u000E-\u001F\u007F].*"))
            throw new IllegalArgumentException("invalid XML character");
        return value;
    }

    public static String CDATA(Object content) {
        return format("<![CDATA[%s]]>", 
                String.valueOf(content)
                        .replaceAll("]]>", "<![CDATA[]]]><![CDATA[>]>"));
    }

    private boolean isCDATA(String content) {
        return content.startsWith("<![CDATA[") && content.endsWith("]]>");
    }

    public static String escape(String value) {
        return value
                .replaceAll("&", "&amp;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;");
    }

    public final Node setNode(Node node) {
        if (node == null) {
            throw new NullPointerException("node");
        }
        node = dereferenceXml(node);
        nodeList.add(node);
        return node;
    }

    public Node addNode(Node node) {
        if (node == null) {
            throw new NullPointerException("node");
        }
        node = dereferenceXml(node);
        nodeList.add(node);
        return this;
    }

    static Node dereferenceXml(Node node) {
        if (node instanceof XML) {
            Node newNode = new Node();
            newNode.nodeTag = node.nodeTag;
            newNode.nodeList = node.nodeList;
            return newNode;
        }
        return node;
    }
    
    public Node addAttr(String attrName, String value) {
            nodeTag += format(" %s=\"%s\"",
                    checkAttr(attrName),
                    escape(checkChars(value)).replaceAll("\"", "&quot;"));
        return this;
    }
    
    public Node addAttr(String... attrs) {
        for (int i = 0; i < attrs.length; i++) {
            addAttr(attrs[i], attrs[++i]);
        }
        return this;
    }

    private String checkAttr(String attrName) {
        attrName = checkName(attrName);
        if(!nodeTag.contains(format(" %s=", attrName)))
            return attrName;
        throw new IllegalArgumentException("duplicate attr: " + attrName);
    }

    public Node setNode(String nodeName) {
        Node node = new Node(nodeName);
        return setNode(node);
    }

    public Node addNode(String nodeName) {
        Node node = new Node(nodeName);
        return addNode(node);
    }

    public Node addNode(String nodeName, Object content) {
        Node node = new Node(nodeName, content);
        return addNode(node);
    }

    public Node setNode(String nodeName, Object content) {
        Node node = new Node(nodeName, content);
        return setNode(node);
    }

    public Node addComment(String comment) {
        if(comment.contains("--") || comment.endsWith("-"))
            throw new IllegalArgumentException("illegal comment");
        nodeList.add(format("<!-- %s -->",comment));
        return this;
    }
/*    
    public Node addDoctype(String doctype) {
        nodeList.add(format("<!DOCTYPE %s>",doctype));
        return this;
    }
*/

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String endTag = "";
        if (nodeList.isEmpty()) {
            sb.append(format("<%s/>", nodeTag));
        } else {
            sb.append(format("<%s>", nodeTag));
            endTag = format("</%s>", nodeTag.split(" ", 2)[0]);
        }
        for (Object node : nodeList) {
            sb.append(node.toString());
        }
        sb.append(endTag);
        return sb.toString();
    }
}
