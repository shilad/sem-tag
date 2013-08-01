package org.semtag.dao;

import org.semtag.model.TagApp;
import org.semtag.model.TagAppGroup;

/**
 * @author Ari Weiland
 */
public interface TagAppDao extends Dao<TagApp> {

    /**
     * Fetches a TagApp from the database specified by the given ID.
     * @param tagAppId
     * @return
     * @throws DaoException
     */
    public TagApp getByTagAppId(long tagAppId) throws DaoException;

    /**
     * Fetches a single TagAppGroup from the database that matches the
     * specified singleton fields.
     * @param filter
     * @return
     * @throws DaoException
     */
    public TagAppGroup getGroup(DaoFilter filter) throws DaoException;
}
