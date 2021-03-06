/* The contents of this file are subject to the license and copyright terms
 * detailed in the license directory at the root of the source tree (also
 * available online at http://fedora-commons.org/license/).
 */
package org.fcrepo.server;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiValueMap<T> {

    private static final Logger logger =
            LoggerFactory.getLogger(MultiValueMap.class);

    private static final String [] EMPTY_STRING_ARRAY = new String[0];

    private boolean m_locked = false;

    private final Map<T,String[]> attributes = new HashMap<T,String[]>();

    /**
     * Creates and initializes the <code>WritableContext</code>.
     * <p>
     * </p>
     * A pre-loaded Map of name-value pairs comprising the context.
     */
    public MultiValueMap() {
    }

    public T setReturn(T name, String value)
            throws IllegalArgumentException, IllegalStateException {
        set(name, value);
        return name;
    }

    public void set(T name, String value)
            throws IllegalArgumentException, IllegalStateException {
        audit(name, value);
        if (value != null) {
            String [] temp = attributes.get(name);
            if (temp == null || temp.length != 1) {
                attributes.put(name, new String[]{value});
            } else temp[0] = value;
        } else {
            attributes.put(name, EMPTY_STRING_ARRAY);
        }
    }

    public T setReturn(T name, String[] value)
            throws IllegalArgumentException, IllegalStateException {
        set(name, value);
        return name;
    }

    public void set(T name, String[] value)
            throws IllegalArgumentException, IllegalStateException {
        audit(name, value);
        if (value != null) {
            attributes.put(name, value);
        } else {
            attributes.put(name, EMPTY_STRING_ARRAY);
        }
    }

    public void lock() {
        m_locked = true;
    }

    public Iterator<T> names() {
        return attributes.keySet().iterator();
    }

    public int length(T name) {
        if (attributes.get(name) != null) {
            return attributes.get(name).length;
        } else {
            return 0;
        }
    }

    /**
     * Returns the first (or only) value for an attribute
     * @param name
     * @return first available value
     */
    public String getString(T name) {
         return (attributes.containsKey(name)) ? attributes.get(name)[0] : null;
    }

    public String[] getStringArray(T name) {
        return attributes.get(name);
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder(128*attributes.size());
        Iterator<T> it = attributes.keySet().iterator();
        while (it.hasNext()) {
            T key = it.next();
            buffer.append(key.toString() + "=[");
            if (attributes.get(key) != null) {
                String[] temp = attributes.get(key);
                boolean second = false;
                for (String element : temp) {
                    if (second) buffer.append(',');
                    buffer.append(element);
                    second |= true;
                }
            }
            buffer.append("]\n");
        }
        return buffer.toString();
    }

    /**
     * Test whether this map is equal to another similar one. We can't just test
     * for equality of the underlying maps, since they may contain arrays of
     * Strings as values, and those arrays are only equal if identical.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MultiValueMap)) {
            return false;
        }
        @SuppressWarnings("unchecked")
        MultiValueMap<T> that = (MultiValueMap<T>) obj;

        return m_locked == that.m_locked && equalMaps(attributes, that.attributes);
    }

    private boolean equalMaps(Map<T,String[]> thisMap, Map<T,String[]> thatMap) {

        /* Check for obvious differences (same number and value of keys) */
        if (!thisMap.keySet().equals(thatMap.keySet())) {
            return false;
        }

        Iterator<T> theseKeys = thisMap.keySet().iterator();

        /* Now do a deep compare of contents.. */
        while (theseKeys.hasNext()) {
            T key = theseKeys.next();
            if (!Arrays.equals(thisMap.get(key), thatMap.get(key))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        return attributes.hashCode() + (m_locked ? 1 : 0);
    }

    public static <S> MultiValueMap<S> empty(Class<S> klazz) {
        MultiValueMap<S> result = new MultiValueMap<S>();
        result.lock();
        return result;
    }

    protected static final String here = "MultiValueMap";

    private void audit(T key, Object value)
            throws IllegalArgumentException, IllegalStateException {
        if (key == null) {
            String msg = "{}: set() has null name, value={}";
            logger.debug(msg, here, value);
            throw new IllegalArgumentException(msg);
        }
        if (m_locked) {
            String msg = "{}: set() has object locked";
            logger.debug(msg, here);
            throw new IllegalStateException(msg);
        }
    }

}
