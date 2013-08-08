package org.semtag.relate;

import org.semtag.dao.DaoException;
import org.semtag.model.Tag;
import org.semtag.model.concept.Concept;

/**
 * This interface extends the Relator interface for concepts. It adds a few
 * additional methods that duplicate the Relator methods with concept IDs.
 *
 * @author Ari Weiland
 */
public interface ConceptRelator extends Relator<Concept> {

    /**
     * Returns the relatedness between two concepts specified by IDs.
     * @param xId
     * @param yId
     * @return
     * @throws DaoException
     */
    public double relatedness(int xId, int yId) throws DaoException;

    /**
     * Returns a list of the most related objects to the concept specified by the ID.
     * @param id
     * @param maxResults
     * @return
     * @throws DaoException
     */
    public RelatedResultList mostRelated(int id, int maxResults) throws DaoException;

    /**
     * Returns a list of the most related objects to the concept specified by the ID.
     * @param id
     * @param maxResults
     * @return
     * @throws DaoException
     */
    public RelatedResultList mostRelated(int id, int maxResults, double threshold) throws DaoException;

    /**
     * Returns a list of the most related concepts to this tag.
     * @param tag
     * @param maxResults
     * @return
     * @throws DaoException
     */
    public RelatedResultList mostRelated(Tag tag, int maxResults) throws DaoException;

    /**
     * Returns a list of the most related concepts to this tag.
     * @param tag
     * @param maxResults
     * @return
     * @throws DaoException
     */
    public RelatedResultList mostRelated(Tag tag, int maxResults, double threshold) throws DaoException;

    /**
     * Returns a corelatedness matrix of concept IDs ids.
     * @param ids
     * @return
     * @throws DaoException
     */
    public double[][] corelatedness(int[] ids) throws DaoException;

    /**
     * Returns an asymmetric corelatedness matrix of concept IDs xIds to yIds.
     * @param xIds
     * @param yIds
     * @return
     * @throws DaoException
     */
    public double[][] corelatedness(int[] xIds, int[] yIds) throws DaoException;
}
