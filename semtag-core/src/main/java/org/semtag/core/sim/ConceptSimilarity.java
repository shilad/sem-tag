package org.semtag.core.sim;

import org.semtag.core.dao.DaoException;
import org.semtag.core.model.concept.Concept;

/**
 * @author Ari Weiland
 */
public interface ConceptSimilarity<T extends Concept> extends Similar<T> {

    public double similarity(int xId, int yId) throws DaoException;

    public SimilarResultList mostSimilar(int id, int maxResults) throws DaoException;

    public double[][] cosimilarity(int[] ids) throws DaoException;
}
