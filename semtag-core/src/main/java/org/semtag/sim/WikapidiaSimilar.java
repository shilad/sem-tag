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
 * Defines ConceptSimilar methods for WikapidiaConcepts.
 *
 * @author Ari Weiland
 */
public class WikapidiaSimilar implements ConceptSimilar {

    private final ConceptDao helperDao;
    private final Language language;
    private final LocalSRMetric srMetric;

    protected WikapidiaSimilar(ConceptDao helperDao, Language language, LocalSRMetric srMetric) {
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
        try {
            SRResult result = srMetric.similarity(
                    x.getLocalId().asLocalPage(),
                    y.getLocalId().asLocalPage(),
                    false);
            return result.getScore();
        } catch (org.wikapidia.core.dao.DaoException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public SimilarResultList<Concept> mostSimilar(Concept concept, int maxResults) throws DaoException {
        return mostSimilar(concept, maxResults, 0);
    }

    @Override
    public SimilarResultList<Concept> mostSimilar(Concept concept, int maxResults, double threshold) throws DaoException {
        Iterable<Concept> concepts = helperDao.get(new DaoFilter());
        TIntSet validIds = new TIntHashSet();
        for (Concept c : concepts) {
            if (c.getConceptId() != -1) {
                validIds.add(c.getLocalId().getId());
            }
        }
        SRResultList results;
        try {
            results = srMetric.mostSimilar(
                    concept.getLocalId().asLocalPage(),
                    maxResults,
                    validIds);
        } catch (org.wikapidia.core.dao.DaoException e) {
            throw new DaoException(e);
        }
        return buildSimilarResultList(results, maxResults, threshold);
    }

    private SimilarResultList<Concept> buildSimilarResultList(SRResultList results, int maxResults, double threshold) {
        SimilarResultList<Concept> list = new SimilarResultList<Concept>(maxResults, threshold);
        for (SRResult r : results) {
            if (r.getId() > -1) {
                LocalId localId = new LocalId(language, r.getId());
                Concept concept = new Concept(localId);
                list.add(new SimilarResult<Concept>(concept.getConceptId(), concept, r.getScore()));
            }
        }
        list.lock();
        return list;
    }

    @Override
    public double[][] cosimilarity(Concept[] concepts) throws DaoException {
        try {
            return srMetric.cosimilarity(getIdVector(concepts), language);
        } catch (org.wikapidia.core.dao.DaoException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public double[][] cosimilarity(Concept[] xConcepts, Concept[] yConcepts) throws DaoException {
        try {
            return srMetric.cosimilarity(getIdVector(xConcepts), getIdVector(yConcepts), language);
        } catch (org.wikapidia.core.dao.DaoException e) {
            throw new DaoException(e);
        }
    }

    private int[] getIdVector(Concept[] concepts) {
        int[] ids = new int[concepts.length];
        for (int i=0; i<concepts.length; i++) {
            ids[i] = concepts[i].getLocalId().getId();
        }
        return ids;
    }

    public static class Provider extends org.wikapidia.conf.Provider<Similar<Concept>> {
        public Provider(Configurator configurator, Configuration config) throws ConfigurationException {
            super(configurator, config);
        }

        @Override
        public Class getType() {
            return ConceptSimilar.class;
        }

        @Override
        public String getPath() {
            return "sem-tag.sim.concept";
        }

        @Override
        public WikapidiaSimilar get(String name, Config config) throws ConfigurationException {
            if (!config.getString("type").equals("wikapidia")) {
                return null;
            }
            return new WikapidiaSimilar(
                    getConfigurator().get(ConceptDao.class, config.getString("conceptDao")),
                    Language.getByLangCode(config.getString("lang")),
                    getConfigurator().get(LocalSRMetric.class, config.getString("metric"))
            );
        }
    }
}
