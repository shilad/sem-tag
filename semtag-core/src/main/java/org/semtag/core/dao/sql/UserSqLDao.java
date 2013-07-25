package org.semtag.core.dao.sql;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.semtag.core.dao.DaoException;
import org.semtag.core.dao.UserDao;
import org.semtag.core.jooq.Tables;
import org.semtag.core.model.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

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

    public User getByUserId(String id) throws DaoException {
        Connection conn = null;
        try {
            conn = ds.getConnection();
            DSLContext context = DSL.using(conn, dialect);
            Record record = context.selectFrom(Tables.USERS)
                    .where(Tables.USERS.USER_ID.eq(id))
                    .fetchOne();
            return buildUser(record);
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            quietlyCloseConn(conn);
        }
    }

    private User buildUser(Record record) {
        if (record == null) {
            return null;
        }
        return new User(record.getValue(Tables.USERS.USER_ID));
    }
}
