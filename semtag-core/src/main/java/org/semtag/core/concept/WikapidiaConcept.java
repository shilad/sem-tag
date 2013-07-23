package org.semtag.core.concept;

import org.wikapidia.core.model.LocalPage;

/**
 * @author Ari Weiland
 */
public class WikapidiaConcept extends Concept {

    private final LocalPage wikapidiaConceptId;

    public WikapidiaConcept(int conceptId, String type, LocalPage wikapidiaConceptId) {
        super(conceptId, type);
        this.wikapidiaConceptId = wikapidiaConceptId;
    }

    public LocalPage getWikapidiaConceptId() {
        return wikapidiaConceptId;
    }

    @Override
    public double getSimilarityTo(Concept other) {
        return 0;
    }
}
