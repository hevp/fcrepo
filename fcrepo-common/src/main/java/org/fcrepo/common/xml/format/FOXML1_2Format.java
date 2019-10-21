/* The contents of this file are subject to the license and copyright terms
 * detailed in the license directory at the root of the source tree (also
 * available online at http://fedora-commons.org/license/).
 */

package org.fcrepo.common.xml.format;

import org.fcrepo.common.xml.namespace.FOXMLNamespace;

/**
 * The FOXML 1.2 XML format.
 *
 * <pre>
 * Format URI        : info:fedora/fedora-system:FOXML-1.2
 * Primary Namespace : info:fedora/fedora-system:def/foxml#
 * XSD Schema URL    : http://repository.surfsara.nl/api/static/xsd/foxml1-2.xsd
 * </pre>
 *
 * @author hevp
 */
public class FOXML1_2Format
        extends XMLFormat {

    /** The only instance of this class. */
    private static final FOXML1_2Format ONLY_INSTANCE = new FOXML1_2Format();

    /**
     * Constructs the instance.
     */
    private FOXML1_2Format() {
        super("info:fedora/fedora-system:FOXML-1.2",
              FOXMLNamespace.getInstance(),
              "http://repository.surfsara.nl/api/static/xsd/foxml1-2.xsd");
    }

    /**
     * Gets the only instance of this class.
     *
     * @return the instance.
     */
    public static FOXML1_2Format getInstance() {
        return ONLY_INSTANCE;
    }

}
