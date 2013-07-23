package org.semtag.core.model.concept;

import org.semtag.core.model.SemTagException;
import org.wikapidia.core.dao.DaoException;
import org.wikapidia.core.lang.LocalId;
import org.wikapidia.sr.LocalSRMetric;
import org.wikapidia.sr.SRResult;

/**
 * @author Ari Weiland
 */
public class WikapidiaConcept extends Concept {

    private final LocalId wikapidiaConceptId;
    private final LocalSRMetric srMetric;

    public WikapidiaConcept(String type, LocalId wikapidiaConceptId, LocalSRMetric srMetric) {
        super(wikapidiaConceptId.getId(), type);
        this.wikapidiaConceptId = wikapidiaConceptId;
        this.srMetric = srMetric;
    }

    public LocalId getWikapidiaConceptId() {
        return wikapidiaConceptId;
    }

    public LocalSRMetric getSrMetric() {
        return srMetric;
    }

    @Override
    public double getSimilarityTo(Concept other) throws SemTagException {
        try {
            SRResult result = srMetric.similarity(wikapidiaConceptId.asLocalPage(), ((WikapidiaConcept) other).wikapidiaConceptId.asLocalPage(), false);
            return result.getValue();
        } catch (DaoException e) {
            throw new SemTagException(e);
        }
    }

}
