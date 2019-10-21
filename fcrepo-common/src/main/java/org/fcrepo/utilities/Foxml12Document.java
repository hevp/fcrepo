/* The contents of this file are subject to the license and copyright terms
 * detailed in the license directory at the root of the source tree (also
 * available online at http://fedora-commons.org/license/).
 */
package org.fcrepo.utilities;

import java.io.OutputStream;
import java.io.StringReader;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import org.fcrepo.utilities.Foxml11Document;

/**
 * A DOM-based utility for generating FOXML 1.2 documents.
 *
 * @author Edwin Shin
 * @author hevp
 * @since 3.9
 * @version $Id$
 */
public class Foxml12Document
    extends Foxml11Document {

    public enum Property {
        STATE("info:fedora/fedora-system:def/model#state"),
        SHARELEVEL("info:fedora/fedora-system:def/model#shareLevel"),
        LABEL("info:fedora/fedora-system:def/model#label"),
        CONTENT_MODEL("info:fedora/fedora-system:def/model#contentModel"),
        CREATE_DATE("info:fedora/fedora-system:def/model#createdDate"),
        MOD_DATE("info:fedora/fedora-system:def/view#lastModifiedDate");

        private final String uri;

        Property(String uri) {
            this.uri = uri;
        }

        String uri() {
            return uri;
        }
    }

    public enum ShareLevel {
        O, R, P;
    }

    public Foxml12Document(String pid) throws Exception {
        DocumentBuilder builder = null;
        try {
            builder = XmlTransformUtility.borrowDocumentBuilder();
            DOMImplementation impl = builder.getDOMImplementation();
            doc = impl.createDocument(FOXML_NS, "foxml:digitalObject", null);
            rootElement = doc.getDocumentElement();
            rootElement.setAttributeNS("http://www.w3.org/2000/xmlns/",
                                "xmlns:xsi",
                                "http://www.w3.org/1999/XMLSchema-instance");
            rootElement.setAttributeNS("http://www.w3.org/1999/XMLSchema-instance",
                                       "xsi:schemaLocation",
                                       "info:fedora/fedora-system:def/foxml# http://repository.surfsara.nl/api/static/xsd/foxml1-2.xsd");
            rootElement.setAttribute("VERSION", "1.2");
            rootElement.setAttribute("PID", pid);
        } finally {
            if (builder != null) {
                XmlTransformUtility.returnDocumentBuilder(builder);
            }
        }

        NamespaceContextImpl nsCtx = new NamespaceContextImpl();
        nsCtx.addNamespace("foxml", FOXML_NS);
        XPathFactory factory = XPathFactory.newInstance();
        xpath = factory.newXPath();
        xpath.setNamespaceContext(nsCtx);
    }
}
