package org.semtag.dao;

import org.apache.commons.lang3.ArrayUtils;
import org.semtag.model.Tag;
import org.semtag.model.concept.Concept;

import java.util.*;

/**
 * Specifies filters to apply to the various SemTag Daos.
 * When passed to a Dao.get() method, all returned results
 * will be within the space of the applied filters. Filters
 * include :
 *
 * - TagApp IDs  (used by TagApp)
 * - User IDs    (used by TagApp and User)
 * - Item IDs    (used by TagApp and Item)
 * - Concept IDs (used by TagApp and Concept)
 * - Tag         (used by TagApp)
 *
 * Note that the tag filter will apply the filter by the
 * normalized string, not the raw string.
 *
 * This DaoFilter also contains special singleton methods
 * that also set additional singleton fields. These are used
 * by the TagAppDao.getGroup() method to retrieve a group of
 * TagApps with certain parameters (specified by the singleton
 * methods) in common.
 *
 * @author Ari Weiland
 * @author Yulun Li
 */
public class DaoFilter {
    private Collection<Long> tagAppIds;
    private Collection<String> userIds;
    private Collection<String> tags;
    private Collection<String> itemIds;
    private Collection<Concept> concepts;
    private String userId;
    private String tag;
    private String itemId;
    private Concept concept;

    public DaoFilter() {
        this.userIds = null;
        this.tags = null;
        this.itemIds = null;
        this.concepts = null;
        this.userId = null;
        this.tag = null;
        this.itemId = null;
        this.concept = null;
    }

    /**
     * Sets the TagApp filter to the specified collection of TagApp IDs.
     * Used by TagApp.
     * @param tagAppIds
     * @return
     */
    public DaoFilter setTagAppIds(Collection<Long> tagAppIds) {
        this.tagAppIds = tagAppIds;
        return this;
    }

    /**
     * Sets the TagApp filter to the specified array of TagApp IDs.
     * Used by TagApp.
     * @param tagAppIds
     * @return
     */
    public DaoFilter setTagAppIds(Long... tagAppIds) {
        return setTagAppIds(Arrays.asList(tagAppIds));
    }

    /**
     * Sets the TagApp filter to the specified array of TagApp IDs.
     * Used by TagApp.
     * @param tagAppIds
     * @return
     */
    public DaoFilter setTagAppIds(long... tagAppIds) {
        return setTagAppIds(ArrayUtils.toObject(tagAppIds));
    }

    /**
     * Sets the user filter to the specified collection of user IDs.
     * Used by TagApp, User.
     * @param userIds
     * @return
     */
    public DaoFilter setUserIds(Collection<String> userIds) {
        this.userIds = userIds;
        return this;
    }

    /**
     * Sets the user filter to the specified array of user IDs.
     * Used by TagApp, User.
     * @param userIds
     * @return
     */
    public DaoFilter setUserIds(String... userIds) {
        return setUserIds(Arrays.asList(userIds));
    }

    /**
     * Sets the tag filter to the specified collection of tags.
     * Used by TagApp.
     * @param tags
     * @return
     */
    public DaoFilter setTags(Collection<Tag> tags) {
        Collection<String> strings = new ArrayList<String>();
        int i=0;
        for (Tag tag : tags) {
            strings.add(tag.getNormalizedTag());
            i++;
        }
        this.tags = strings;
        return this;
    }

    /**
     * Sets the tag filter to the specified array of tags.
     * Used by TagApp.
     * @param tags
     * @return
     */
    public DaoFilter setTags(Tag... tags) {
        return setTags(Arrays.asList(tags));
    }

    /**
     * Sets the item filter to the specified collection of item IDs.
     * Used by TagApp, Item.
     * @param itemIds
     * @return
     */
    public DaoFilter setItemIds(Collection<String> itemIds) {
        this.itemIds = itemIds;
        return this;
    }

    /**
     * Sets the item filter to the specified array of item IDs.
     * Used by TagApp, Item.
     * @param itemIds
     * @return
     */
    public DaoFilter setItemIds(String... itemIds) {
        this.itemIds = Arrays.asList(itemIds);
        return this;
    }

    /**
     * Sets the concept filter to the specified collection of concept IDs.
     * Used by TagApp, Concept.
     * @param concepts
     * @return
     */
    public DaoFilter setConceptIds(Collection<Concept> concepts) {
        this.concepts = concepts;
        return this;
    }

    /**
     * Sets the concept filter to the specified array of concepts.
     * Used by TagApp, Concept.
     * @param concepts
     * @return
     */
    public DaoFilter setConcepts(Collection<Concept> concepts) {
        this.concepts = Collections.unmodifiableCollection(concepts);
        return this;
    }

    /**
     * Sets the concept filter to the specified array of concepts.
     * Used by TagApp, Concept.
     * @param concepts
     * @return
     */
    public DaoFilter setConcepts(Concept... concepts) {
        this.concepts = Arrays.asList(concepts);
        return this;
    }

    /**
     * Sets the user filter to the specified user ID.
     * Additionally sets a singleton user filter for use by TagAppGroup.
     * Used by TagApp, User.
     * @param userId
     * @return
     */
    public DaoFilter setUserId(String userId) {
        this.userIds = Collections.singleton(userId);
        this.userId = userId;
        return this;
    }

    /**
     * Sets the tag filter to the specified tag.
     * Additionally sets a singleton tag filter for use by TagAppGroup.
     * Used by TagApp.
     * @param tag
     * @return
     */
    public DaoFilter setTag(Tag tag) {
        String string = tag.getNormalizedTag();
        this.tags = Collections.singleton(string);
        this.tag = string;
        return this;
    }

    /**
     * Sets the item filter to the specified item ID.
     * Additionally sets a singleton item filter for use by TagAppGroup.
     * Used by TagApp, User.
     * @param itemId
     * @return
     */
    public DaoFilter setItemId(String itemId) {
        this.itemIds = Collections.singleton(itemId);
        this.itemId = itemId;
        return this;
    }

    /**
     * Sets the concept filter to the specified concept ID.
     * Additionally sets a singleton concept filter for use by TagAppGroup.
     * Used by TagApp, User.
     * @param concept
     * @return
     */
    public DaoFilter setConcept(Concept concept) {
        this.concepts = Collections.singleton(concept);
        this.concept = concept;
        return this;
    }

    public Collection<Long> getTagAppIds() {
        return tagAppIds;
    }

    public Collection<String> getUserIds() {
        return userIds;
    }

    public Collection<String> getTags() {
        return tags;
    }

    public Collection<String> getItemIds() {
        return itemIds;
    }

    public Collection<Concept> getConcepts() {
        return concepts;
    }

    public Collection<Long> getConceptIds() {
        if (concepts == null) {
            return null;
        }
        List<Long> ids = new ArrayList<Long>();
        for (Concept c : concepts) {
            ids.add(c.getConceptId());
        }
        return ids;
    }

    public String getUserId() {
        return userId;
    }

    public String getTag() {
        return tag;
    }

    public String getItemId() {
        return itemId;
    }

    public Concept getConcept() {
        return concept;
    }

    public long getConceptId() {
        return concept == null ? -1 : concept.getConceptId();
    }
}
