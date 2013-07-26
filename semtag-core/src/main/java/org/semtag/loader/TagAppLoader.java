package org.semtag.loader;

import org.semtag.SemTagException;
import org.semtag.core.dao.*;
import org.semtag.core.model.Item;
import org.semtag.core.model.Tag;
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
     * @param rawTagString
     * @param itemId
     * @param timestamp
     * @throws DaoException
     * @throws SemTagException
     */
    public void add(String userId, String rawTagString, String itemId, Timestamp timestamp) throws SemTagException {

        User user = new User(userId);
        Tag tag = new Tag(rawTagString);
        Item item = new Item(itemId);

        TagApp tagApp = mapper.mapTagApp(user, tag, item, timestamp);
        Concept concept = tagApp.getConcept();

        try {
            tagAppDao.save(tagApp);
            userDao.save(user);
            itemDao.save(item);
            conceptDao.save(concept);
        } catch (DaoException e) {
            // TODO: implement transaction model so we save all or nothing
            throw new SemTagException(e);
        }
    }
}
