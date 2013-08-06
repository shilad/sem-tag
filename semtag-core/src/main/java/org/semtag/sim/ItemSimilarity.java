package org.semtag.sim;

import com.typesafe.config.Config;
import org.apache.commons.lang3.ArrayUtils;
import org.semtag.dao.DaoException;
import org.semtag.dao.DaoFilter;
import org.semtag.dao.TagAppDao;
import org.semtag.model.Item;
import org.semtag.model.Tag;
import org.semtag.model.TagApp;
import org.semtag.model.TagAppGroup;
import org.semtag.model.concept.ConceptVector;
import org.wikapidia.conf.Configuration;
import org.wikapidia.conf.ConfigurationException;
import org.wikapidia.conf.Configurator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
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
        if (x.equals(y)) {
            return 1;
        }
        int[] vectorSpace = getVectorSpace(x, y);
        double[][] matrix = sim.cosimilarity(vectorSpace);
        return similarity(x, y, vectorSpace, matrix);
    }

    /**
     * Returns the similarity between two items within a given concept ID
     * vectorspace. The matrix MUST have been generated by a call to
     * ConceptSimilarity.cosimilarity(vectorSpace), or else the result will
     * be meaningless.
     * @param x
     * @param y
     * @param vectorSpace
     * @param matrix
     * @return
     * @throws DaoException
     */
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
            for (TagApp t : groupX) {
                if (vectorSpace[i] == t.getConceptId()) {
                    aX[i]++;
                }
            }
            for (TagApp t : groupY) {
                if (vectorSpace[i] == t.getConceptId()) {
                    aY[i]++;
                }
            }
        }
        return cosineSimilarity(aX, aY, matrix);
    }

    /**
     * Returns the similarity between a concept ID vector and an item.
     * Not really intended for use outside of this class.
     * @param vector
     * @param item
     * @return
     * @throws DaoException
     */
    public double similarity(ConceptVector vector, Item item) throws DaoException {
        ConceptVector space = new ConceptVector(vector);
        space.addAll(vector.getVectorSpace());
        int[] vectorSpace = space.getVectorSpace();
        double[][] matrix = sim.cosimilarity(vectorSpace);
        return similarity(vector, item, vectorSpace, matrix);
    }

    /**
     * Returns the similarity between a concept ID vector and an item.
     * Not really intended for use outside of this class.
     * The matrix MUST have been generated by a call to
     * ConceptSimilarity.cosimilarity(vectorSpace), or else the result will
     * be meaningless.
     * @param vector
     * @param item
     * @param vectorSpace
     * @param matrix
     * @return
     * @throws DaoException
     */
    public double similarity(ConceptVector vector, Item item, int[] vectorSpace, double[][] matrix) throws DaoException {
        TagAppGroup group = helperDao.getGroup(new DaoFilter().setItemId(item.getItemId()));
        int dim = vectorSpace.length;

        // convert to alpha vector form
        int[] aX = new int[dim]; // alpha vector representation of groupX concepts in specified vector space
        int[] aY = new int[dim]; // alpha vector representation of groupY concepts in specified vector space
        for (int i=0; i<dim; i++) {
            for (int id : vector.getVectorSpace()) {
                if (vectorSpace[i] == id) {
                    aX[i] += vector.get(id);
                }
            }
            for (TagApp t : group) {
                if (vectorSpace[i] == t.getConceptId()) {
                    aY[i]++;
                }
            }
        }
        return cosineSimilarity(aX, aY, matrix);
    }

    /**
     * Private cosine similarity method defines how ItemSimilarity calculates
     * cosine similarity for two alpha vectors and a given matrix.
     * @param aX
     * @param aY
     * @param matrix
     * @return
     */
    private double cosineSimilarity(int[] aX, int[] aY, double[][] matrix) {
        int dim = aX.length;
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

    @Override
    public SimilarResultList mostSimilar(Item obj, int maxResults) throws DaoException {
        TagAppGroup group = helperDao.getGroup(new DaoFilter().setItemId(obj.getItemId()));
        ConceptVector vector = new ConceptVector();
        for (TagApp t : group) {
            vector.increment(t.getConceptId());
        }
        return mostSimilar(vector, maxResults);
    }

    /**
     * Returns the most similar items to an array of TagApps.
     * @param tagApps
     * @param maxResults
     * @return
     * @throws DaoException
     */
    public SimilarResultList mostSimilar(TagApp[] tagApps, int maxResults) throws DaoException {
        ConceptVector vector = new ConceptVector();
        for (TagApp t : tagApps) {
            vector.increment(t.getConceptId());
        }
        return mostSimilar(vector, maxResults);
    }

    /**
     * Returns the most similar items to a collection of TagApps.
     * @param tagApps
     * @param maxResults
     * @return
     * @throws DaoException
     */
    public SimilarResultList mostSimilar(Collection<TagApp> tagApps, int maxResults) throws DaoException {
        ConceptVector vector = new ConceptVector();
        for (TagApp t : tagApps) {
            vector.increment(t.getConceptId());
        }
        return mostSimilar(vector, maxResults);
    }

    /**
     * Returns the most similar items to a concept vector.
     * @param vector
     * @param maxResults
     * @return
     * @throws DaoException
     */
    public SimilarResultList mostSimilar(ConceptVector vector, int maxResults) throws DaoException {
        ConceptVector space = new ConceptVector();
        for (int id : vector.getVectorSpace()) {
            space.add(id);
            SimilarResultList conceptList = sim.mostSimilar(id, maxResults);
            for (SimilarResult result : conceptList) {
                space.add(result.getIntId());
            }
        }
        int[] vectorSpace = space.getVectorSpace();
        double[][] matrix = sim.cosimilarity(vectorSpace);
        Iterable<TagApp> iterable = helperDao.get(new DaoFilter().setConceptIds(vectorSpace));
        Map<String, Item> items = new HashMap<String, Item>();
        for (TagApp t : iterable) {
            items.put(t.getItem().getItemId(), t.getItem());
        }
        SimilarResultList list = new SimilarResultList(maxResults);
        for (Item item : items.values()) {
            list.add(new SimilarResult(item.getItemId(), similarity(vector, item, vectorSpace, matrix)));
        }
        list.lock();
        return list;
    }

    @Override
    public SimilarResultList mostSimilar(Tag tag, int maxResults) throws DaoException {
        throw new UnsupportedOperationException("This method would be meaningless. " +
                "Instead, use mostSimilar(TagApp[], int) or mostSimilar(Collection<TagApp>, int)");
    }

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

    @Override
    public double[][] cosimilarity(Item[] xObjs, Item[] yObjs) throws DaoException {
        int[] vectorSpace = getVectorSpace((Item[]) ArrayUtils.addAll(xObjs, yObjs));
        double[][] matrix = sim.cosimilarity(vectorSpace);
        double[][] output = new double[xObjs.length][yObjs.length];
        for (int i=0; i<xObjs.length; i++) {
            for (int j=0; j<yObjs.length; j++) {
                output[i][j] = similarity(xObjs[i], yObjs[j], vectorSpace, matrix);
            }
        }
        return output;
    }

    private int[] getVectorSpace(Item... items) throws DaoException {
        ConceptVector vectorSpace = new ConceptVector();
        for (Item item : items) {
            TagAppGroup group = helperDao.getGroup(new DaoFilter().setItemId(item.getItemId()));
            for (TagApp t : group.getTagApps()) {
                if (t.getConceptId() > -1) {
                    vectorSpace.add(t.getConceptId());
                }
            }
        }
        return vectorSpace.getVectorSpace();
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
