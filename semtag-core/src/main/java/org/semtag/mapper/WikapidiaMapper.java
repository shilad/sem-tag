package org.semtag.mapper;

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
 * A ConceptMapper subclass that maps TagApps to
 * @author Yulun Li
 * @author Ari Weiland
 */
public class WikapidiaMapper implements ConceptMapper {

    public static final Language LANGUAGE = Language.getByLangCode("en");

    protected final Configurator configurator;
    protected final Disambiguator disambiguator;
    protected final TagAppDao tagAppDao;

    public WikapidiaMapper(Configurator configurator, Disambiguator disambiguator, TagAppDao tagAppDao) {
        this.configurator = configurator;
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
    public TagApp mapTagApp(User user, Tag tag, Item item, Timestamp timestamp) throws SemTagException {
        try {
            Set<LocalString> context = new HashSet<LocalString>();
            if (tagAppDao != null) {
                TagAppGroup group = tagAppDao.getGroup(new DaoFilter().setItemId(item.getItemId()));
                for (TagApp tagApp : group) {
                    context.add(new LocalString(LANGUAGE, tagApp.getTag().toString()));
                }
            }
            LocalString tagString = new LocalString(LANGUAGE, tag.toString());
            LocalId conceptObj = disambiguator.disambiguate(tagString, context);
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
                    getConfigurator().get(Disambiguator.class, config.getString("disambiguator")),
                    getConfigurator().get(TagAppDao.class, config.getString("tagAppDao"))
            );
        }
    }
}
