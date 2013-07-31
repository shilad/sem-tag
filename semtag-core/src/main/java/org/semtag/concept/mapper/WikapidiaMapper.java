package org.semtag.concept.mapper;

import com.typesafe.config.Config;
import org.semtag.SemTagException;
import org.semtag.core.dao.DaoException;
import org.semtag.core.dao.DaoFilter;
import org.semtag.core.dao.TagAppDao;
import org.semtag.core.model.*;
import org.semtag.core.model.concept.Concept;
import org.semtag.core.model.concept.WikapidiaConcept;
import org.wikapidia.conf.Configuration;
import org.wikapidia.conf.ConfigurationException;
import org.wikapidia.conf.Configurator;
import org.wikapidia.core.lang.Language;
import org.wikapidia.core.lang.LocalId;
import org.wikapidia.core.lang.LocalString;
import org.wikapidia.sr.LocalSRMetric;
import org.wikapidia.sr.disambig.Disambiguator;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * A ConceptMapper subclass that maps TagApps to a Wikipedia local page.
 * @author Yulun Li
 * @author Ari Weiland
 */
public class WikapidiaMapper implements ConceptMapper {

    private final Configurator configurator;
    private final Language language;
    private final Disambiguator disambiguator;
    private final TagAppDao tagAppDao;

    public WikapidiaMapper(Configurator configurator, Language language, Disambiguator disambiguator, TagAppDao tagAppDao) {
        this.configurator = configurator;
        this.language = language;
        this.disambiguator = disambiguator;
        this.tagAppDao = tagAppDao;
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
            if (tagAppDao != null) {
                TagAppGroup group = tagAppDao.getGroup(new DaoFilter().setItemId(item.getItemId()));
                for (TagApp t : group) {
                    context.add(new LocalString(language, t.getTag().getNormalizedTag()));
                }
            }
            LocalString tagString = new LocalString(language, tag.getNormalizedTag());
            LocalId conceptObj = disambiguator.disambiguate(tagString, context);
            if (conceptObj == null) { // TODO: this is not ideal, what should we do?
                conceptObj = new LocalId(language, -1);
            }
            Concept concept = new WikapidiaConcept(conceptObj, configurator.get(LocalSRMetric.class));
            return new TagApp(user, tag, item, timestamp, concept);
        } catch (ConfigurationException e) {
            throw new SemTagException(e);
        } catch (org.wikapidia.core.dao.DaoException e) {
            throw new SemTagException(e);
        } catch (DaoException e) {
            throw new SemTagException(e);
        }
    }

    @Override
    public Concept getConcept(int conceptId, String metric, byte[] objBytes) throws DaoException {
        try {
            return new WikapidiaConcept(
                    conceptId,
                    configurator.get(LocalSRMetric.class, metric),
                    objBytes
            );
        } catch (ConfigurationException e) {
            throw new DaoException(e);
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
                    getConfigurator(),
                    Language.getByLangCode(config.getString("language")),
                    getConfigurator().get(Disambiguator.class, config.getString("disambiguator")),
                    getConfigurator().get(TagAppDao.class, config.getString("tagAppDao"))
            );
        }
    }
}
