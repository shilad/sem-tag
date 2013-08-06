package org.semtag.dao;

import org.apache.commons.lang.ArrayUtils;
import org.semtag.model.Tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Ari Weiland
 * @author Yulun Li
 */
public class DaoFilter {
    private Collection<Long> tagAppIds;
    private Collection<String> userIds;
    private Collection<String> tags;
    private Collection<String> itemIds;
    private Collection<Integer> conceptIds;
    private String userId;
    private String tag;
    private String itemId;
    private Integer conceptId;

    public DaoFilter() {
        this.userIds = null;
        this.tags = null;
        this.itemIds = null;
        this.conceptIds = null;
        this.userId = null;
        this.tag = null;
        this.itemId = null;
        this.conceptId = null;
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
     * @param conceptIds
     * @return
     */
    public DaoFilter setConceptIds(Collection<Integer> conceptIds) {
        this.conceptIds = conceptIds;
        return this;
    }

    /**
     * Sets the concept filter to the specified array of concept IDs.
     * Used by TagApp, Concept.
     * @param conceptIds
     * @return
     */
    public DaoFilter setConceptIds(Integer... conceptIds) {
        this.conceptIds = Arrays.asList(conceptIds);
        return this;
    }

    /**
     * Sets the concept filter to the specified array of concept IDs.
     * Used by TagApp, Concept.
     * @param conceptIds
     * @return
     */
    public DaoFilter setConceptIds(int... conceptIds) {
        return setConceptIds(ArrayUtils.toObject(conceptIds));
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
     * @param conceptId
     * @return
     */
    public DaoFilter setConceptId(Integer conceptId) {
        this.conceptIds = Collections.singleton(conceptId);
        this.conceptId = conceptId;
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

    public Collection<Integer> getConceptIds() {
        return conceptIds;
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

    public Integer getConceptId() {
        return conceptId;
    }
}
