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

    private int conceptId;
    private Concept concept;

    /**
     * Constructs a TagApp without an ID and without any concept references.
     * @param user
     * @param tag
     * @param item
     * @param timestamp
     */
    public TagApp(User user, Tag tag, Item item, Timestamp timestamp) {
        this(user, tag, item, timestamp, -1);
    }

    /**
     * Constructs a TagApp without an ID and with a concept ID.
     * @param user
     * @param tag
     * @param item
     * @param timestamp
     * @param conceptId
     */
    public TagApp(User user, Tag tag, Item item, Timestamp timestamp, int conceptId) {
        this(-1, user, tag, item, timestamp, conceptId);
    }

    /**
     * Constructs a TagApp without an ID and with a concept.
     * @param user
     * @param tag
     * @param item
     * @param timestamp
     * @param concept
     */
    public TagApp(User user, Tag tag, Item item, Timestamp timestamp, Concept concept) {
        this(-1, user, tag, item, timestamp, concept);
    }

    /**
     * Constructs a TagApp with an ID and with a concept ID.
     * @param tagAppId
     * @param user
     * @param tag
     * @param item
     * @param timestamp
     * @param conceptId
     */
    public TagApp(long tagAppId, User user, Tag tag, Item item, Timestamp timestamp, int conceptId) {
        this(tagAppId, user, tag, item, timestamp, conceptId, null);
    }

    /**
     * Constructs a TagApp with an ID and with a concept.
     * @param tagAppId
     * @param user
     * @param tag
     * @param item
     * @param timestamp
     * @param concept
     */
    public TagApp(long tagAppId, User user, Tag tag, Item item, Timestamp timestamp, Concept concept) {
        this(tagAppId, user, tag, item, timestamp, concept.getConceptId(), concept);
    }

    private TagApp(long tagAppId, User user, Tag tag, Item item, Timestamp timestamp, int conceptId, Concept concept) {
        this.tagAppId = tagAppId < 0 ? -1 : tagAppId;
        this.user = user;
        this.tag = tag;
        this.item = item;
        this.timestamp = timestamp;
        if (conceptId < 0) {
            this.conceptId = -1;
            this.concept = null;
        } else {
            this.conceptId = conceptId;
            this.concept = concept;
        }
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
     * Sets the concept ID only if the concept ID was not previously set.
     * Returns whether or not the concept ID was set
     * @param conceptId
     * @return true only if the concept ID was not previously set.
     */
    public boolean setConceptId(int conceptId) {
        if (conceptId > -1) {
            this.conceptId = conceptId;
            return true;
        }
        return false;
    }

    /**
     * Sets the concept only if the specified concept matches this TagApp's concept ID,
     * and the concept was not previously set. Returns whether or not the concept was set.
     * @param concept
     * @return true only if the concept was not previously set and the input concept is valid
     */
    public boolean setConcept(Concept concept) {
        if (concept != null && this.concept == null && setConceptId(concept.getConceptId())) {
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
        if (!(o instanceof TagApp)) {
            return false;
        }
        TagApp t = (TagApp) o;
        if (tagAppId != -1 && t.tagAppId != -1) {
            return tagAppId == t.tagAppId;
        } else {
            return  user.equals(t.user) &&
                    tag.equals(t.tag) &&
                    item.equals(t.item) &&
                    timestamp.equals(t.timestamp) &&
                    conceptId == t.conceptId;
        }
    }

    @Override
    public String toString() {
        return "TagApp{" +
                "tagAppId=" + tagAppId +
                ", user=" + user +
                ", tag=" + tag +
                ", item=" + item +
                ", timestamp=" + timestamp +
                ", conceptId=" + conceptId +
                ", concept=" + concept +
                '}';
    }
}
