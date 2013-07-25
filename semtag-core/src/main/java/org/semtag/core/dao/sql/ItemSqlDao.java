package org.semtag.core.dao.sql;

import org.jooq.Condition;
import org.jooq.Cursor;
import org.jooq.Record;
import org.semtag.core.dao.DaoException;
import org.semtag.core.dao.DaoFilter;
import org.semtag.core.dao.ItemDao;
import org.semtag.core.jooq.Tables;
import org.semtag.core.model.Item;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collection;

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
    public Iterable<Item> get(DaoFilter filter) throws DaoException {
        Collection<Condition> conditions = new ArrayList<Condition>();
        if (filter.getUserIds() != null) {
            conditions.add(Tables.ITEMS.ITEM_ID.in(filter.getUserIds()));
        }
        Cursor<Record> cursor = fetchLazy(conditions);
        return buildItemIterable(cursor);
    }

    @Override
    public int getCount(DaoFilter filter) throws DaoException {
        Collection<Condition> conditions = new ArrayList<Condition>();
        if (filter.getItemIds() != null) {
            conditions.add(Tables.ITEMS.ITEM_ID.in(filter.getItemIds()));
        }
        return fetchCount(conditions);
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

    private Iterable<Item> buildItemIterable(Cursor<Record> cursor) {
        return new SqlDaoIterable<Item>(cursor) {
            @Override
            public Item transform(Record record) throws DaoException {
                return buildItem(record);
            }
        };
    }

    private Item buildItem(Record record) {
        if (record == null) {
            return null;
        }
        return new Item(record.getValue(Tables.ITEMS.ITEM_ID));
    }
}
