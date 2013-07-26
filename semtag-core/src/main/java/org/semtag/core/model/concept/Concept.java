package org.semtag.core.model.concept;

import org.semtag.SemTagException;

/**
 * @author Ari Weiland
 * @author Yulun Li
 */
public abstract class Concept<I> {
    protected final int conceptId;
    protected final String metric;
    protected I conceptObj;

    public Concept(int conceptId, String metric, I conceptObj) {
        this.conceptId = conceptId;
        this.metric = metric;
        this.conceptObj = conceptObj;
    }

    protected Concept(int conceptId, String metric) {
        this.conceptId = conceptId;
        this.metric = metric;
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

    public abstract double getSimilarityTo(Concept other) throws SemTagException;

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
}
