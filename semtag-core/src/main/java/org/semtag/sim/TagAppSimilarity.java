package org.semtag.sim;

import com.typesafe.config.Config;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import org.semtag.dao.DaoException;
import org.semtag.dao.DaoFilter;
import org.semtag.dao.TagAppDao;
import org.semtag.model.TagApp;
import org.wikapidia.conf.Configuration;
import org.wikapidia.conf.ConfigurationException;
import org.wikapidia.conf.Configurator;

/**
 * @author Ari Weiland
 */
public class TagAppSimilarity implements Similar<TagApp> {

    private final TagAppDao helperDao;
    private final ConceptSimilarity sim;

    public TagAppSimilarity(TagAppDao helperDao, ConceptSimilarity sim) {
        this.helperDao = helperDao;
        this.sim = sim;
    }

    public TagAppDao getHelperDao() {
        return helperDao;
    }

    public ConceptSimilarity getSim() {
        return sim;
    }

    @Override
    public double similarity(TagApp x, TagApp y) throws DaoException {
        return sim.similarity(x.getConceptId(), y.getConceptId());
    }

    @Override
    public SimilarResultList mostSimilar(TagApp obj, int maxResults) throws DaoException {
        SimilarResultList concepts = sim.mostSimilar(obj.getConceptId(), maxResults);
        TIntSet conceptIds = new TIntHashSet();
        for (SimilarResult result : concepts) {
            conceptIds.add(result.getIntId());
        }
        Iterable<TagApp> iterable = helperDao.get(new DaoFilter().setConceptIds(conceptIds.toArray()));
        SimilarResultList list = new SimilarResultList(maxResults);
        for (TagApp t : iterable) {
            list.add(new SimilarResult(t.getTagAppId(), concepts.getValue(t.getConceptId())));
        }
        list.lock();
        return list;
    }

    @Override
    public double[][] cosimilarity(TagApp[] objs) throws DaoException {
        int[] ids = new int[objs.length];
        for (int i=0; i<objs.length; i++) {
            ids[i] = objs[i].getConceptId();
        }
        return sim.cosimilarity(ids);
    }

    public static class Provider extends org.wikapidia.conf.Provider<TagAppSimilarity> {
        public Provider(Configurator configurator, Configuration config) throws ConfigurationException {
            super(configurator, config);
        }

        @Override
        public Class getType() {
            return TagAppSimilarity.class;
        }

        @Override
        public String getPath() {
            return "sem-tag.similar.tagApp";
        }

        @Override
        public TagAppSimilarity get(String name, Config config) throws ConfigurationException {
            if (!config.getString("type").equals("tagApp")) {
                return null;
            }
            return new TagAppSimilarity(
                    getConfigurator().get(TagAppDao.class, config.getString("tagAppDao")),
                    getConfigurator().get(ConceptSimilarity.class, config.getString("conceptSim"))
            );
        }
    }
}
