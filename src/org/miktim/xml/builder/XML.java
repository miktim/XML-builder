/*
 * XML builder. XML, MIT (c) 2026 miktim@mail.ru
 */
package org.miktim.xml.builder;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import static java.lang.String.format;
import java.nio.charset.Charset;
import java.util.HashSet;

public class XML extends Node {

    public XML(String nodeName) {
        super(nodeName);
    }

    public static final String NAME_PATTERN
            = "([_\\p{L}][._\\p{L}0-9]*)(:([_\\p{L}][._\\p{L}0-9]*))*";
    
    @Override
    public String toString() {
        checkPrefs(this, new HashSet<String>());
        return getDeclaration(Charset.defaultCharset().toString())
                + super.toString();
    }

    public String toString(String charsetName) throws UnsupportedEncodingException {
        checkPrefs(this, new HashSet<String>());
        String xml = getDeclaration(charsetName) + super.toString();
        return new String(xml.getBytes(charsetName));
    }

    public void toStream(OutputStream out) throws IOException {
//        toStream(out, Charset.defaultCharset().toString());
        toStream(out, "UTF-8");
    }

    public void toStream(OutputStream out, String charsetName) throws IOException {
        checkPrefs(this, new HashSet<String>());
        byte[] bytes
                = (getDeclaration(charsetName) + super.toString()).getBytes(charsetName);
        out.write(bytes);
        out.flush();
//        out.close();
    }

    private String getDeclaration(String charset) {
        return format("<?xml version=\"1.0\" encoding=\"%s\"?>\n", charset);
    }

// checks prefixes binding    
    private void checkPrefs(Node node, HashSet<String> nsPrefs) {
        HashSet<String> thisNsPrefs = new HashSet<>(nsPrefs); // keep nsPrefs
        thisNsPrefs.addAll(node.hd.nsPrefs);
        for (String pref : node.hd.prefs) {
            if (!thisNsPrefs.contains(pref)) {
                throw new IllegalArgumentException(
                        format("prefix \"%s\" with an element \"%s\" is not bound",
                                 pref, node.hd.nodeTag.split(" ")[0]));
            }
        }
// scan nodeList
        for (Object entry : node.hd.nodeList) {
            if (entry instanceof Node) {
                checkPrefs((Node) entry, thisNsPrefs);
            }
        }
    }
}
