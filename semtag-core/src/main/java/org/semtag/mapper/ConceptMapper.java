package org.semtag.mapper;

import org.semtag.SemTagException;
import org.semtag.dao.DaoException;
import org.semtag.model.Item;
import org.semtag.model.Tag;
import org.semtag.model.TagApp;
import org.semtag.model.User;
import org.semtag.model.concept.Concept;

import java.sql.Timestamp;

/**
 * This class serves two purposes. The primary purpose is during the load process,
 * the {@code map} method takes in the TagApp parameters, maps them to an
 * appropriate concept, and returns the assembled TagApp. Additionally, the
 * {@code getConcept} method is used by the ConceptDao to assemble the appropriate
 * concept from the information stored in the database. A subclass should implement
 * these methods to refer to a specific subclass of {@link org.semtag.model.concept.Concept}.
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
     * Maps a tag without any context to a concept.
     * Sets the concept id of the tag to the concept as a side-effect.
     * @param tag
     * @return
     * @throws SemTagException
     */
    public Concept map(Tag tag) throws SemTagException;
}
