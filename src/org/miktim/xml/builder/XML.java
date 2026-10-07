/*
 * XMLgen XML, MIT (c) 2026 miktim@mail.ru
 */
package org.miktim.xml.builder;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import static java.lang.String.format;
import java.nio.charset.Charset;
import java.util.HashSet;
import javax.xml.bind.DataBindingException;

public class XML extends Node {

    public XML(Node rootNode) {
        hd = rootNode.hd;
//        nodeTag = rootNode.nodeTag;
//        nodeList = rootNode.nodeList;
    }

    public static final String NAME_PATTERN
            = "([_\\p{L}][._\\p{L}0-9]*)(:([_\\p{L}][._\\p{L}0-9]*))*";

    @Override
    public String toString() {
        checkPrefs(this, new HashSet<String>());
        return getDeclaration(Charset.defaultCharset().toString())
                + super.toString();
    }

    public String toString(String charset) throws UnsupportedEncodingException {
        checkPrefs(this, new HashSet<String>());
        String xml = getDeclaration(charset) + super.toString();
        return new String(xml.getBytes(charset));
    }

    public void toStream(OutputStream out) throws IOException {
        toStream(out, Charset.defaultCharset().toString());
    }

    public void toStream(OutputStream out, String charset) throws IOException {
        checkPrefs(this, new HashSet<String>());
        byte[] bytes = 
                (getDeclaration(charset) + super.toString()).getBytes(charset);
        out.write(bytes);
        out.flush();
        out.close();
    }

    private String getDeclaration(String charset) {
        return format("<?xml version=\"1.0\" encoding=\"%s\"?>\n", charset);
    }

// checks prefixes binding    
    private void checkPrefs(Node node, HashSet<String> nsPrefs) {
        HashSet<String> thisNsPrefs = new HashSet<>(nsPrefs); // keep nsPrefs
        thisNsPrefs.addAll(node.hd.nsPrefs);
        for(String pref : node.hd.prefs) {
            if(!thisNsPrefs.contains(pref))
// The prefix "R" for element "R:author" is not bound.
                throw new DataBindingException("prefix is not bound: " + pref, null);
        }
// scan nodeList
        for(Object entry : node.hd.nodeList) {
            if(entry instanceof Node)
                checkPrefs((Node)entry, thisNsPrefs);
        }
    }
}
