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
        int[] vectorSpace = getVectorSpace(x, y);
        double[][] matrix = sim.cosimilarity(vectorSpace);
        return similarity(x, y, vectorSpace, matrix);
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

        // calculate cosine similarity
        double xDotX = 0.0;
        double yDotY = 0.0;
        double xDotY = 0.0;
        for (int i=0; i<dim; i++) {
            // calculate beta vector values
            double bX = 0;
            double bY = 0;
            for (int j=0; j<dim; j++) {
                bX += matrix[i][j] * aX[j];
                bY += matrix[i][j] * aY[j];
            }
            // calculate cosine similarity between beta vector values
            xDotX += bX * bX;
            yDotY += bY * bY;
            xDotY += bX * bY;
        }
        return xDotY / Math.sqrt(xDotX * yDotY);
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
                conceptIds.add(cId);    // divided by 2 a modification to improve speed
                SimilarResultList conceptList = sim.mostSimilar(cId, maxResults/2);
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
        int[] vectorSpace = getVectorSpace(objs);
        double[][] matrix = sim.cosimilarity(vectorSpace);
        double[][] output = new double[dim][dim];
        for (int i=0; i<dim; i++) {
            for (int j=0; j<=i; j++) {
                // For efficiency, only calculate cosimilarity in the upper triangle of the matrix
                output[i][j] = similarity(objs[i], objs[j], vectorSpace, matrix);
                output[j][i] = output[i][j];
            }
        }
        return output;
    }

    private int[] getVectorSpace(Item... items) throws DaoException {
        TIntSet conceptIds = new TIntHashSet();
        for (Item item : items) {
            TagAppGroup group = helperDao.getGroup(new DaoFilter().setItemId(item.getItemId()));
            for (TagApp t : group.getTagApps()) {
                if (t.getConceptId() > -1) {
                    conceptIds.add(t.getConceptId());
                }
            }
        }
        return conceptIds.toArray();
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
