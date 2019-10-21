/* The contents of this file are subject to the license and copyright terms
 * detailed in the license directory at the root of the source tree (also
 * available online at http://fedora-commons.org/license/).
 */
package org.fcrepo.server.utilities;

/**
 * DataCite Field base
 *
 * @author hevp
 * @since 3.9
 * @version $Id$
 */
public class DataCiteField {
    protected final ArrayList attributes;

    public DataCiteField(ArrayList attributes) {
        this(attributes, null);
    }

    public ArrayList getValues() {
        return new ArrayList<DataCiteField>()
    }

    public ArrayList getAttributes() {
        return attributes;
    }
}

/**
 * DataCite simple string field
 *
 * @author hevp
 * @since 3.9
 * @version $Id$
 */
public class DataCiteSimpleField
    extends DataCiteField {
    private final String value;

    public DataCiteSimpleField(String value, ArrayList attributes) {
        super(null);
        this(value, null);
    }

    public DataCiteSimpleField(String value) {
        this(value, null);
    }

    public ArrayList getValue() {
        return value;
    }
}

/**
 * DataCite complex field
 *
 * @author hevp
 * @since 3.9
 * @version $Id$
 */
public class DataCiteComplexField
    extends DataCiteField {
    private final ArrayList<DataCiteSimpleField> values;

    public DataCiteComplexField(ArrayList<DataCiteSimpleField> values) {
        super(null);
        this(values, null);
    }

    public DataCiteComplexField(ArrayList<DataCiteSimpleField> values) {
        this(values, null);
    }

    public ArrayList<DataCiteSimpleField> getValues() {
        return values;
    }

    public ArrayList getValue() {
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s.getValue() + " ");
        }

        return sb.toString();
    }
}
