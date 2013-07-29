package org.semtag.loader;

import org.semtag.SemTagException;
import org.semtag.core.dao.DaoException;
import org.semtag.core.dao.SaveHandler;
import org.semtag.core.model.Item;
import org.semtag.core.model.Tag;
import org.semtag.core.model.TagApp;
import org.semtag.core.model.User;
import org.semtag.core.model.concept.Concept;
import org.semtag.mapper.ConceptMapper;

import java.sql.Timestamp;
import java.util.logging.Logger;

/**
 * @author Ari Weiland
 * @author Yulun Li
 */
public class TagAppLoader {

    private static final Logger LOG = Logger.getLogger(TagAppLoader.class.getName());
    private final SaveHandler handler;
    private final ConceptMapper mapper;

    public TagAppLoader(SaveHandler handler, ConceptMapper mapper) {
        this.handler = handler;
        this.mapper = mapper;
    }

    public void beginLoad() throws SemTagException {
        try {
            handler.beginLoad();
        } catch (DaoException e) {
            throw new SemTagException(e);
        }
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
            handler.save(tagApp);
        } catch (DaoException e) {
            throw new SemTagException(e);
        }
    }

    public void endLoad() throws SemTagException {
        try {
            handler.endLoad();
        } catch (DaoException e) {
            throw new SemTagException(e);
        }
    }
}
