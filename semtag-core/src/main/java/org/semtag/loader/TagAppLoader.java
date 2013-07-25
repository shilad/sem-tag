package org.semtag.loader;

import org.semtag.core.dao.DaoException;
import org.semtag.core.dao.ItemDao;
import org.semtag.core.dao.TagAppDao;
import org.semtag.core.dao.UserDao;
import org.semtag.core.model.Item;
import org.semtag.core.model.TagApp;
import org.semtag.core.model.User;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Ari Weiland
 * @author Yulun Li
 */
public class TagAppLoader {

    private static final Logger LOG = Logger.getLogger(TagAppLoader.class.getName());
    private final AtomicInteger counter = new AtomicInteger();
    private final TagAppDao tagAppDao;
    private final UserDao userDao;
    private final ItemDao itemDao;

    public TagAppLoader(TagAppDao tagAppDao) {
        this.tagAppDao = tagAppDao;
        this.userDao = new userDao();
        this.itemDao = new itemDao();
    }

    // TODO: add context

    /**
     * Call this method to load a tagApp into the semtag db.
     * @param tagApp
     */
    public void add(TagApp tagApp) throws DaoException {
        try {
            tagAppDao.save(tagApp);
        } catch (DaoException e) {
            LOG.log(Level.WARNING, "adding of tag application <" + tagApp + "> failed", e);
        }

        User user = tagApp.getUser();
        try {
            userDao.save(user);
        } catch (DaoException e) {
            LOG.log(Level.WARNING, "adding of user " + user.getUserId() + " failed", e);
        }

        Item item = tagApp.getItem();
        try {
            itemDao.save(item);
        } catch (DaoException e) {
            LOG.log(Level.WARNING, "adding of item " + item.getItemId() + " failed", e);
        }
    }

}
