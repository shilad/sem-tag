package org.semtag.core;

import org.wikapidia.core.lang;

/**
 * @author Ari Weiland
 */
public class SingletonConcept extends Concept {

    private final LocalId wikapidiaConceptId;

    public SingletonConcept(int conceptId, String type, LocalId wikapidiaConceptId) {
        super(conceptId, type);
        this.wikapidiaConceptId = wikapidiaConceptId;
    }



    @Override
    public float getSimilarityTo(Concept other) {
        return 0;
    }
}
