package org.semtag.sim;

import com.typesafe.config.Config;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import org.semtag.dao.ConceptDao;
import org.semtag.dao.DaoException;
import org.semtag.dao.DaoFilter;
import org.semtag.model.concept.Concept;
import org.wikapidia.conf.Configuration;
import org.wikapidia.conf.ConfigurationException;
import org.wikapidia.conf.Configurator;
import org.wikapidia.core.lang.Language;
import org.wikapidia.core.lang.LocalId;
import org.wikapidia.sr.LocalSRMetric;
import org.wikapidia.sr.SRResult;
import org.wikapidia.sr.SRResultList;

/**
 * @author Ari Weiland
 */
public class WikapidiaSimilarity implements ConceptSimilarity {

    private final ConceptDao helperDao;
    private final Language language;
    private final LocalSRMetric srMetric;

    protected WikapidiaSimilarity(ConceptDao helperDao, Language language, LocalSRMetric srMetric) {
        this.helperDao = helperDao;
        this.language = language;
        this.srMetric = srMetric;
    }

    public ConceptDao getHelperDao() {
        return helperDao;
    }

    public Language getLanguage() {
        return language;
    }

    public LocalSRMetric getSrMetric() {
        return srMetric;
    }

    @Override
    public double similarity(Concept x, Concept y) throws DaoException {
        if (x.equals(y)) {
            return 1.0;
        }
        return similarity(x.getConceptId(), y.getConceptId());
    }

    @Override
    public double similarity(int xId, int yId) throws DaoException {
        if (xId == yId) {
            return 1.0;
        }
        try {
            SRResult result = srMetric.similarity(
                    new LocalId(language, xId).asLocalPage(),
                    new LocalId(language, yId).asLocalPage(),
                    false);
            return result.getScore();
        } catch (org.wikapidia.core.dao.DaoException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public SimilarResultList mostSimilar(Concept obj, int maxResults) throws DaoException {
        return mostSimilar(obj.getConceptId(), maxResults);
    }

    @Override
    public SimilarResultList mostSimilar(int id, int maxResults) throws DaoException {
        Iterable<Concept> concepts = helperDao.get(new DaoFilter());
        TIntSet validIds = new TIntHashSet();
        for (Concept c : concepts) {
            validIds.add(c.getConceptId());
        }
        SRResultList results;
        try {
            results = srMetric.mostSimilar(
                    new LocalId(language, id).asLocalPage(),
                    maxResults,
                    validIds);
        } catch (org.wikapidia.core.dao.DaoException e) {
            throw new DaoException(e);
        }
        SimilarResultList list = new SimilarResultList(maxResults);
        for (SRResult r : results) {
            if (r.getId() > -1) {
                list.add(new SimilarResult(r.getId(), r.getScore()));
            }
        }
        list.lock();
        return list;
    }

    @Override
    public double[][] cosimilarity(Concept[] objs) throws DaoException {
        int[] ids = new int[objs.length];
        for (int i=0; i<objs.length; i++) {
            ids[i] = objs[i].getConceptId();
        }
        return cosimilarity(ids);
    }

    @Override
    public double[][] cosimilarity(int[] ids) throws DaoException {
        try {
            return srMetric.cosimilarity(ids, language);
        } catch (org.wikapidia.core.dao.DaoException e) {
            throw new DaoException(e);
        }
    }

    public static class Provider extends org.wikapidia.conf.Provider<ConceptSimilarity> {
        public Provider(Configurator configurator, Configuration config) throws ConfigurationException {
            super(configurator, config);
        }

        @Override
        public Class getType() {
            return ConceptSimilarity.class;
        }

        @Override
        public String getPath() {
            return "sem-tag.similar.concept";
        }

        @Override
        public WikapidiaSimilarity get(String name, Config config) throws ConfigurationException {
            if (!config.getString("type").equals("wikapidia")) {
                return null;
            }
            return new WikapidiaSimilarity(
                    getConfigurator().get(ConceptDao.class, config.getString("conceptDao")),
                    Language.getByLangCode(config.getString("lang")),
                    getConfigurator().get(LocalSRMetric.class, config.getString("srMetric"))
            );
        }
    }
}
