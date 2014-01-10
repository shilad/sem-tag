package org.semtag.dao;

import org.semtag.model.TagApp;

import java.util.logging.Logger;

/**
 * The SaveHandler is used by the loader to synchronize the save
 * process across all tables. It is designed to prevent for example
 * a TagApp to be saved but not the concept it is mapped to. Either
 * all four tables are saved to, or it saves to none of them.
 *
 * @author Ari Weiland
 */
public abstract class SaveHandler {

    protected static final Logger LOG = Logger.getLogger(SaveHandler.class.getName());

    protected final TagAppDao tagAppDao;
    protected final UserDao userDao;
    protected final ItemDao itemDao;
    protected final ConceptDao conceptDao;

    public SaveHandler(TagAppDao tagAppDao, UserDao userDao, ItemDao itemDao, ConceptDao conceptDao) {
        this.tagAppDao = tagAppDao;
        this.userDao = userDao;
        this.itemDao = itemDao;
        this.conceptDao = conceptDao;
    }

    /**
     * Calls clear() on all its wrapped daos.
     * @throws DaoException
     */
    public void clear() throws DaoException {
        this.tagAppDao.clear();
        this.userDao.clear();
        this.itemDao.clear();
        this.conceptDao.clear();
    }

    /**
     * Calls beginLoad() on all its wrapped daos.
     * @throws DaoException
     */
    public void beginLoad() throws DaoException {
        this.tagAppDao.beginLoad();
        this.userDao.beginLoad();
        this.itemDao.beginLoad();
        this.conceptDao.beginLoad();
    }

    /**
     * Calls save() on all its wrapped daos.
     * @throws DaoException
     */
    public abstract void save(TagApp tagApp) throws DaoException;

    /**
     * Calls endLoad() on all its wrapped daos.
     * @throws DaoException
     */
    public void endLoad() throws DaoException {
        this.tagAppDao.endLoad();
        this.userDao.endLoad();
        this.itemDao.endLoad();
        this.conceptDao.endLoad();
    }
}
