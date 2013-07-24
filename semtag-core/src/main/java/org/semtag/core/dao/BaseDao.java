package org.semtag.core.dao;

/**
 * @author Ari Weiland
 */
public abstract class BaseDao<T> implements Dao<T> {
    @Override
    public void clear() throws DaoException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void beginLoad() throws DaoException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void save(T item) throws DaoException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void endLoad() throws DaoException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Iterable<T> get(DaoFilter daoFilter) throws DaoException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getCount(DaoFilter daoFilter) throws DaoException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
