package org.semtag.mapper;

import org.semtag.SemTagException;
import org.semtag.core.model.Item;
import org.semtag.core.model.TagApp;
import org.semtag.core.model.User;
import org.semtag.core.model.concept.Concept;
import org.wikapidia.conf.Configurator;

import java.sql.Timestamp;

/**
 * This class serves two purposes. The primary purpose is during the load process,
 * the {@code mapTagApp} method takes in the TagApp parameters, maps them to an
 * appropriate concept, and returns the assembled TagApp. Additionally, the
 * {@code getConcept} method is used by the ConceptDao to assemble the appropriate
 * concept from the information stored in the database. A subclass should implement
 * these methods to refer to a specific subclass of {@link Concept}.
 *
 * This class must be passed to both the loader and any instance of a ConceptDao.
 *
 * @author Ari Weiland
 * @author Yulun Li
 */
public abstract class ConceptMapper {

    protected final Configurator configurator;

    public ConceptMapper(Configurator configurator) {
        this.configurator = configurator;
    }

    public abstract TagApp mapTagApp(User user, String tag, Item item, Timestamp timestamp) throws SemTagException;

    /**
     * This method is used by the ConceptDao to retrieve the correct type of concept
     * based on the information in the database.
     * @param conceptId
     * @param metric
     * @param objBytes
     * @return
     */
    public abstract Concept getConcept(int conceptId, String metric, byte[] objBytes);
}
