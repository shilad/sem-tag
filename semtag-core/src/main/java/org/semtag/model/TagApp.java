package org.semtag.model;

import org.apache.commons.lang3.ObjectUtils;
import org.semtag.dao.ConceptDao;
import org.semtag.dao.DaoException;
import org.semtag.model.concept.Concept;

import java.sql.Timestamp;

/**
 * A specific application of a tag.
 * Maintains the user who applied it, the tag they applied, the
 * item to which they applied it, and the application timestamp.
 *
 * @author Ari Weiland
 */
public class TagApp {
    private final long tagAppId;
    private final User user;
    private final Tag tag;
    private final Item item;
    private final Timestamp timestamp;

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
        this(tagAppId, user, tag, item, timestamp, new Concept(conceptId));
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
        this.tagAppId = tagAppId < 0 ? -1 : tagAppId;
        this.user = user;
        this.tag = tag;
        this.item = item;
        this.timestamp = timestamp;
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
    public Concept getConcept() {
        return concept;
    }

    public boolean hasConcept() {
        return concept != null;
    }

    /**
     * Sets the concept only if the specified concept matches this TagApp's concept ID,
     * and the concept was not previously set. Returns whether or not the concept was set.
     * @param concept
     * @return true only if the concept was not previously set and the input concept is valid
     */
    public boolean setConcept(Concept concept) {
        this.concept = concept;
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TagApp)) return false;

        TagApp t = (TagApp) o;
        if (tagAppId != -1 && t.tagAppId != -1) {
            return tagAppId == t.tagAppId;
        } else {
            return  user.equals(t.user) &&
                    tag.equals(t.tag) &&
                    item.equals(t.item) &&
                    timestamp.equals(t.timestamp) &&
                    ObjectUtils.equals(concept, t.concept);
        }
    }

    @Override
    public int hashCode() {
        int result = user.hashCode();
        result = 31 * result + tag.hashCode();
        result = 31 * result + item.hashCode();
        result = 31 * result + timestamp.hashCode();
        result = 31 * result + ((concept == null) ? -1 : concept.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "TagApp{" +
                "tagAppId=" + tagAppId +
                ", user=" + user +
                ", tag=" + tag +
                ", item=" + item +
                ", timestamp=" + timestamp +
                ", concept=" + concept +
                '}';
    }
}
