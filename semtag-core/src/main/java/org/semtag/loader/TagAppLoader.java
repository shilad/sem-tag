package org.semtag.loader;

import org.semtag.core.dao.TagAppDao;
import org.semtag.core.model.TagApp;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 *
 */
public class TagAppLoader {

    private static final Logger LOG = Logger.getLogger(TagAppLoader.class.getName());
    private final AtomicInteger counter = new AtomicInteger();
    private final TagAppDao tagAppDao;

    public TagAppLoader(TagAppDao tagAppDao) {
        this.tagAppDao = tagAppDao;
    }

    public void add(TagApp tagApp) {

    }

}
