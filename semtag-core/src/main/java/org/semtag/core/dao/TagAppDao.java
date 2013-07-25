package org.semtag.core.dao;

import org.semtag.core.model.TagApp;
import org.semtag.core.model.TagAppGroup;

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
    TagApp getByTagAppId(long tagAppId) throws DaoException;

    /**
     * Fetches a single TagAppGroup from the database that matches the
     * specified singleton fields.
     * @param filter
     * @return
     * @throws DaoException
     */
    TagAppGroup getGroup(DaoFilter filter) throws DaoException;
}
