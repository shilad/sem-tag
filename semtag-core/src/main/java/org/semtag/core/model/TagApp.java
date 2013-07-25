package org.semtag.core.model;

import org.semtag.core.dao.ConceptDao;
import org.semtag.core.dao.DaoException;
import org.semtag.core.model.concept.Concept;

import java.sql.Timestamp;

/**
 * @author Ari Weiland
 */
public class TagApp {
    private final long tagAppId;
    private final User user;
    private final Tag tag;
    private final Item item;
    private final Timestamp timestamp;
    private final int conceptId;

    private Concept concept;

    public TagApp(long tagAppId, User user, Tag tag, Item item, Timestamp timestamp, int conceptId) {
        this.tagAppId = tagAppId;
        this.user = user;
        this.tag = tag;
        this.item = item;
        this.timestamp = timestamp;
        this.conceptId = conceptId;
        this.concept = null;
    }

    public TagApp(long tagAppId, User user, Tag tag, Item item, Timestamp timestamp, Concept concept) {
        this.tagAppId = tagAppId;
        this.user = user;
        this.tag = tag;
        this.item = item;
        this.timestamp = timestamp;
        this.conceptId = concept.getConceptId();
        this.concept = concept;
    }

    public long getTagAppId() {
        return tagAppId;
    }

    public User getUser() {
        return user;
    }

    public Tag getTag() {
        return tag;
    }

    public Item getItem() {
        return item;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public int getConceptId() {
        return conceptId;
    }

    public Concept getConcept() {
        return concept;
    }

    /**
     * Sets the concept only if the specified concept matches this TagApp's concept ID,
     * and the concept was not previously set. Returns whether or not the concept was set.
     * @param concept
     * @return true only if the concept was not previously set and the input concept is valid
     */
    public boolean setConcept(Concept concept) {
        if (concept != null && this.concept == null && conceptId == concept.getConceptId()) {
            this.concept = concept;
            return true;
        }
        return false;
    }

    /**
     * Sets the concept to the concept retrieved by the specified ConceptDao, as long as
     * the concept was not previously set. Returns whether or not the concept was set.
     * @param dao
     * @return true only if the concept was not previously set and the dao returned a valid concept
     * @throws DaoException
     */
    public boolean setConcept(ConceptDao dao) throws DaoException {
        if (concept == null) {
            concept = dao.getByConceptId(conceptId);
            return concept != null;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof TagApp && this.tagAppId == ((TagApp) o).tagAppId;
    }
}
