package org.semtag.sim;

import org.semtag.dao.DaoException;
import org.semtag.model.concept.Concept;

/**
 * This interface extends the Similar interface for concepts. It adds a few
 * additional methods that duplicate the Similar methods with concept IDs.
 *
 * @author Ari Weiland
 */
public interface ConceptSimilarity extends Similar<Concept> {

    /**
     * Returns the similarity between two concepts specified by IDs.
     * @param xId
     * @param yId
     * @return
     * @throws DaoException
     */
    public double similarity(int xId, int yId) throws DaoException;

    /**
     * Returns a list of the most similar objects to the concept specified by the ID.
     * @param id
     * @param maxResults
     * @return
     * @throws DaoException
     */
    public SimilarResultList mostSimilar(int id, int maxResults) throws DaoException;

    /**
     * Returns a cosimilarity matrix of concept IDs ids.
     * @param ids
     * @return
     * @throws DaoException
     */
    public double[][] cosimilarity(int[] ids) throws DaoException;

    /**
     * Returns an asymmetric cosimilarity matrix of concept IDs xIds to yIds.
     * @param xIds
     * @param yIds
     * @return
     * @throws DaoException
     */
    public double[][] cosimilarity(int[] xIds, int[] yIds) throws DaoException;
}
