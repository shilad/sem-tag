package org.semtag.core.sim;

import com.typesafe.config.Config;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import org.semtag.core.dao.ConceptDao;
import org.semtag.core.dao.DaoException;
import org.semtag.core.dao.DaoFilter;
import org.semtag.core.model.concept.Concept;
import org.semtag.core.model.concept.WikapidiaConcept;
import org.wikapidia.conf.Configuration;
import org.wikapidia.conf.ConfigurationException;
import org.wikapidia.conf.Configurator;
import org.wikapidia.core.lang.Language;
import org.wikapidia.sr.LocalSRMetric;
import org.wikapidia.sr.SRResult;
import org.wikapidia.sr.SRResultList;

/**
 * @author Ari Weiland
 */
public class WikapidiaSimilarity implements ConceptSimilarity<WikapidiaConcept> {

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
    public double similarity(WikapidiaConcept x, WikapidiaConcept y) throws DaoException {
        try {
            SRResult result = srMetric.similarity(
                    x.getConceptObj().asLocalPage(),
                    y.getConceptObj().asLocalPage(),
                    false);
            return result.getValue();
        } catch (org.wikapidia.core.dao.DaoException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public SimilarResultList mostSimilar(WikapidiaConcept obj, int maxResults) throws DaoException {
        Iterable<Concept> concepts = helperDao.get(new DaoFilter());
        TIntSet validIds = new TIntHashSet();
        for (Concept c : concepts) {
            validIds.add(c.getConceptId());
        }
        SRResultList results;
        try {
            results = srMetric.mostSimilar(
                    obj.getConceptObj().asLocalPage(),
                    maxResults,
                    validIds);
        } catch (org.wikapidia.core.dao.DaoException e) {
            throw new DaoException(e);
        }
        SimilarResultList list = new SimilarResultList(maxResults);
        for (SRResult r : results) {
            list.add(new SimilarResult(r.getId(), r.getValue()));
        }
        list.lock();
        return list;
    }

    @Override
    public double[][] cosimilarity(WikapidiaConcept[] objs) throws DaoException {
        int[] ids = new int[objs.length];
        for (int i=0; i<objs.length; i++) {
            ids[i] = objs[i].getConceptId();
        }
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
