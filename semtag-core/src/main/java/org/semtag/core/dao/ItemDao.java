package org.semtag.core.dao;

import org.semtag.core.model.Item;

/**
 * @author Ari Weiland
 */
public interface ItemDao extends Dao<Item> {
    Item getByItemId(int itemId) throws DaoException;

    Item getByItemId(String itemId) throws DaoException;
}
