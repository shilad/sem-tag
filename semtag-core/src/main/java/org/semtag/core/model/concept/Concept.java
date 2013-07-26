package org.semtag.core.model.concept;

import org.semtag.SemTagException;
import org.semtag.core.model.Similar;

/**
 * @author Ari Weiland
 * @author Yulun Li
 */
public abstract class Concept<I> implements Similar<Concept<I>> {
    protected final int conceptId;
    protected final String metric;
    protected final I conceptObj;

    /**
     * Constructs a concept with given ID, metric, and concept object.
     * @param conceptId
     * @param metric
     * @param conceptObj
     */
    public Concept(int conceptId, String metric, I conceptObj) {
        this.conceptId = conceptId;
        this.metric = metric;
        this.conceptObj = conceptObj;
    }

    /**
     * Constructs a concept with given ID, metric, and concept object
     * extracted by the {@code bytesToConceptObj} method.
     * @param conceptId
     * @param metric
     * @param objBytes
     */
    public Concept(int conceptId, String metric, byte[] objBytes) {
        this.conceptId = conceptId;
        this.metric = metric;
        this.conceptObj = bytesToConceptObj(objBytes);
    }

    public int getConceptId() {
        return conceptId;
    }

    public String getMetric() {
        return metric;
    }

    public I getConceptObj() {
        return conceptObj;
    }

    /**
     * Describes how Sem-Tag will determine the similarity between two concepts,
     * and by extension two tags.
     * @param other
     * @return
     * @throws SemTagException
     */
    public abstract double getSimilarityTo(Concept<I> other) throws SemTagException;

    /**
     * Describes how to convert a conceptObj to a String.
     * Necessary for proper implementation of conceptObjToBytes().
     *
     * @return
     */
    protected abstract String conceptObjToString();

    /**
     * Describes how to convert a String back into a conceptObj.
     * Should reverse the conversion used by conceptObjToString().
     * Necessary for proper implementation of bytesToConceptObj().
     *
     * @param s
     * @return
     */
    protected abstract I stringToConceptObj(String s);

    /**
     * Converts the conceptObj to an array of bytes.
     *
     * @return
     */
    public byte[] conceptObjToBytes() {
        return conceptObjToString().getBytes();
    }

    /**
     * Converts an array of bytes back to a conceptObj.
     *
     * @param bytes
     * @return
     */
    public I bytesToConceptObj(byte[] bytes) {
        return stringToConceptObj(new String(bytes));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Concept)) return false;

        Concept concept = (Concept) o;

        return conceptId == concept.conceptId && metric.equals(concept.metric);

    }

    @Override
    public String toString() {
        return "Concept{" +
                "conceptId=" + conceptId +
                ", metric=\'" + metric + '\'' +
                ", conceptObj=" + conceptObj +
                '}';
    }
}
