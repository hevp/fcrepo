/* The contents of this file are subject to the license and copyright terms
 * detailed in the license directory at the root of the source tree (also
 * available online at http://fedora-commons.org/license/).
 */
package org.fcrepo.common.rdf;

/**
 * The DataCite RDF namespace.
 *
 * <pre>
 * Namespace URI    : https://schema.datacite.org/
 * Preferred Prefix : resource
 * </pre>
 *
 * @author hevp
 */
public class DataCiteNamespace
        extends RDFNamespace {

    private static final long serialVersionUID = 1L;
    public String xsdLocation;

    /* Required fields */
    public final RDFName IDENTIFIER;

    public final RDFName CREATORS;

    public final RDFName TITLES;

    public final RDFName PUBLISHER;

    public final RDFName PUBLICATION_YEAR;

    public final RDFName RESOURCE_TYPE;

    /* Optional fields */
    public final RDFName SUBJECTS;

    public final RDFName CONTRIBUTORS;

    public final RDFName DATES;

    public final RDFName LANGUAGE;

    public final RDFName ALTERNATE_IDENTIFIERS;

    public final RDFName RELATED_IDENTIFIERS;

    public final RDFName SIZES;

    public final RDFName FORMATS;

    public final RDFName VERSION;

    public final RDFName RIGHTS_LIST;

    public final RDFName DESCRIPTIONS;

    public final RDFName GEO_LOCATIONS;

    public final RDFName FUNDING_REFERENCES;

    public DataCiteNamespace() {

        uri = "http://datacite.org/schema/kernel-4";
        prefix = "resource";
        xsdLocation = "http://schema.datacite.org/meta/kernel-4.2/metadata.xsd";

        IDENTIFIER = new RDFName(this, "identifier");
        CREATORS = new RDFName(this, "creators");
        TITLES = new RDFName(this, "titles");
        PUBLISHER = new RDFName(this, "publisher");
        PUBLICATION_YEAR = new RDFName(this, "publication_year");
        RESOURCE_TYPE = new RDFName(this, "resource_type");

        SUBJECTS = new RDFName(this, "subjects");
        CONTRIBUTORS = new RDFName(this, "contributors");
        DATES = new RDFName(this, "dates");
        LANGUAGE = new RDFName(this, "language");
        ALTERNATE_IDENTIFIERS = new RDFName(this, "alternate_identifiers");
        RELATED_IDENTIFIERS = new RDFName(this, "related_identifiers");
        SIZES = new RDFName(this, "sizes");
        FORMATS = new RDFName(this, "formats");
        VERSION = new RDFName(this, "version");
        RIGHTS_LIST = new RDFName(this, "rights_list");
        DESCRIPTIONS = new RDFName(this, "descriptions");
        GEO_LOCATIONS = new RDFName(this, "geo_locations");
        FUNDING_REFERENCES = new RDFName(this, "funding_references");
    }

}
