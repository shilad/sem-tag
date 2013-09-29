package org.semtag.model.concept;

import gnu.trove.map.TIntDoubleMap;
import gnu.trove.map.TLongDoubleMap;
import gnu.trove.map.hash.TIntDoubleHashMap;
import gnu.trove.map.hash.TLongDoubleHashMap;

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
 * This class also offers add(), addAll(), and contains()
 * methods so that it can be used as a concept set to define
 * a vector space. These methods add the ID with value 1 unless
 * it is already in the vector.
 *
 * This class operates on a TIntDoubleMap.
 *
 * @author Ari Weiland
 */
public class ConceptVector {
    private final TLongDoubleMap vector;

    /**
     * Constructs an empty ConceptVector.
     */
    public ConceptVector() {
        vector = new TLongDoubleHashMap();
    }

    public ConceptVector(ConceptVector vector) {
        this.vector = vector.vector;
    }

    /**
     * Constructs a ConceptVector with concept mappings defined
     * by the input TIntDoubleMap.
     * @param vector
     */
    public ConceptVector(TLongDoubleMap vector) {
        this.vector = vector;
        prune();
    }

    /**
     * Constructs a ConceptVector with concept mappings defined
     * by the input Map.
     * @param vector
     */
    public ConceptVector(Map<Long, Double> vector) {
        this();
        this.vector.putAll(vector);
        prune();
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
        prune();
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
        prune();
    }

    /**
     * Puts the specified ID into the vector with the specified value.
     * @param id
     * @param value
     */
    public void put(long id, double value) {
        if (id >= 0) {
            vector.put(id, value);
        }
    }

    /**
     * Puts all the concept mappings defined by the input
     * TIntDoubleMap into the vector.
     * @param vector
     */
    public void putAll(TLongDoubleMap vector) {
        this.vector.putAll(vector);
        prune();
    }

    /**
     * Puts all the concept mappings defined by the input
     * Map into the vector.
     * @param vector
     */
    public void putAll(Map<Long, Double> vector) {
        this.vector.putAll(vector);
        prune();
    }

    /**
     * Increments the value of the ID by one.
     * @param id
     */
    public void increment(long id) {
        if (id >= 0) {
            int value = 1;
            if (vector.containsKey(id)) {
                value += vector.get(id);
            }
            vector.put(id, value);
        }
    }

    /**
     * Adds the ID to the vector with a value of 1 if it is not
     * already in the vector. Useful if values are irrelevant.
     * @param id
     */
    public void add(long id) {
        if (id >= 0 && !vector.containsKey(id)) {
            vector.put(id, 1);
        }
    }

    /**
     * Adds an array of IDs according to the add method.
     * @param ids
     */
    public void addAll(long[] ids) {
        for (long id : ids) {
            add(id);
        }
    }

    /**
     * Returns the value of the ID.
     * @param id
     * @return
     */
    public double get(long id) {
        return vector.get(id);
    }

    /**
     * Returns whether or not the vector contains the concept ID.
     * @param id
     * @return
     */
    public boolean containsConcept(long id) {
        return vector.containsKey(id);
    }

    /**
     * Removes the ID mapping.
     * Note that an ID mapped to 0 may still affect things
     * down the road, while a removed ID will not.
     * @param id
     */
    public void remove(long id) {
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
    public long[] getVectorSpace() {
        return vector.keys();
    }

    public Concept[] getVectorSpaceConcepts() {
        Concept concepts[] = new Concept[vector.size()];
        int i = 0;
        for (long id : vector.keys()) {
            concepts[i++] = new Concept(id);
        }
        return concepts;
    }

    /**
     * Returns this vector as a TIntDoubleMap.
     * @return
     */
    public TLongDoubleMap asTroveMap() {
        return vector;
    }

    private void prune() {
        if (vector.containsKey(-1)) {
            vector.remove(-1);
        }
    }
}
