package org.semtag.loader;

import org.semtag.SemTagException;
import org.semtag.core.dao.*;
import org.semtag.core.model.Item;
import org.semtag.core.model.TagApp;
import org.semtag.core.model.User;
import org.semtag.core.model.concept.Concept;
import org.semtag.mapper.ConceptMapper;

import java.sql.Timestamp;
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
    private final ConceptMapper mapper;

    public TagAppLoader(TagAppDao tagAppDao, UserDao userDao, ItemDao itemDao, ConceptDao conceptDao, ConceptMapper mapper) {
        this.tagAppDao = tagAppDao;
        this.userDao = userDao;
        this.itemDao = itemDao;
        this.conceptDao = conceptDao;
        this.mapper = mapper;
    }

    /**
     * Call this method to load a tagApp into the semtag db.
     * @param userId
     * @param tag
     * @param itemId
     * @param timestamp
     * @throws DaoException
     * @throws SemTagException
     */
    public void add(String userId, String tag, String itemId, Timestamp timestamp) throws SemTagException {

        TagApp tagApp = mapper.mapTagApp(userId, tag, itemId, timestamp);
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
        try {
            conceptDao.save(concept);
        } catch (DaoException e) {
            LOG.log(Level.WARNING, "adding of item " + concept.toString() + " failed", e);  //TODO: use a transaction model, set autocommit false, if there is an exception, roll back the previous changes
        }
    }

}
