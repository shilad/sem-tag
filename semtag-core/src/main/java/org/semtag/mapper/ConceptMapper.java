package org.semtag.mapper;

import org.semtag.SemTagException;
import org.semtag.core.model.Item;
import org.semtag.core.model.Tag;
import org.semtag.core.model.TagApp;
import org.semtag.core.model.User;
import org.wikapidia.conf.Configurator;

import java.sql.Timestamp;

/**
 * @author Ari Weiland
 * @author Yulun Li
 */
public abstract class ConceptMapper {

    protected final Configurator configurator;

    public ConceptMapper(Configurator configurator) {
        this.configurator = configurator;
    }

    public TagApp mapTagApp(String userId, String tag, String itemId, Timestamp timestamp) throws SemTagException {
        return mapTagApp(
                new User(userId),
                new Tag(tag),
                new Item(itemId),
                timestamp);
    }

    protected abstract TagApp mapTagApp(User user, Tag tag, Item item, Timestamp timestamp) throws SemTagException;
}
