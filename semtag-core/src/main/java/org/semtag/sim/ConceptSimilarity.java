package org.semtag.sim;

import org.semtag.dao.DaoException;
import org.semtag.model.concept.Concept;

/**
 * @author Ari Weiland
 */
public interface ConceptSimilarity extends Similar<Concept> {

    public double similarity(int xId, int yId) throws DaoException;

    public SimilarResultList mostSimilar(int id, int maxResults) throws DaoException;

    /**
     * Returns a cosimilarity matrix of concept IDs ids.
     * @param ids
     * @return
     * @throws DaoException
     */
    public double[][] cosimilarity(int[] ids) throws DaoException;

    /**
     * Returns a cosimilarity matrix of concept IDs xIds to yIds.
     * @param xIds
     * @param yIds
     * @return
     * @throws DaoException
     */
    public double[][] cosimilarity(int[] xIds, int[] yIds) throws DaoException;
}
