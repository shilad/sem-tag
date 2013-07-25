package org.semtag.core.dao;

import org.semtag.core.model.Tag;

import java.util.Collection;

/**
 * @author Ari Weiland
 */
public class DaoFilter {
    private Collection<String> userIds;
    private Collection<Tag> tags;
    private Collection<String> itemIds;
    private Collection<Integer> conceptIds;

    public DaoFilter() {
        this.userIds = null;
        this.tags = null;
        this.itemIds = null;
        this.conceptIds = null;
    }

    public void setUserIds(Collection<String> userIds) {
        this.userIds = userIds;
    }

    public void setTags(Collection<Tag> tags) {
        this.tags = tags;
    }

    public void setItemIds(Collection<String> itemIds) {
        this.itemIds = itemIds;
    }

    public void setConceptIds(Collection<Integer> conceptIds) {
        this.conceptIds = conceptIds;
    }

    public Collection<String> getUserIds() {
        return userIds;
    }

    public Collection<Tag> getTags() {
        return tags;
    }

    public Collection<String> getItemIds() {
        return itemIds;
    }

    public Collection<Integer> getConceptIds() {
        return conceptIds;
    }
}
