package org.semtag.dao;

import org.semtag.model.Item;

/**
 * Interface that describes a dao for storing and retrieving items.
 *
 * @author Ari Weiland
 */
public interface ItemDao extends Dao<Item> {

    /**
     * Fetches an Item from the database specified by the given ID.
     * @param itemId
     * @return
     * @throws DaoException
     */
    public Item getByItemId(long itemId) throws DaoException;

    /**
     * Fetches an Item from the database specified by the given ID.
     * @param itemId
     * @return
     * @throws DaoException
     */
    public Item getByItemId(String itemId) throws DaoException;
}
