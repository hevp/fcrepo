/* The contents of this file are subject to the license and copyright terms
 * detailed in the license directory at the root of the source tree (also
 * available online at http://fedora-commons.org/license/).
 */
package org.fcrepo.server.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.SimpleArrayMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import org.fcrepo.common.Constants;
import org.fcrepo.common.rdf.RDFName;
import org.fcrepo.server.errors.ObjectIntegrityException;
import org.fcrepo.server.errors.RepositoryConfigurationException;
import org.fcrepo.server.errors.StreamIOException;
import org.fcrepo.utilities.ReadableCharArrayWriter;
import org.fcrepo.utilities.XmlTransformUtility;

import javax.xml.XMLConstants;

/**
 * DataCite Fields.
 *
 * @author hevp
 * @version $Id$
 */
public class DataCiteFields
        extends DefaultHandler
        implements Constants {

    private static final char[] XML_OPEN = xmlOpen();
    private static final char[] XML_CLOSE = "</resource>\n".toCharArray();

    private Map<RDFName, DataCiteField> m_fields = null;

    private StringBuffer m_currentContent;

    public DataCiteFields() {}

    public DataCiteFields(InputStream in)
            throws RepositoryConfigurationException, ObjectIntegrityException,
            StreamIOException {
        try {
            XmlTransformUtility.parseWithoutValidating(in, this);
        } catch (SAXException saxe) {
            throw new ObjectIntegrityException("Parse error parsing DataCite XML Metadata: "
                    + saxe.getMessage());
        } catch (IOException ioe) {
            throw new StreamIOException("Stream error parsing DataCite XML Metadata: "
                    + ioe.getMessage());
        }
    }

    @Override
    public void startElement(String uri,
                             String localName,
                             String qName,
                             Attributes attrs) {
        m_currentContent = new StringBuffer();
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        m_currentContent.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        m_fields[localName].add(new DataCiteField())
    }

    /**
     * Returns a Map with RDFName keys, each value containing List of
     * values for that field.
     */
    public Map<RDFName, List<DataCiteField>> getMap() {
        Map<RDFName, List<DataCiteField>> map = new HashMap<RDFName, List<DataCiteField>>(15);

        for (Map.Entry<RDFName, List<DataCiteField>> f : m_fields) {
            map.put(f.getKey(), f.getValue());
        }

        return map;
    }

    /**
     * Get the DataCiteFields as a String in namespace-qualified XML form, matching
     * the datacite schema.... but without the xml declaration.
     */
    public String getAsXML() {
        return getAsXML((String)null);
    }

    public void getAsXML(Writer out) throws IOException {
        getAsXML(null, out);
    }

    /**
     * Ensure the identifier include the pid of the target object
            * @param targetPid
            * @return String xml
     */
    public String getAsXML(String targetPid) {
        ReadableCharArrayWriter out = new ReadableCharArrayWriter(512);
        try {
            getAsXML(targetPid, out);
        } catch (IOException wonthappen) {
            throw new RuntimeException(wonthappen);
        }
        return out.getString();
    }

    public void getAsXML(String targetPid, Writer out) throws IOException {
        out.write(XML_OPEN);
        for (Map.Entry<RDFName, ArrayList<DataCiteField>> f : m_fields) {
            appendXML(f.getValue(), f.getKey().toCharArray(), out);
        }
        out.write(XML_CLOSE);
    }

    private void appendXML(ArrayList<DataCiteField> values, char[] name, Writer out)
        throws IOException {
        if (values == null || values.size() == 0) return;
        for (DataCiteField f : values) {
            appendXML(f, name, out);
        }
    }

    private void appendXML(DataCiteField value, char[] name, Writer out) throws IOException {
        out.write("<" + name.toString() + ">");
        StreamUtility.enc(value.getValue(), out);
        out.write("</" + name.toString() + ">\n");
    }

    private static final char[] xmlOpen() {
        return ("<" + DATACITE.prefix + " xmlns:" + XSI.prefix + "=\"" + XSI.uri
                + " xmlns=\"" + DATACITE.uri + "\" " + XSI.prefix + ":schemaLocation=\"" + DATACITE.uri
                + " " + DATACITE.xsdLocation + "\">\n").toCharArray();
    }
}
