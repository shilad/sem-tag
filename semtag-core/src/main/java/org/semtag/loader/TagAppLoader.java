package org.semtag.loader;

import org.semtag.core.dao.*;
import org.semtag.core.model.Item;
import org.semtag.core.model.TagApp;
import org.semtag.core.model.User;
import org.semtag.core.model.concept.Concept;

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
    private final ConceptDao conceptDao;

    public TagAppLoader(TagAppDao tagAppDao, UserDao userDao, ItemDao itemDao, ConceptDao conceptDao) {
        this.tagAppDao = tagAppDao;
        this.userDao = userDao;
        this.itemDao = itemDao;
        this.conceptDao = conceptDao;
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
            LOG.log(Level.WARNING, "adding of item " + item.getItemId() + " failed", e);  //TODO: use a transaction model, set autocommit false, if there is an exception, roll back the previous changes
        }

        Concept concept = tagApp.getConcept();

    }

}
