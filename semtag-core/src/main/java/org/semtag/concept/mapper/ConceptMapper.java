package org.semtag.concept.mapper;

import org.semtag.SemTagException;
import org.semtag.concept.Concept;
import org.semtag.core.dao.DaoException;
import org.semtag.core.model.Item;
import org.semtag.core.model.Tag;
import org.semtag.core.model.TagApp;
import org.semtag.core.model.User;

import java.sql.Timestamp;

/**
 * This class serves two purposes. The primary purpose is during the load process,
 * the {@code map} method takes in the TagApp parameters, maps them to an
 * appropriate concept, and returns the assembled TagApp. Additionally, the
 * {@code getConcept} method is used by the ConceptDao to assemble the appropriate
 * concept from the information stored in the database. A subclass should implement
 * these methods to refer to a specific subclass of {@link org.semtag.concept.Concept}.
 *
 * This class must be passed to both the loader and any instance of a ConceptDao.
 *
 * @author Ari Weiland
 * @author Yulun Li
 */
public interface ConceptMapper {

    /**
     * This method is used in the ConceptLoader to assemble a TagApp and map it to
     * the appropriate concept.
     * @param user
     * @param tag
     * @param item
     * @param timestamp
     * @return
     * @throws SemTagException
     */
    public abstract TagApp map(User user, Tag tag, Item item, Timestamp timestamp) throws SemTagException;

    /**
     * This method is used in the ConceptDao to retrieve the correct type of concept
     * based on the information in the database.
     * @param conceptId
     * @param metric
     * @param objBytes
     * @return
     */
    public abstract Concept getConcept(int conceptId, String metric, byte[] objBytes) throws DaoException;


}
