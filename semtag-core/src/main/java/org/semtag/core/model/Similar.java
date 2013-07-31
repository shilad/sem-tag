package org.semtag.core.model;

import org.semtag.SemTagException;
import org.semtag.core.dao.DaoException;
import org.semtag.core.dao.TagAppDao;

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
    public double getSimilarityTo(T other) throws DaoException;

    /**
     * Describes a method for returning a list of the most similar
     * items to this item.
     * @param maxResults the maximum amount of items to return
     * @param helperDao a helper TagAppDao used to communicate with the database
     * @return
     * @throws DaoException
     */
    public SimilarResultList getMostSimilar(int maxResults, TagAppDao helperDao) throws DaoException;
}
