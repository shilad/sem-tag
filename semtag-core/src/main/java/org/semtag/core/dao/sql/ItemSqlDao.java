package org.semtag.core.dao.sql;

import org.semtag.core.dao.DaoException;
import org.semtag.core.dao.ItemDao;
import org.semtag.core.model.Item;
import org.semtag.loader.PropertySchema;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * @author Ari Weiland
 */
public class ItemSqlDao extends BaseSqLDao<Item> implements ItemDao {

    private final PropertySchema schema;

    public ItemSqlDao(DataSource dataSource, PropertySchema schema) throws DaoException {
        super(dataSource, schema.getSchemaName());
        this.schema = schema;
    }

    @Override
    public void beginLoad() throws DaoException {
        try {
            schema.buildSqlFiles(true);
        } catch (IOException e) {
            throw new DaoException(e);
        }
        super.beginLoad();
    }

    @Override
    public void save(Item item) throws DaoException {
//        Object[] values = new Object[] { item.getItemId(), item.getName() };
//        values = ArrayUtils.addAll(values, item.getProperties().values().toArray());
//        insert(values);
    }

}
