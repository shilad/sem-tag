package org.semtag.mapper;

import org.semtag.core.model.Item;
import org.semtag.core.model.TagApp;
import org.semtag.core.model.User;

import java.sql.Timestamp;

/**
 * @author Ari Weiland
 */
public abstract class TagAppMapper {

    public TagAppMapper() {
    }

    public abstract TagApp mapTagApp(User user, String tag, Item item, Timestamp timestamp);
}
