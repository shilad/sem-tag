package org.semtag.core.model.concept;

import org.semtag.SemTagException;

/**
 * @author Ari Weiland
 * @author Yulun Li
 */
public abstract class Concept {
    private final int conceptId;
    private final String type;

    public Concept(int conceptId, String type) {
        this.conceptId = conceptId;
        this.type = type;
    }

    public int getConceptId() {
        return conceptId;
    }

    public String getType() {
        return type;
    }

    public abstract double getSimilarityTo(Concept other) throws SemTagException;
}
