package org.semtag.core.dao;

import edu.emory.mathcs.backport.java.util.Arrays;
import edu.emory.mathcs.backport.java.util.Collections;
import org.semtag.core.model.Tag;

import java.util.Collection;

/**
 * @author Ari Weiland
 */
public class DaoFilter {
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

    public DaoFilter setUserIds(Collection<String> userIds) {
        this.userIds = userIds;
        return this;
    }

    public DaoFilter setUserIds(String... userIds) {
        return setUserIds(Arrays.asList(userIds));
    }

    public DaoFilter setTags(Collection<Tag> tags) {
        String[] strings = new String[tags.size()];
        int i=0;
        for (Tag tag : tags) {
            strings[i] = tag.getNormalizedTag();
            i++;
        }
        return setTags(strings);
    }

    public DaoFilter setTags(Tag... tags) {
        return setTags(Arrays.asList(tags));
    }

    public DaoFilter setTags(String... tags) {
        this.tags = Arrays.asList(tags);
        return this;
    }

    public DaoFilter setItemIds(Collection<String> itemIds) {
        this.itemIds = itemIds;
        return this;
    }

    public DaoFilter setItemIds(String... itemIds) {
        this.itemIds = Arrays.asList(itemIds);
        return this;
    }

    public DaoFilter setConceptIds(Collection<Integer> conceptIds) {
        this.conceptIds = conceptIds;
        return this;
    }

    public DaoFilter setConceptIds(Integer... conceptIds) {
        this.conceptIds = Arrays.asList(conceptIds);
        return this;
    }

    public DaoFilter setUserId(String userId) {
        this.userIds = Collections.singleton(userId);
        this.userId = userId;
        return this;
    }

    public DaoFilter setTag(Tag tag) {
        return setTag(tag.getNormalizedTag());
    }

    public DaoFilter setTag(String tag) {
        this.tags = Collections.singleton(tag);
        this.tag = tag;
        return this;
    }

    public DaoFilter setItemId(String itemId) {
        this.itemIds = Collections.singleton(itemId);
        this.itemId = itemId;
        return this;
    }

    public DaoFilter setConceptId(Integer conceptId) {
        this.conceptIds = Collections.singleton(conceptId);
        this.conceptId = conceptId;
        return this;
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
