package org.semtag.dao;

import org.semtag.model.Tag;

/**
 * Interface that describes a dao for storing and retrieving tags.
 *
 * @author Shilad Sen
 */
public interface TagDao extends Dao<Tag> {

    /**
     * Fetches a tag from the database associated with the specified string.
     * @param tag
     * @return
     * @throws org.semtag.dao.DaoException
     */
    public Tag getByTag(String tag) throws DaoException;
}
