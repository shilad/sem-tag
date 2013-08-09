package org.semtag.sim;

import com.typesafe.config.Config;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import org.semtag.dao.DaoException;
import org.semtag.dao.DaoFilter;
import org.semtag.dao.TagAppDao;
import org.semtag.model.Tag;
import org.semtag.model.TagApp;
import org.wikapidia.conf.Configuration;
import org.wikapidia.conf.ConfigurationException;
import org.wikapidia.conf.Configurator;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines Similar methods for TagApps.
 *
 * @author Ari Weiland
 */
public class TagAppSimilar implements Similar<TagApp> {

    private final TagAppDao helperDao;
    private final ConceptSimilar sim;

    public TagAppSimilar(TagAppDao helperDao, ConceptSimilar sim) {
        this.helperDao = helperDao;
        this.sim = sim;
    }

    public TagAppDao getHelperDao() {
        return helperDao;
    }

    public ConceptSimilar getSim() {
        return sim;
    }

    @Override
    public double similarity(TagApp x, TagApp y) throws DaoException {
        return sim.similarity(x.getConceptId(), y.getConceptId());
    }

    @Override
    public SimilarResultList mostSimilar(TagApp obj, int maxResults) throws DaoException {
        return mostSimilar(obj, maxResults, 0);
    }

    @Override
    public SimilarResultList mostSimilar(TagApp obj, int maxResults, double threshold) throws DaoException {
        SimilarResultList concepts = sim.mostSimilar(obj.getConceptId(), maxResults);
        TIntSet conceptIds = new TIntHashSet();
        for (SimilarResult result : concepts) {
            conceptIds.add(result.getIntId());
        }
        Iterable<TagApp> iterable = helperDao.get(new DaoFilter().setConceptIds(conceptIds.toArray()));
        Map<String, TagApp> tags = new HashMap<String, TagApp>();
        for (TagApp t : iterable) {
            tags.put(t.getTag().getNormalizedTag(), t);
        }
        SimilarResultList list = new SimilarResultList(maxResults, threshold);
        for (TagApp t : tags.values()) {
            list.add(new SimilarResult(t.getTagAppId(), t, concepts.getValue(t.getConceptId())));
        }
        list.lock();
        return list;
    }

    /**
     * Returns a list of the most similar raw tags to this tag.
     * Note that this method is 3-6 times slower than the mostSimilar(TagApp),
     * and you should try to use that one when possible.
     * @param tag
     * @param maxResults
     * @return
     * @throws DaoException
     */
    public SimilarResultList mostSimilar(Tag tag, int maxResults) throws DaoException {
        return mostSimilar(tag, maxResults, 0);
    }

    /**
     * Returns a list of the most similar raw tags to this tag that pass the threshold.
     * Note that this method is 3-6 times slower than the mostSimilar(TagApp),
     * and you should try to use that one when possible.
     * @param tag
     * @param maxResults
     * @return
     * @throws DaoException
     */
    public SimilarResultList mostSimilar(Tag tag, int maxResults, double threshold) throws DaoException {
        SimilarResultList concepts = sim.mostSimilar(tag, maxResults);
        TIntSet conceptIds = new TIntHashSet();
        for (SimilarResult result : concepts) {
            conceptIds.add(result.getIntId());
        }
        Iterable<TagApp> iterable = helperDao.get(new DaoFilter().setConceptIds(conceptIds.toArray()));
        Map<String, TagApp> tags = new HashMap<String, TagApp>();
        for (TagApp t : iterable) {
            tags.put(t.getTag().getNormalizedTag(), t);
        }
        SimilarResultList list = new SimilarResultList(maxResults, threshold);
        for (TagApp t : tags.values()) {
            list.add(new SimilarResult<TagApp>(t.getTagAppId(), t, concepts.getValue(t.getConceptId())));
        }
        list.lock();
        return list;
    }

    @Override
    public double[][] cosimilarity(TagApp[] objs) throws DaoException {
        return sim.cosimilarity(getVector(objs));
    }

    @Override
    public double[][] cosimilarity(TagApp[] xObjs, TagApp[] yObjs) throws DaoException {
        return sim.cosimilarity(getVector(xObjs), getVector(yObjs));
    }

    private int[] getVector(TagApp[] objs) {
        int[] ids = new int[objs.length];
        for (int i=0; i<objs.length; i++) {
            ids[i] = objs[i].getConceptId();
        }
        return ids;
    }

    public static class Provider extends org.wikapidia.conf.Provider<TagAppSimilar> {
        public Provider(Configurator configurator, Configuration config) throws ConfigurationException {
            super(configurator, config);
        }

        @Override
        public Class getType() {
            return TagAppSimilar.class;
        }

        @Override
        public String getPath() {
            return "sem-tag.sim.tagApp";
        }

        @Override
        public TagAppSimilar get(String name, Config config) throws ConfigurationException {
            if (!config.getString("type").equals("tagApp")) {
                return null;
            }
            return new TagAppSimilar(
                    getConfigurator().get(TagAppDao.class, config.getString("tagAppDao")),
                    getConfigurator().get(ConceptSimilar.class, config.getString("conceptSim"))
            );
        }
    }
}
