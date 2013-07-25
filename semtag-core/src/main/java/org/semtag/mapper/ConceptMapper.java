package org.semtag.mapper;

import org.semtag.SemTagException;
import org.semtag.core.model.Item;
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

    public abstract TagApp mapTagApp(User user, String tag, Item item, Timestamp timestamp) throws SemTagException;
}
