package org.semtag.core.dao.sql;

import org.semtag.core.dao.DaoException;
import org.semtag.core.dao.UserDao;
import org.semtag.core.model.User;

import javax.sql.DataSource;

/**
 * @author Ari Weiland
 */
public class UserSqLDao extends BaseSqLDao<User> implements UserDao {

    public UserSqLDao(DataSource dataSource, String sqlScriptPrefix) throws DaoException {
        super(dataSource, "users");
    }

    @Override
    public void save(User item) throws DaoException {
        insert(item.getUserId());
    }
}
