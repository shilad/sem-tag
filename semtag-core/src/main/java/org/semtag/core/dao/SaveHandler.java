package org.semtag.core.dao;

import org.semtag.core.model.TagApp;

import java.util.logging.Logger;

/**
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

    public void beginLoad() throws DaoException {
        this.tagAppDao.beginLoad();
        this.userDao.beginLoad();
        this.itemDao.beginLoad();
        this.conceptDao.beginLoad();
    }

    public abstract void save(TagApp tagApp) throws DaoException;

    public void endLoad() throws DaoException {
        this.tagAppDao.endLoad();
        this.userDao.endLoad();
        this.itemDao.endLoad();
        this.conceptDao.endLoad();
    }
}
