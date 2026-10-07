/*
 * XML builder Node, MIT (c) 2026 miktim@mail.ru
 */
package org.miktim.xml.builder;

import static java.lang.String.format;
import java.util.ArrayList;
import java.util.HashSet;

public class Node {
    Head hd = new Head();
    
    protected class Head {
        String nodeTag = null;
        ArrayList<Object> nodeList = new ArrayList<>(); // not Thread-safe
        HashSet<String> nsPrefs = new HashSet<>(); // declared prefixes
        HashSet<String> prefs = new HashSet<>(); // used prefixes
    }

    protected Node() {

    }

    public Node(String nodeName) {
        hd.nodeTag = checkName(nodeName);
        String[] names = nodeName.split(":");
        if(names.length > 1)
            hd.prefs.add(names[0]);
    }

    public Node(String nodeName, Object content) {
        this(nodeName);
        if (content == null) {
            return;
        }
        String text = checkChars(String.valueOf(content));
//        if (content instanceof String) {
            if (text.isEmpty()) {
                return;
            }
            if(!isCDATA(text)){ 
                text = escape(text);
            }
//        }
        hd.nodeList.add(text);
    }
    
    private static String NAME_PATTERN = format("^%s$", XML.NAME_PATTERN);

    private static String checkName(String name) {
        if (!name.matches(NAME_PATTERN)) {
            throw new IllegalArgumentException("illegal name: " + name);
        }
//        for(String nm : name.toLowerCase().split(":"))
//            if(!nm.equals("xmlns") && nm.startsWith("xml"))
//                throw new IllegalArgumentException("illegal name: " + name);
        return name;
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
        hd.nodeList.add(node);
        return node;
    }

    public Node addNode(Node node) {
        if (node == null) {
            throw new NullPointerException("node");
        }
        node = dereferenceXml(node);
        hd.nodeList.add(node);
        return this;
    }

    static Node dereferenceXml(Node node) {
        if (node instanceof XML) {
            Node newNode = new Node();
            newNode.hd = node.hd;
            return newNode;
        }
        return node;
    }
    
    public Node addAttr(String attrName, String value) {
        hd.nodeTag += format(" %s=\"%s\"",
                checkAttr(attrName),
                escape(checkChars(value)).replaceAll("\"", "&quot;"));
        String[] names = attrName.split(":");
        if(names.length > 1) {
            if(names[0].equals("xmlns"))
                hd.nsPrefs.add(names[1]);
            else
                hd.prefs.add(names[0]);
        }
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
        if(!hd.nodeTag.contains(format(" %s=", attrName)))
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
        hd.nodeList.add(format("<!-- %s -->",comment));
        return this;
    }
/*    
    public Node addDoctype(String doctype) {
        nodeList.add(format("<!DOCTYPE %s>",doctype));
        return this;
    }
*/
    @Override
    public Node clone() {
        Node newNode = new Node();
        newNode.hd.nodeTag = hd.nodeTag;
        newNode.hd.nodeList = new ArrayList<Object>(hd.nodeList);
        newNode.hd.nsPrefs = new HashSet<String>(hd.nsPrefs);
        newNode.hd.prefs = new HashSet<String>(hd.prefs);
        return newNode;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String endTag = "";
        if (hd.nodeList.isEmpty()) {
            sb.append(format("<%s/>", hd.nodeTag));
        } else {
            sb.append(format("<%s>", hd.nodeTag));
            endTag = format("</%s>", hd.nodeTag.split(" ", 2)[0]);
        }
        for (Object node : hd.nodeList) {
            sb.append(node.toString());
        }
        sb.append(endTag);
        return sb.toString();
    }
}
