package org.semtag.core.dao.sql;

import org.jooq.Record;
import org.semtag.core.dao.DaoException;
import org.semtag.core.dao.UserDao;
import org.semtag.core.jooq.Tables;
import org.semtag.core.model.User;

import javax.sql.DataSource;

/**
 * @author Ari Weiland
 */
public class UserSqLDao extends BaseSqLDao<User> implements UserDao {

    public UserSqLDao(DataSource dataSource) throws DaoException {
        super(dataSource, "users", Tables.USERS);
    }

    @Override
    public void save(User item) throws DaoException {
        insert(item.getUserId());
    }

    @Override
    public User getByUserId(int userId) throws DaoException {
        return getByUserId(String.valueOf(userId));
    }

    @Override
    public User getByUserId(String userId) throws DaoException {
        Record record = fetchOne(Tables.USERS.USER_ID.eq(userId));
        return buildUser(record);
    }

    private User buildUser(Record record) {
        if (record == null) {
            return null;
        }
        return new User(record.getValue(Tables.USERS.USER_ID));
    }
}
