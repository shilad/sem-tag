package org.semtag.model.concept;

import gnu.trove.map.TIntDoubleMap;
import gnu.trove.map.hash.TIntDoubleHashMap;

import java.util.Collection;
import java.util.Map;

/**
 * This class describes a concept vector, where concept IDs
 * are mapped to double values. The values themselves are
 * generally irrelevant, and only matter with respect to each
 * other. The intended use for values is as an integer that
 * represents the amount of occurrences of that concept. This
 * led to the increment(int id) method, as well as all the
 * standard map methods. Still, the double implementation
 * allows other usages.
 *
 * This class operates on a TIntDoubleMap.
 *
 * @author Ari Weiland
 */
public class ConceptVector {
    private final TIntDoubleMap vector;

    /**
     * Constructs an empty ConceptVector.
     */
    public ConceptVector() {
        vector = new TIntDoubleHashMap();
    }

    /**
     * Constructs a ConceptVector with concept mappings defined
     * by the input TIntDoubleMap.
     * @param vector
     */
    public ConceptVector(TIntDoubleMap vector) {
        this.vector = vector;
    }

    /**
     * Constructs a ConceptVector with concept mappings defined
     * by the input Map.
     * @param vector
     */
    public ConceptVector(Map<Integer, Double> vector) {
        this();
        this.vector.putAll(vector);
    }

    /**
     * Constructs a ConceptVector with IDs of the specified
     * concepts and mapping to occurrence in the array.
     * @param concepts
     */
    public ConceptVector(Concept[] concepts) {
        this();
        for (Concept concept : concepts) {
            increment(concept.getConceptId());
        }
    }

    /**
     * Constructs a ConceptVector with IDs of the specified
     * concepts and mapping to occurrence in the collection.
     * @param concepts
     */
    public ConceptVector(Collection<Concept> concepts) {
        this();
        for (Concept concept : concepts) {
            increment(concept.getConceptId());
        }
    }

    /**
     * Puts the specified ID into the vector with the specified value.
     * @param id
     * @param value
     */
    public void put(int id, double value) {
        vector.put(id, value);
    }

    /**
     * Puts all the concept mappings defined by the input
     * TIntDoubleMap into the vector.
     * @param vector
     */
    public void putAll(TIntDoubleMap vector) {
        this.vector.putAll(vector);
    }

    /**
     * Puts all the concept mappings defined by the input
     * Map into the vector.
     * @param vector
     */
    public void putAll(Map<Integer, Double> vector) {
        this.vector.putAll(vector);
    }

    /**
     * Increments the value of the ID by one.
     * @param id
     */
    public void increment(int id) {
        int value = 1;
        if (vector.containsKey(id)) {
            value += vector.get(id);
        }
        vector.put(id, value);
    }

    /**
     * Returns the value of the ID.
     * @param id
     * @return
     */
    public double get(int id) {
        return vector.get(id);
    }

    /**
     * Removes the ID mapping.
     * Note that an ID mapped to 0 may still affect things
     * down the road, while a removed ID will not.
     * @param id
     */
    public void remove(int id) {
        vector.remove(id);
    }

    /**
     * Clears the vector.
     */
    public void clear() {
        vector.clear();
    }

    /**
     * Returns true if the vector is empty.
     * @return
     */
    public boolean isEmpty() {
        return vector.isEmpty();
    }

    /**
     * Returns the dimension of this vector.
     * ie the number of unique concepts in this vector.
     * @return
     */
    public int size() {
        return vector.size();
    }

    /**
     * Returns an arbitrarily ordered array of the concepts
     * mapped in this vector. This includes concepts mapped to 0.
     * @return
     */
    public int[] getVectorSpace() {
        return vector.keys();
    }

    /**
     * Returns this vector as a TIntDoubleMap.
     * @return
     */
    public TIntDoubleMap asTroveMap() {
        return vector;
    }
}
