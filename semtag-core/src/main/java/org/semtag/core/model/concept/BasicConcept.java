package org.semtag.core.model.concept;

import java.util.Collection;

/**
 * @author Yulun Li
 */
public class BasicConcept extends Concept {

    private final Collection<String> stems;

    public BasicConcept(int conceptId, String type, Collection<String> stems) {
        super(conceptId, type);
        this.stems = stems;
    }

    public Collection<String> getStems() {
        return stems;
    }

    @Override
    public double getSimilarityTo(Concept other) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
