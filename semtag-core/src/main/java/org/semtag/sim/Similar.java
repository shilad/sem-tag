package org.semtag.sim;

import org.semtag.dao.DaoException;

/**
 * A general-purpose interface that defines how to test similarity in SemTag
 * between two objects of a specified type. Implementations include
 * ItemSimilar, TagAppSimilar, and the sub-interface ConceptSimilar,
 * which is implemented by WikapidiaSimilar. It contains:
 * <p>
 * - similarity method that gives a score (theoretically) between 0 and 1
 *   rating the similarity of two objects <p>
 * - mostSimilar method that returns a list of the most similar objects to
 *   a specified object or tag <p>
 * - cosimilarity method that returns a matrix of the similarity scores of
 *   objects specified in the input array or arrays <p>
 *
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
     * Returns a list of the most similar objects to this objects that pass the threshold.
     * @param obj
     * @param maxResults the maximum amount of items to return
     * @return
     * @throws DaoException
     */
    public SimilarResultList mostSimilar(T obj, int maxResults, double threshold) throws DaoException;

    /**
     * Returns a symmetric cosimilarity matrix of T objects.
     * @param objs
     * @return
     * @throws DaoException
     */
    public double[][] cosimilarity(T[] objs) throws  DaoException;

    /**
     * Returns an asymmetric cosimilarity matrix of T xObjs to yObjs.
     * @param xObjs
     * @param yObjs
     * @return
     * @throws DaoException
     */
    public double[][] cosimilarity(T[] xObjs, T[] yObjs) throws  DaoException;
}
