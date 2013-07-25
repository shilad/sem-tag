package org.semtag.core.dao.sql;

import org.jooq.Record;
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

    @Override
    public Item getByItemId(int itemId) throws DaoException {
        return getByItemId(String.valueOf(itemId));
    }

    @Override
    public Item getByItemId(String itemId) throws DaoException {
        Record record = fetchOne(Tables.ITEMS.ITEM_ID.eq(itemId));
        return buildItem(record);
    }

    private Item buildItem(Record record) {
        if (record == null) {
            return null;
        }
        return new Item(record.getValue(Tables.ITEMS.ITEM_ID));
    }
}
