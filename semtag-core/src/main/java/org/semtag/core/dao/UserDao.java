package org.semtag.core.dao;

import org.semtag.core.model.User;

/**
 * @author Ari Weiland
 */
public interface UserDao extends Dao<User> {

    /**
     * Fetches a User from the database specified by the given ID.
     * @param userId
     * @return
     * @throws DaoException
     */
    public User getByUserId(int userId) throws DaoException;

    /**
     * Fetches a User from the database specified by the given ID.
     * @param userId
     * @return
     * @throws DaoException
     */
    public User getByUserId(String userId) throws DaoException;
}
