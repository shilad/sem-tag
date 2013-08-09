package org.semtag.dao;

import org.semtag.model.User;

/**
 * Interface that describes a dao for storing and retrieving users.
 *
 * @author Ari Weiland
 */
public interface UserDao extends Dao<User> {

    /**
     * Fetches a User from the database specified by the given ID.
     * @param userId
     * @return
     * @throws DaoException
     */
    public User getByUserId(long userId) throws DaoException;

    /**
     * Fetches a User from the database specified by the given ID.
     * @param userId
     * @return
     * @throws DaoException
     */
    public User getByUserId(String userId) throws DaoException;
}
