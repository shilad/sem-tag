package org.semtag.core.model.concept;

import org.semtag.SemTagException;

/**
 * @author Ari Weiland
 * @author Yulun Li
 */
public abstract class Concept<I> {
    private final int conceptId;
    private final String metric;
    private final I conceptObj;

    protected Concept(int conceptId, String metric, I conceptObj) {
        this.conceptId = conceptId;
        this.metric = metric;
        this.conceptObj = conceptObj;
    }

    public int getConceptId() {
        return conceptId;
    }

    public String getMetric() {
        return metric;
    }

    public abstract double getSimilarityTo(Concept other) throws SemTagException;
}
