package org.semtag.dao;

import org.semtag.model.concept.Concept;

/**
 * @author Ari Weiland
 */
public interface ConceptDao extends Dao<Concept> {

    /**
     * Fetches a concept from the database specified by the given ID.
     * @param conceptId
     * @return
     * @throws DaoException
     */
    public Concept getByConceptId(int conceptId) throws DaoException;
}