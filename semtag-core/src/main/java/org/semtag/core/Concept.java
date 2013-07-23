package org.semtag.core;

/**
 *
 */
public class Concept {
    private final int conceptId;
    private final String planText;

    public Concept(int conceptId, String planText) {
        this.conceptId = conceptId;
        this.planText = planText;
    }

    public int getConceptId() {
        return conceptId;
    }

    public String getPlanText() {
        return planText;
    }
}
