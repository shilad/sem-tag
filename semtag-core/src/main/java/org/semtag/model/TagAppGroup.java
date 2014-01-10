package org.semtag.model;

import com.google.common.collect.Sets;
import org.semtag.dao.DaoFilter;
import org.semtag.model.concept.Concept;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

/**
 * This class represents a group of TagApps, generally associated with
 * an itemId or userId or both that all the TagApps have in common.
 * Instantiating this class without the userId or itemId parameters or
 * setting those parameters to null represents a lack of association
 * to a userId or itemId, respectively.
 *
 * @author Ari Weiland
 */
public class TagAppGroup implements Iterable<TagApp> {
    private final String userId;
    private final Tag tag;
    private final String itemId;
    private final Concept concept;
    private final Set<TagApp> tagApps;

    /**
     * Creates a TagAppGroup with no associations.
     * @param tagApps
     */
    public TagAppGroup(Set<TagApp> tagApps) {
        this.userId = null;
        this.tag = null;
        this.itemId = null;
        this.concept = null;
        this.tagApps = tagApps;
    }

    /**
     * Creates a TagAppGroup with an optional associated common userId and itemId.
     * @param userId
     * @param tag
     * @param itemId
     * @param concept
     * @param tagApps
     */
    public TagAppGroup(String userId, Tag tag, String itemId, Concept concept, Set<TagApp> tagApps) {
        this.userId = userId;
        this.tag = tag;
        this.itemId = itemId;
        this.concept = concept;
        this.tagApps = tagApps;
    }

    public TagAppGroup(DaoFilter filter, Set<TagApp> tagApps) {
        this.userId = filter.getUserId();
        if (filter.getTag() == null) {
            this.tag = null;
        } else {
            this.tag = new Tag(filter.getTag());
        }
        this.itemId = filter.getItemId();
        this.concept = filter.getConcept();
        this.tagApps = tagApps;
    }

    /**
     * Returns true if this TagAppGroup has an associated common user.
     * @return
     */
    public boolean hasUser() {
        return userId != null;
    }

    /**
     * Returns the ID of the associated common user, or null if there is none.
     * @return
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Returns true if this TagAppGroup has an associated common tag.
     * @return
     */
    public boolean hasTag() {
        return tag != null;
    }

    /**
     * Returns the associated common tag, or null if there is none.
     * @return
     */
    public Tag getTag() {
        return tag;
    }

    /**
     * Returns true if this TagAppGroup has an associated common item.
     * @return
     */
    public boolean hasItem() {
        return itemId != null;
    }

    /**
     * Returns the ID of the associated common item, or null if there is none.
     * @return
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * Returns true if this TagAppGroup has an associated concept.
     * @return
     */
    public boolean hasConcept() {
        return concept != null;
    }

    /**
     * Returns the ID of the associated concept, or null if there is none.
     * @return
     */
    public Concept getConcept() {
        return concept;
    }

    /**
     * Returns an unmodifiable set of the TagApps in this group.
     * @return
     */
    public Set<TagApp> getTagApps() {
        return Collections.unmodifiableSet(tagApps);
    }

    /**
     * Returns an arbitrary TagApp from the group, based on iterator.next().
     * Returns null if the group is empty.
     * @return
     */
    public TagApp getAnyTagApp() {
        if (isEmpty()) {
            return null;
        } else {
            return iterator().next();
        }
    }

    public int size() {
        return tagApps.size();
    }

    public boolean isEmpty() {
        return tagApps.isEmpty();
    }

    /**
     * Returns a new TagAppGroup that represents the intersection of the two groups,
     * with properly set common fields. If the intersection is empty, it will return
     * an empty group with the common fields set. However, if the input groups
     * conflict (ie. group1 refers to user 1, but group2 refers to user 2), then it
     * returns null.
     *
     * @param group1
     * @param group2
     * @return
     */
    public static TagAppGroup intersection(TagAppGroup group1, TagAppGroup group2) {
        String userId;
        if (group1.userId != null) {
            if (group2.userId != null && !group2.userId.equals(group1.userId)) {
                // userIds conflict
                return null;
            }
            // group1 userId is not null and group2 userId is null or is the same.
            userId = group1.userId;
        } else {
            // either both userIds are null or group2 userId is not null and group1 userId is
            userId = group2.userId;
        }
        Tag tag;
        if (group1.tag != null) {
            if (group2.tag != null && !group2.tag.equals(group1.tag)) {
                return null;
            }
            tag = group1.tag;
        } else {
            tag = group2.tag;
        }
        String itemId;
        if (group1.itemId != null) {
            if (group2.itemId != null && !group2.itemId.equals(group1.itemId)) {
                return null;
            }
            itemId = group1.itemId;
        } else {
            itemId = group2.itemId;
        }
        Concept concept;
        if (group1.concept != null) {
            if (group2.concept != null && !group2.concept.equals(group1.concept)) {
                return null;
            }
            concept = group1.concept;
        } else {
            concept = group2.concept;
        }
        Set<TagApp> intersection = Sets.intersection(group1.tagApps, group2.tagApps);
        return new TagAppGroup(
                userId,
                tag,
                itemId,
                concept,
                intersection);
    }

    @Override
    public Iterator<TagApp> iterator() {
        return Collections.unmodifiableSet(tagApps).iterator();
    }
}
