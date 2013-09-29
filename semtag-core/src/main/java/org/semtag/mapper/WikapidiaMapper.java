package org.semtag.mapper;

import com.typesafe.config.Config;
import org.semtag.SemTagException;
import org.semtag.dao.DaoException;
import org.semtag.dao.DaoFilter;
import org.semtag.dao.TagAppDao;
import org.semtag.model.*;
import org.semtag.model.concept.Concept;
import org.wikapidia.conf.Configuration;
import org.wikapidia.conf.ConfigurationException;
import org.wikapidia.conf.Configurator;
import org.wikapidia.core.lang.Language;
import org.wikapidia.core.lang.LocalId;
import org.wikapidia.core.lang.LocalString;
import org.wikapidia.sr.disambig.Disambiguator;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * A ConceptMapper subclass that maps TagApps to a Wikipedia local page.
 *
 * @author Yulun Li
 * @author Ari Weiland
 */
public class WikapidiaMapper implements ConceptMapper {

    private final Language language;
    private final Disambiguator disambiguator;
    private final TagAppDao tagAppDao;

    public WikapidiaMapper(Language language, Disambiguator disambiguator, TagAppDao tagAppDao) {
        this.language = language;
        this.disambiguator = disambiguator;
        this.tagAppDao = tagAppDao;
    }

    public Language getLanguage() {
        return language;
    }

    public Disambiguator getDisambiguator() {
        return disambiguator;
    }

    public TagAppDao getTagAppDao() {
        return tagAppDao;
    }

    @Override
    public TagApp map(User user, Tag tag, Item item, Timestamp timestamp) throws SemTagException {
        try {
            Set<LocalString> context = new HashSet<LocalString>();
            TagAppGroup group = tagAppDao.getGroup(new DaoFilter().setItemId(item.getItemId()));
            for (TagApp t : group) {
                context.add(new LocalString(language, t.getTag().getNormalizedTag()));
            }
            LocalString tagString = new LocalString(language, tag.getNormalizedTag());
            LocalId localId = disambiguator.disambiguate(tagString, context);
            if (localId == null) { // TODO: this is not ideal, what should we do?
                localId = new LocalId(language, -1);
            }
            return new TagApp(user, tag, item, timestamp, new Concept(localId));
        } catch (org.wikapidia.core.dao.DaoException e) {
            throw new SemTagException(e);
        } catch (DaoException e) {
            throw new SemTagException(e);
        }
    }

    @Override
    public Concept map(Tag tag) throws SemTagException {
        try {
            LocalString tagString = new LocalString(language, tag.getNormalizedTag());
            LocalId localId = disambiguator.disambiguate(tagString, null);
            if (localId == null) {
                tag.setConcept(null);
                return null;
            } else {
                tag.setConcept(new Concept(localId));
                return tag.getConcept();
            }
        } catch (org.wikapidia.core.dao.DaoException e) {
            throw new SemTagException(e);
        }

    }

    public static class Provider extends org.wikapidia.conf.Provider<ConceptMapper> {
        public Provider(Configurator configurator, Configuration config) throws ConfigurationException {
            super(configurator, config);
        }

        @Override
        public Class getType() {
            return ConceptMapper.class;
        }

        @Override
        public String getPath() {
            return "sem-tag.concept";
        }

        @Override
        public WikapidiaMapper get(String name, Config config) throws ConfigurationException {
            if (!config.getString("type").equals("wikapidia")) {
                return null;
            }
            return new WikapidiaMapper(
                    Language.getByLangCode(config.getString("lang")),
                    getConfigurator().get(Disambiguator.class, config.getString("disambiguator")),
                    getConfigurator().get(TagAppDao.class, config.getString("tagAppDao")));
        }
    }
}
