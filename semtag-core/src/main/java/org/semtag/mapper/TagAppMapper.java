package org.semtag.mapper;

import org.apache.commons.lang.StringUtils;
import org.semtag.core.model.Item;
import org.semtag.core.model.TagApp;
import org.semtag.core.model.User;

import java.sql.Timestamp;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Ari Weiland
 */
public abstract class TagAppMapper {

    protected static final AtomicInteger TAGAPP_ID_COUNTER = new AtomicInteger(0);

    public TagAppMapper() {
    }

    public abstract TagApp mapTagApp(User user, String tag, Item item, Timestamp timestamp);
}
