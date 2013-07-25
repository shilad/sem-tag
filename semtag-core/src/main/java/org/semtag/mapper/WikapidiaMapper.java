package org.semtag.mapper;

import org.semtag.SemTagException;
import org.semtag.core.model.Item;
import org.semtag.core.model.TagApp;
import org.semtag.core.model.User;
import org.semtag.core.model.concept.Concept;
import org.semtag.core.model.concept.WikapidiaConcept;
import org.wikapidia.conf.ConfigurationException;
import org.wikapidia.conf.Configurator;
import org.wikapidia.core.dao.DaoException;
import org.wikapidia.core.lang.Language;
import org.wikapidia.core.lang.LocalId;
import org.wikapidia.core.lang.LocalString;
import org.wikapidia.sr.LocalSRMetric;
import org.wikapidia.sr.disambig.Disambiguator;

import java.sql.Timestamp;
import java.util.Set;

/**
 * @author Yulun Li
 */
public class WikapidiaMapper extends ConceptMapper {

    public static final Language LANGUAGE = Language.getByLangCode("en");

    protected Disambiguator disambiguator;

    public WikapidiaMapper(Configurator configurator) {
        super(configurator);
    }

    @Override
    public TagApp mapTagApp(User user, String tag, Item item, Timestamp timestamp) throws SemTagException {
        Set<LocalString> context = null;
        LocalString tagString = new LocalString(LANGUAGE, tag);
        try {
            LocalId conceptObj = disambiguator.disambiguate(tagString, context);
            Concept concept = new WikapidiaConcept(conceptObj, configurator.get(LocalSRMetric.class));
        } catch (DaoException e) {
            throw new SemTagException(e);
        } catch (ConfigurationException e) {
            throw new SemTagException(e);
        }
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
