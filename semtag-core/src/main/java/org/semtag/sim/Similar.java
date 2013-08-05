package org.semtag.sim;

import org.semtag.dao.DaoException;
import org.semtag.model.Tag;

/**
 * @author Ari Weiland
 */
public interface Similar<T> {

    /**
     * Returns the similarity between two instances of a specified class.
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
     * Returns a list of the most similar objects to this objects.
     * @param obj
     * @param maxResults the maximum amount of items to return
     * @return
     * @throws DaoException
     */
    public SimilarResultList mostSimilar(T obj, int maxResults) throws DaoException;

    /**
     * Returns a list of the most similar objects to this string.
     * @param tag
     * @param maxResults
     * @return
     * @throws DaoException
     */
    public SimilarResultList mostSimilar(Tag tag, int maxResults) throws DaoException;

    /**
     * Returns a symmetric cosimilarity matrix of T objects.
     * @param objs
     * @return
     * @throws DaoException
     */
    public double[][] cosimilarity(T[] objs) throws  DaoException;

    /**
     * Returns a cosimilarity matrix of T xObjs to yObjs.
     * @param xObjs
     * @param yObjs
     * @return
     * @throws DaoException
     */
    public double[][] cosimilarity(T[] xObjs, T[] yObjs) throws  DaoException;
}
