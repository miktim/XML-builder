/*
 * XML builder. Node, MIT (c) 2026 miktim@mail.ru
 * Node is XML element node.
 */
package org.miktim.xml.builder;

import static java.lang.String.format;
import java.util.ArrayList;
import java.util.HashSet;

public class Node {

    Head hd = new Head();
    Node current = this;
    
    protected class Head {
        String nodeTag = null; // node name and attributes
        ArrayList<Object> nodeList = new ArrayList<>(); // text and child nodes
        HashSet<String> nsPrefs = new HashSet<>(); // declared prefixes
        HashSet<String> prefs = new HashSet<>(); // used prefixes
    }

    public Node(String nodeName) {
        hd.nodeTag = checkName(nodeName);
        String[] names = nodeName.split(":");
        if (names.length > 1) {
            hd.prefs.add(names[0]);
        }
    }

    public Node set(String nodeName) {
        Node node = new Node(nodeName);
        hd.nodeList.add(node);
        return node;
    }

    public Node add(String nodeName) {
        current = set(nodeName);
        return this;
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
        if (value.matches(".*[\u0000-\u0008\u000B\u000C\u000E-\u001F\u007F].*")) {
            throw new IllegalArgumentException("invalid XML character");
        }
        return value;
    }

    static String escapeText(String value) {
        return value
                .replaceAll("&", "&amp;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;");
    }

    public Node attr(String attrName, String value) {
        Head hd = current.hd;
        hd.nodeTag += format(" %s=\"%s\"",
                checkAttr(attrName),
                escapeText(checkChars(value)).replaceAll("\"", "&quot;"));
        String[] names = attrName.split(":");
        if (names.length > 1) {
            if (names[0].equals("xmlns")) {
                hd.nsPrefs.add(names[1]);
            } else {
                hd.prefs.add(names[0]);
            }
        }
        return this;
    }

    public Node attr(String... attrs) {
        for (int i = 0; i < attrs.length; i++) {
            attr(attrs[i], attrs[++i]);
        }
        return this;
    }

    private String checkAttr(String attrName) {
        attrName = checkName(attrName);
        if (current.hd.nodeTag.contains(format(" %s=", attrName))) {
            throw new IllegalArgumentException("duplicate attr: " + attrName);
        }
        return attrName;
    }

    public Node text(Object content) {
        if (content == null) {
            return this;
        }
        String text = escapeText(checkChars(String.valueOf(content)));
        if (text.isEmpty()) {
            return this;
        }
        current.hd.nodeList.add(text);
        return this;
    }

    public Node add(String name, Object content) {
        return add(name).text(content);
    }
    public Node set(String name, Object content) {
        return set(name).text(content);
    }
    
    public Node cdata(Object content) {
        String text = format("<![CDATA[%s]]>",
                String.valueOf(content)
                        .replaceAll("]]>", "<![CDATA[]]]><![CDATA[>]>"));
        current.hd.nodeList.add(text);
        return this;
    }

    public Node comment(String comment) {
        if (comment.contains("--") || comment.endsWith("-")) {
            throw new IllegalArgumentException("illegal comment");
        }
        hd.nodeList.add(format("<!-- %s -->", comment));
        return this;
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
