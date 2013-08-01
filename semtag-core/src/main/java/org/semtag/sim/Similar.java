package org.semtag.sim;

import org.semtag.dao.DaoException;

/**
 * @author Ari Weiland
 */
public interface Similar<T> {

    /**
     * Describes a method for returning the similarity between two
     * instances of a specified class.
     *
     * @param x
     * @param y
     * @return a double between 0.0 and 1.0 inclusive.
     *          1.0 suggests objects are identical. This does not necessarily
     *          imply that a call to equals(other) would return true, however.
     *          0.0 suggests objects have no relation whatsoever.
     * @throws DaoException
     */
    public double similarity(T x, T y) throws DaoException;

    /**
     * Describes a method for returning a list of the most similar
     * items to this item.
     * @param obj
     * @param maxResults the maximum amount of items to return
     * @return
     * @throws DaoException
     */
    public SimilarResultList mostSimilar(T obj, int maxResults) throws DaoException;

    /**
     * Returns a symmetric cosimilarity matrix of T objects.
     * @param objs
     * @return
     * @throws DaoException
     */
    public double[][] cosimilarity(T[] objs) throws  DaoException;
}
