package org.semtag.relate;

import org.semtag.dao.DaoException;

/**
 * A general-purpose interface that defines how to test relatedness in SemTag
 * between two objects of a specified type. Implementations include
 * ItemRelator, TagAppRelator, and the sub-interface ConceptRelator,
 * which is implemented by WikapidiaRelator. It contains:
 * <p>
 * - relatedness method that gives a score (theoretically) between 0 and 1
 *   rating the relatedness of two objects <p>
 * - mostRelated method that returns a list of the most related objects to
 *   a specified object or tag <p>
 * - corelatedness method that returns a matrix of the relatedness scores of
 *   objects specified in the input array or arrays <p>
 *
 * @author Ari Weiland
 */
public interface Relator<T> {

    /**
     * Returns the relatedness between two instances of a specified class.
     * @param x
     * @param y
     * @return a double between 0.0 and 1.0 inclusive.
     *          1.0 suggests objects are identical. This does not necessarily
     *          imply that a call to equals(other) would return true, however.
     *          0.0 suggests objects have no relation whatsoever.
     * @throws DaoException
     */
    public double relatedness(T x, T y) throws DaoException;

    /**
     * Returns a list of the most related objects to this objects.
     * @param obj
     * @param maxResults the maximum amount of items to return
     * @return
     * @throws DaoException
     */
    public RelatedResultList mostRelated(T obj, int maxResults) throws DaoException;

    /**
     * Returns a list of the most related objects to this objects that pass the threshold.
     * @param obj
     * @param maxResults the maximum amount of items to return
     * @return
     * @throws DaoException
     */
    public RelatedResultList mostRelated(T obj, int maxResults, double threshold) throws DaoException;

    /**
     * Returns a symmetric corelatedness matrix of T objects.
     * @param objs
     * @return
     * @throws DaoException
     */
    public double[][] corelatedness(T[] objs) throws  DaoException;

    /**
     * Returns an asymmetric corelatedness matrix of T xObjs to yObjs.
     * @param xObjs
     * @param yObjs
     * @return
     * @throws DaoException
     */
    public double[][] corelatedness(T[] xObjs, T[] yObjs) throws  DaoException;
}
