package org.semtag.core.model;

import com.google.common.collect.Sets;
import org.semtag.core.dao.DaoFilter;

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
    private final Integer conceptId;
    private final Set<TagApp> tagApps;

    /**
     * Creates a TagAppGroup with no associations.
     * @param tagApps
     */
    public TagAppGroup(Set<TagApp> tagApps) {
        this.userId = null;
        this.tag = null;
        this.itemId = null;
        this.conceptId = null;
        this.tagApps = tagApps;
    }

    /**
     * Creates a TagAppGroup with an optional associated common userId and itemId.
     * @param userId
     * @param tag
     * @param itemId
     * @param conceptId
     * @param tagApps
     */
    public TagAppGroup(String userId, Tag tag, String itemId, Integer conceptId, Set<TagApp> tagApps) {
        this.userId = userId;
        this.tag = tag;
        this.itemId = itemId;
        this.conceptId = conceptId;
        this.tagApps = tagApps;
    }

    public TagAppGroup(DaoFilter filter, Set<TagApp> tagApps) {
        this.userId = filter.getUserId();
        this.tag = new Tag(filter.getTag());
        this.itemId = filter.getItemId();
        this.conceptId = filter.getConceptId();
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
        return conceptId != null;
    }

    /**
     * Returns the ID of the associated concept, or null if there is none.
     * @return
     */
    public Integer getConceptId() {
        return conceptId;
    }

    /**
     * Returns an unmodifiable set of the TagApps in this group.
     * @return
     */
    public Set<TagApp> getTagApps() {
        return Collections.unmodifiableSet(tagApps);
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
        Integer conceptId;
        if (group1.conceptId != null) {
            if (group2.conceptId != null && !group2.conceptId.equals(group1.conceptId)) {
                return null;
            }
            conceptId = group1.conceptId;
        } else {
            conceptId = group2.conceptId;
        }
        Set<TagApp> intersection = Sets.intersection(group1.tagApps, group2.tagApps);
        return new TagAppGroup(
                userId,
                tag,
                itemId,
                conceptId,
                intersection);
    }

    @Override
    public Iterator<TagApp> iterator() {
        return Collections.unmodifiableSet(tagApps).iterator();
    }
}
