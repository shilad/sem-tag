package org.semtag.core.dao.sql;

import org.semtag.core.dao.DaoException;
import org.semtag.core.dao.UserDao;
import org.semtag.core.model.User;

import javax.sql.DataSource;

/**
 * @author Ari Weiland
 */
public class UserSqLDao extends BaseSqLDao<User> implements UserDao {

//    private final PropertySchema schema;
//
//    public UserSqLDao(DataSource dataSource, PropertySchema schema) throws DaoException {
//        super(dataSource, schema.getSchemaName());
//        this.schema = schema;
//    }

    public UserSqLDao(DataSource dataSource, String sqlScriptPrefix) throws DaoException {
        super(dataSource, sqlScriptPrefix);
    }


//    @Override
//    public void beginLoad() throws DaoException {
//        try {
//            schema.buildSqlFiles(true);
//        } catch (IOException e) {
//            throw new DaoException(e);
//        }
//        super.beginLoad();
//    }
//
//    @Override
//    public void save(User user) throws DaoException {
//        Object[] values = new Object[] { user.getUserId() };
//        values = ArrayUtils.addAll(values, user.getProperties().values().toArray());
//        insert(values);
//    }

    @Override
    public void save(User item) throws DaoException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
