package org.semtag.model.concept;

import gnu.trove.map.TIntDoubleMap;
import gnu.trove.map.hash.TIntDoubleHashMap;

import java.util.Collection;
import java.util.Map;

/**
 * @author Ari Weiland
 */
public class ConceptVector {
    private final TIntDoubleMap vector;

    public ConceptVector() {
        vector = new TIntDoubleHashMap();
    }

    public ConceptVector(TIntDoubleMap vector) {
        this.vector = vector;
    }

    public ConceptVector(Map<Integer, Double> vector) {
        this();
        this.vector.putAll(vector);
    }

    public ConceptVector(Concept[] concepts) {
        this();
        for (Concept concept : concepts) {
            increment(concept.getConceptId());
        }
    }

    public ConceptVector(Collection<Concept> concepts) {
        this();
        for (Concept concept : concepts) {
            increment(concept.getConceptId());
        }
    }

    public void put(int id, double value) {
        vector.put(id, value);
    }

    public void putAll(TIntDoubleMap vector) {
        this.vector.putAll(vector);
    }

    public void putAll(Map<Integer, Double> vector) {
        this.vector.putAll(vector);
    }

    public void increment(int id) {
        int value = 1;
        if (vector.containsKey(id)) {
            value += vector.get(id);
        }
        vector.put(id, value);
    }

    public double get(int id) {
        return vector.get(id);
    }

    public void remove(int id) {
        vector.remove(id);
    }

    public void clear() {
        vector.clear();
    }

    public boolean isEmpty() {
        return vector.isEmpty();
    }

    public int[] getVectorSpace() {
        return vector.keys();
    }

    public TIntDoubleMap asTroveMap() {
        return vector;
    }
}
