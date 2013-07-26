package org.semtag.core.model;

import org.semtag.SemTagException;

/**
 * @author Ari Weiland
 */
public interface Similar<T> {

    /**
     * Describes a method for returning the similarity between two
     * instances of a specified class.
     * @param other
     * @return a double between 0.0 and 1.0 inclusive.
     *          1.0 suggests objects are identical. This does not necessarily
     *          imply that a call to equals(other) would return true, however.
     *          0.0 suggests objects have no relation whatsoever.
     * @throws SemTagException
     */
    public double getSimilarityTo(T other) throws SemTagException;
}
