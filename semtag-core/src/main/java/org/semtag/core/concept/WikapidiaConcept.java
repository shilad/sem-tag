package org.semtag.core.concept;

import org.semtag.core.SemTagException;
import org.wikapidia.core.dao.DaoException;
import org.wikapidia.core.model.LocalPage;
import org.wikapidia.sr.LocalSRMetric;
import org.wikapidia.sr.SRResult;

/**
 * @author Ari Weiland
 */
public class WikapidiaConcept extends Concept {

    private final LocalPage wikapidiaConceptId;
    private final LocalSRMetric srMetric;

    public WikapidiaConcept(int conceptId, String type, LocalPage wikapidiaConceptId, LocalSRMetric srMetric) {
        super(conceptId, type);
        this.wikapidiaConceptId = wikapidiaConceptId;
        this.srMetric = srMetric;
    }

    public LocalPage getWikapidiaConceptId() {
        return wikapidiaConceptId;
    }

    public LocalSRMetric getSrMetric() {
        return srMetric;
    }

    @Override
    public double getSimilarityTo(Concept other) throws SemTagException {
        try {
            SRResult result = srMetric.similarity(wikapidiaConceptId, ((WikapidiaConcept) other).wikapidiaConceptId, false);
            return result.getValue();
        } catch (DaoException e) {
            throw new SemTagException(e);
        }
    }
}
