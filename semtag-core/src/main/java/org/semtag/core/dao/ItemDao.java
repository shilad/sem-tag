package org.semtag.core.dao;

import org.semtag.core.model.Item;

/**
 * @author Ari Weiland
 */
public interface ItemDao extends Dao<Item> {

    /**
     * Fetches an Item from the database specified by the given ID.
     * @param itemId
     * @return
     * @throws DaoException
     */
    Item getByItemId(int itemId) throws DaoException;

    /**
     * Fetches an Item from the database specified by the given ID.
     * @param itemId
     * @return
     * @throws DaoException
     */
    Item getByItemId(String itemId) throws DaoException;
}
