package org.semtag.core.dao.sql;

import org.semtag.core.dao.DaoException;
import org.semtag.core.dao.ItemDao;
import org.semtag.core.jooq.Tables;
import org.semtag.core.model.Item;

import javax.sql.DataSource;

/**
 * @author Ari Weiland
 */
public class ItemSqlDao extends BaseSqLDao<Item> implements ItemDao {

    public ItemSqlDao(DataSource dataSource) throws DaoException {
        super(dataSource, "items", Tables.ITEMS);
    }

    @Override
    public void save(Item item) throws DaoException {
        insert(item.getItemId());
    }

}
