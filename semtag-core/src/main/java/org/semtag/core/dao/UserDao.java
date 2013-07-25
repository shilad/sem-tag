package org.semtag.core.dao;

import org.semtag.core.model.User;

/**
 * @author Ari Weiland
 */
public interface UserDao extends Dao<User> {
    User getByUserId(int userId) throws DaoException;

    User getByUserId(String userId) throws DaoException;
}
