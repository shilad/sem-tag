package org.semtag.dao.sql;

import com.typesafe.config.Config;
import org.jooq.Condition;
import org.jooq.Cursor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.semtag.core.jooq.Tables;
import org.semtag.dao.DaoException;
import org.semtag.dao.DaoFilter;
import org.semtag.dao.UserDao;
import org.semtag.model.User;
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
 * A SQL implementation of UserDao.
 *
 * @author Ari Weiland
 */
public class UserSqLDao extends BaseSqLDao<User> implements UserDao {

    public UserSqLDao(WpDataSource dataSource) throws DaoException {
        super(dataSource, "/db/users", Tables.USERS);
    }

    @Override
    public void save(User user) throws DaoException {
        if (getCount(new DaoFilter().setUserId(user.getUserId())) == 0) {
            insert(user.getUserId());
        }
    }

    @Override
    public void save(DSLContext conn, User user) throws DaoException {
        if (getCount(new DaoFilter().setUserId(user.getUserId())) == 0) {
            insert(conn, user.getUserId());
        }
    }

    @Override
    public Iterable<User> get(DaoFilter filter) throws DaoException {
        Collection<Condition> conditions = new ArrayList<Condition>();
        if (filter.getUserIds() != null) {
            conditions.add(Tables.USERS.USER_ID.in(filter.getUserIds()));
        }
        Cursor<Record> cursor = fetchLazy(conditions);
        return buildUserIterable(cursor);
    }

    @Override
    public int getCount(DaoFilter filter) throws DaoException {
        Collection<Condition> conditions = new ArrayList<Condition>();
        if (filter.getUserIds() != null) {
            conditions.add(Tables.USERS.USER_ID.in(filter.getUserIds()));
        }
        return fetchCount(conditions);
    }

    @Override
    public User getByUserId(long userId) throws DaoException {
        return getByUserId(String.valueOf(userId));
    }

    @Override
    public User getByUserId(String userId) throws DaoException {
        Record record = fetchOne(Tables.USERS.USER_ID.eq(userId));
        return buildUser(record);
    }

    private Iterable<User> buildUserIterable(Cursor<Record> cursor) {
        return new SqlDaoIterable<User>(cursor) {
            @Override
            public User transform(Record record) throws DaoException {
                return buildUser(record);
            }
        };
    }

    private User buildUser(Record record) {
        if (record == null) {
            return null;
        }
        return new User(record.getValue(Tables.USERS.USER_ID));
    }

    public static class Provider extends org.wikapidia.conf.Provider<UserDao> {
        public Provider(Configurator configurator, Configuration config) throws ConfigurationException {
            super(configurator, config);
        }

        @Override
        public Class getType() {
            return UserDao.class;
        }

        @Override
        public String getPath() {
            return "sem-tag.dao.user";
        }

        @Override
        public UserSqLDao get(String name, Config config, Map<String, String> runtimeParams) throws ConfigurationException {
            if (!config.getString("type").equals("sql")) {
                return null;
            }
            try {
                return new UserSqLDao(
                        getConfigurator().get(WpDataSource.class, config.getString("datasource"))
                );
            } catch (DaoException e) {
                throw new ConfigurationException(e);
            }
        }
    }
}
