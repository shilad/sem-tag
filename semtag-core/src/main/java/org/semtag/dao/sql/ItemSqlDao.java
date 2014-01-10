package org.semtag.dao.sql;

import com.typesafe.config.Config;
import org.jooq.Condition;
import org.jooq.Cursor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.semtag.core.jooq.Tables;
import org.semtag.dao.DaoException;
import org.semtag.dao.DaoFilter;
import org.semtag.dao.ItemDao;
import org.semtag.model.Item;
import org.wikapidia.conf.Configuration;
import org.wikapidia.conf.ConfigurationException;
import org.wikapidia.conf.Configurator;
import org.wikapidia.core.dao.sql.WpDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * A SQL implementation of ItemDao.
 *
 * @author Ari Weiland
 */
public class ItemSqlDao extends BaseSqLDao<Item> implements ItemDao {

    public ItemSqlDao(WpDataSource dataSource) throws DaoException {
        super(dataSource, "/db/items", Tables.ITEMS);
    }

    @Override
    public void save(Item item) throws DaoException {
        if (getCount(new DaoFilter().setItemId(item.getItemId())) == 0) {
            insert(item.getItemId());
        }
    }

    @Override
    public void save(DSLContext conn, Item item) throws DaoException {
        if (getCount(new DaoFilter().setItemId(item.getItemId())) == 0) {
            insert(conn, item.getItemId());
        }
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
    public Item getByItemId(long itemId) throws DaoException {
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

    public static class Provider extends org.wikapidia.conf.Provider<ItemDao> {
        public Provider(Configurator configurator, Configuration config) throws ConfigurationException {
            super(configurator, config);
        }

        @Override
        public Class getType() {
            return ItemDao.class;
        }

        @Override
        public String getPath() {
            return "sem-tag.dao.item";
        }

        @Override
        public ItemSqlDao get(String name, Config config, Map<String, String> runtimeParams) throws ConfigurationException {
            if (!config.getString("type").equals("sql")) {
                return null;
            }
            try {
                return new ItemSqlDao(
                        getConfigurator().get(WpDataSource.class, config.getString("datasource"))
                );
            } catch (DaoException e) {
                throw new ConfigurationException(e);
            }
        }
    }
}
