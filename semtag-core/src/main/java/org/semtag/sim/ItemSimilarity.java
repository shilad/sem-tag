package org.semtag.sim;

import com.typesafe.config.Config;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import org.semtag.dao.DaoException;
import org.semtag.dao.DaoFilter;
import org.semtag.dao.TagAppDao;
import org.semtag.model.Item;
import org.semtag.model.TagApp;
import org.semtag.model.TagAppGroup;
import org.wikapidia.conf.Configuration;
import org.wikapidia.conf.ConfigurationException;
import org.wikapidia.conf.Configurator;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: This class is SO UNGODLY SLOW. It runs upwards of 50-100 times slower than the others!
 * @author Ari Weiland
 */
public class ItemSimilarity implements Similar<Item> {

    private final TagAppDao helperDao;
    private final ConceptSimilarity sim;

    public ItemSimilarity(TagAppDao helperDao, ConceptSimilarity sim) {
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
    public double similarity(Item x, Item y) throws DaoException {
        TagAppGroup groupX = helperDao.getGroup(new DaoFilter().setItemId(x.getItemId()));
        TagAppGroup groupY = helperDao.getGroup(new DaoFilter().setItemId(y.getItemId()));

        // set of concept IDs to become the vector space
        TIntSet cIds = new TIntHashSet();
        // maps X's concept IDs to amount they are tagged
        Map<Integer, Integer> mapX = new HashMap<Integer, Integer>();
        // maps Y's concept IDs to amount they are tagged
        Map<Integer, Integer> mapY = new HashMap<Integer, Integer>();
        for (TagApp t : groupX.getTagApps()) {
            int cId = t.getConceptId();
            if (cId > -1) {
                cIds.add(cId);
                int count = 1;
                if (mapX.containsKey(cId)) {
                    count = mapX.get(cId) + 1;
                }
                mapX.put(cId, count);
            }
        }
        for (TagApp t : groupY.getTagApps()) {
            int cId = t.getConceptId();
            if (cId > -1) {
                cIds.add(cId);
                int count;
                if (mapY.containsKey(cId)) {
                    count = mapY.get(cId) + 1;
                } else {
                    count = 1;
                }
                mapY.put(cId, count);
            }
        }
        int dim = cIds.size();

        // convert to vector form
        // concept vector space
        int[] vectorSpace = cIds.toArray();
        // alpha vector representation of mapX values in above vector space
        int[] aX = new int[dim];
        // alpha vector representation of mapY values in above vector space
        int[] aY = new int[dim];
        for (int i=0; i<dim; i++) {
            int cId = vectorSpace[i];
            int count = 0;
            if (mapX.containsKey(cId)) {
                count = mapX.get(cId);
            }
            aX[i] = count;
            count = 0;
            if (mapY.containsKey(cId)) {
                count = mapY.get(cId);
            }
            aY[i] = count;
            i++;
        }
        double[][] matrix = sim.cosimilarity(vectorSpace);
        return itemSimAlg(aX, aY, matrix);
    }

    public double similarity(Item x, Item y, int[] vectorSpace, double[][] matrix) throws DaoException {
        TagAppGroup groupX = helperDao.getGroup(new DaoFilter().setItemId(x.getItemId()));
        TagAppGroup groupY = helperDao.getGroup(new DaoFilter().setItemId(y.getItemId()));
        int dim = vectorSpace.length;

        // convert to vector form
        // alpha vector representation of groupX concepts in specified vector space
        int[] aX = new int[dim];
        // alpha vector representation of groupY concepts in specified vector space
        int[] aY = new int[dim];
        for (int i=0; i<dim; i++) {
            int cId = vectorSpace[i];
            for (TagApp t : groupX) {
                if (cId == t.getConceptId()) {
                    aX[i]++;
                }
            }
            for (TagApp t : groupY) {
                if (cId == t.getConceptId()) {
                    aY[i]++;
                }
            }
        }
        return itemSimAlg(aX, aY, matrix);
    }

    private double itemSimAlg(int[] aX, int[] aY, double[][] matrix) {
        int dim = aX.length;
        double[] bX = new double[dim];
        double[] bY = new double[dim];
        double xDotX = 0.0;
        double yDotY = 0.0;
        double xDotY = 0.0;
        for (int i=0; i<dim; i++) {
            // calculate beta vectors
            for (int j=0; j<dim; j++) {
                bX[i] += matrix[i][j] * aX[j];
                bY[i] += matrix[i][j] * aY[j];
            }
            // calculate cosine similarity between beta vectors
            xDotX += bX[i] * bX[i];
            yDotY += bY[i] * bY[i];
            xDotY += bX[i] * bY[i];
        }
        return xDotY / Math.sqrt(xDotX * yDotY);
    }

    @Override
    public SimilarResultList mostSimilar(Item obj, int maxResults) throws DaoException {
        TagAppGroup group = helperDao.getGroup(new DaoFilter().setItemId(obj.getItemId()));
        TIntSet conceptIds = new TIntHashSet();
        for (TagApp t : group) {
            int cId = t.getConceptId();
            if (cId > -1) {
                conceptIds.add(cId); // divided by 2 a modification to improve speed
                SimilarResultList conceptList = sim.mostSimilar(cId, maxResults);
                for (SimilarResult result : conceptList) {
                    if (result.getIntId() > -1) {
                        conceptIds.add(result.getIntId());
                    }
                }
            }
        }
        int[] vectorSpace = conceptIds.toArray();
        double[][] matrix = sim.cosimilarity(vectorSpace);
        Iterable<TagApp> iterable = helperDao.get(new DaoFilter().setConceptIds(vectorSpace));
        SimilarResultList list = new SimilarResultList(maxResults);
        for (TagApp t : iterable) {
            Item item = t.getItem();
            list.add(new SimilarResult(item.getItemId(), similarity(obj, item, vectorSpace, matrix)));
        }
        list.lock();
        return list;
    }

    // TODO: is there a way to not do this manually?
    @Override
    public double[][] cosimilarity(Item[] objs) throws DaoException {
        int dim = objs.length;
        double[][] matrix = new double[dim][dim];
        for (int i=0; i<dim; i++) {
            for (int j=0; j<dim; j++) {
                // For efficiency, only cosimilarity in the upper triangle of the matrix is calculated
                if (i > j) {
                    matrix[i][j] = matrix[j][i];
                } else {
                    matrix[i][j] = similarity(objs[i], objs[j]);
                }
            }
        }
        return matrix;
    }

    public static class Provider extends org.wikapidia.conf.Provider<ItemSimilarity> {
        public Provider(Configurator configurator, Configuration config) throws ConfigurationException {
            super(configurator, config);
        }

        @Override
        public Class getType() {
            return ItemSimilarity.class;
        }

        @Override
        public String getPath() {
            return "sem-tag.similar.item";
        }

        @Override
        public ItemSimilarity get(String name, Config config) throws ConfigurationException {
            if (!config.getString("type").equals("item")) {
                return null;
            }
            return new ItemSimilarity(
                    getConfigurator().get(TagAppDao.class, config.getString("tagAppDao")),
                    getConfigurator().get(ConceptSimilarity.class, config.getString("conceptSim"))
            );
        }
    }
}
