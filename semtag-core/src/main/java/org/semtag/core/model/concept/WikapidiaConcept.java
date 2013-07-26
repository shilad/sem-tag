package org.semtag.core.model.concept;

import org.semtag.SemTagException;
import org.wikapidia.core.dao.DaoException;
import org.wikapidia.core.lang.Language;
import org.wikapidia.core.lang.LocalId;
import org.wikapidia.sr.LocalSRMetric;
import org.wikapidia.sr.SRResult;

/**
 * @author Ari Weiland
 */
public class WikapidiaConcept extends Concept<LocalId> {

    private final LocalSRMetric srMetric;

    public WikapidiaConcept(int conceptId, LocalSRMetric srMetric, byte[] objBytes) {
        super(conceptId, srMetric.getName(), objBytes);
        this.srMetric = srMetric;
    }

    public WikapidiaConcept(LocalId conceptObj, LocalSRMetric srMetric) {
        super(conceptObj.getId(), srMetric.getName(), conceptObj);
        this.srMetric = srMetric;
    }

    public LocalSRMetric getSrMetric() {
        return srMetric;
    }

    @Override
    public double getSimilarityTo(Concept other) throws SemTagException {
        if (!(other instanceof WikapidiaConcept)) {
            throw new IllegalArgumentException("Concept types do not match");
        }
        try {
            SRResult result = srMetric.similarity(
                    conceptObj.asLocalPage(),
                    ((WikapidiaConcept) other).conceptObj.asLocalPage(),
                    false);
            return result.getValue();
        } catch (DaoException e) {
            throw new SemTagException(e);
        }
    }

    @Override
    protected String conceptObjToString() {
        return conceptObj.getId() + " " +conceptObj.getLanguage().getId();
    }

    @Override
    protected LocalId stringToConceptObj(String s) {
        String[] split = s.split(" ");
        return new LocalId(
                Language.getById(new Integer(split[1])),
                new Integer(split[0]));
    }
}
