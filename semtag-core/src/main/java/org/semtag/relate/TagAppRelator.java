package org.semtag.relate;

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
 * @author Ari Weiland
 */
public class TagAppRelator implements Relator<TagApp> {

    private final TagAppDao helperDao;
    private final ConceptRelator relator;

    public TagAppRelator(TagAppDao helperDao, ConceptRelator relator) {
        this.helperDao = helperDao;
        this.relator = relator;
    }

    public TagAppDao getHelperDao() {
        return helperDao;
    }

    public ConceptRelator getRelator() {
        return relator;
    }

    @Override
    public double relatedness(TagApp x, TagApp y) throws DaoException {
        return relator.relatedness(x.getConceptId(), y.getConceptId());
    }

    @Override
    public RelatedResultList mostRelated(TagApp obj, int maxResults) throws DaoException {
        return mostRelated(obj, maxResults, 0);
    }

    @Override
    public RelatedResultList mostRelated(TagApp obj, int maxResults, double threshold) throws DaoException {
        RelatedResultList concepts = relator.mostRelated(obj.getConceptId(), maxResults);
        TIntSet conceptIds = new TIntHashSet();
        for (RelatedResult result : concepts) {
            conceptIds.add(result.getIntId());
        }
        Iterable<TagApp> iterable = helperDao.get(new DaoFilter().setConceptIds(conceptIds.toArray()));
        Map<String, TagApp> tags = new HashMap<String, TagApp>();
        for (TagApp t : iterable) {
            tags.put(t.getTag().getNormalizedTag(), t);
        }
        RelatedResultList list = new RelatedResultList(maxResults, threshold);
        for (TagApp t : tags.values()) {
            list.add(new RelatedResult(t.getTagAppId(), obj, concepts.getValue(t.getConceptId())));
        }
        list.lock();
        return list;
    }

    /**
     * Returns a list of the most related raw tags to this tag.
     * @param tag
     * @param maxResults
     * @return
     * @throws DaoException
     */
    public RelatedResultList mostRelated(Tag tag, int maxResults) throws DaoException {
        return mostRelated(tag, maxResults, 0);
    }

    /**
     * Returns a list of the most related raw tags to this tag that pass the threshold.
     * @param tag
     * @param maxResults
     * @return
     * @throws DaoException
     */
    public RelatedResultList mostRelated(Tag tag, int maxResults, double threshold) throws DaoException {
        RelatedResultList concepts = relator.mostRelated(tag, maxResults);
        TIntSet conceptIds = new TIntHashSet();
        for (RelatedResult result : concepts) {
            conceptIds.add(result.getIntId());
        }
        Iterable<TagApp> iterable = helperDao.get(new DaoFilter().setConceptIds(conceptIds.toArray()));
        Map<String, TagApp> tags = new HashMap<String, TagApp>();
        for (TagApp t : iterable) {
            tags.put(t.getTag().getNormalizedTag(), t);
        }
        RelatedResultList list = new RelatedResultList(maxResults, threshold);
        for (TagApp t : tags.values()) {
            list.add(new RelatedResult<TagApp>(t.getTag().getRawTag(), t, concepts.getValue(t.getConceptId())));
        }
        list.lock();
        return list;
    }

    @Override
    public double[][] corelatedness(TagApp[] objs) throws DaoException {
        return relator.corelatedness(getVector(objs));
    }

    @Override
    public double[][] corelatedness(TagApp[] xObjs, TagApp[] yObjs) throws DaoException {
        return relator.corelatedness(getVector(xObjs), getVector(yObjs));
    }

    private int[] getVector(TagApp[] objs) {
        int[] ids = new int[objs.length];
        for (int i=0; i<objs.length; i++) {
            ids[i] = objs[i].getConceptId();
        }
        return ids;
    }

    public static class Provider extends org.wikapidia.conf.Provider<TagAppRelator> {
        public Provider(Configurator configurator, Configuration config) throws ConfigurationException {
            super(configurator, config);
        }

        @Override
        public Class getType() {
            return TagAppRelator.class;
        }

        @Override
        public String getPath() {
            return "sem-tag.related.tagApp";
        }

        @Override
        public TagAppRelator get(String name, Config config) throws ConfigurationException {
            if (!config.getString("type").equals("tagApp")) {
                return null;
            }
            return new TagAppRelator(
                    getConfigurator().get(TagAppDao.class, config.getString("tagAppDao")),
                    getConfigurator().get(ConceptRelator.class, config.getString("relator"))
            );
        }
    }
}
