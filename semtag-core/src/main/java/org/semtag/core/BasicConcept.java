package org.semtag.core;

/**
 *
 */
public class BasicConcept extends Concept {

    public BasicConcept(int conceptId, String type) {
        super(conceptId, type);
    }

    @Override
    public double getSimilarityTo(Concept other) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
